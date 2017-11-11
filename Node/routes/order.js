const express = require('express');
const router = express.Router();
const async = require('async');
const uuidv4 = require('uuid/v4');
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

// receive payment order and verify the signature and later emit a token
router.post('/', function (req, res) {
	const idUser = req.body.idUser;
	// const encrypted = req.params.encrypted; // hashed with sha1. encrypted with private key. now i have to verify it with public key
	// carry on
	var stmt = db.prepare('SELECT * FROM Cart, CartItem, Product WHERE idUser = ? AND Cart.idCart = CartItem.idCart AND Product.idProduct = CartItem.idProduct');
	stmt.all(idUser, (err, cart) => {
		console.log(cart);
		// criar order
		var uuid = uuidv4();
		stmt = db.prepare('INSERT INTO Orders (idOrder, day, idUser) VALUES (?, date(\'now\'), ?)');
		stmt.get([uuid, idUser], (err, t) => {
			// criar order items
			async.each(cart, (c, callback) => {
				stmt = db.prepare('INSERT INTO OrderItem (quantity, idProduct, idOrder) VALUES (?, ?, ?)');
				stmt.get([c.quantity, c.idProduct, uuid], (err, t1) => {
					callback();
				});
			}, (err) => {
				if (err)
					console.log(err);
				else res.json(uuid);
			});
		});
	});
});


// retrieve order information given UUID
router.get('/:uuid', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Orders, OrderItem, Product WHERE Orders.idOrder = OrderItem.idOrder AND Product.idProduct = OrderItem.idProduct AND Orders.idOrder = ?');
	stmt.get(req.params.uuid, (err, info) => {
		if (info.idOrder == req.params.uuid) {
			res.send(info);
		} else {
			console.log(info);
			res.send('Error');
		}
	});
});

// retrieve order information and client information
router.get('/printer/:uuid', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Orders, OrderItem, Product, User WHERE Orders.idOrder = OrderItem.idOrder AND User.idUser = Orders.idUser AND Product.idProduct = OrderItem.idProduct AND Orders.idOrder = ?');
	stmt.get(req.params.uuid, (err, info) => {
		if (info.idOrder == req.params.uuid) {
			res.send(info);
		} else {
			console.log(info);
			res.send('Error');
		}
	});
});

// retrieve all orders of a given customer
router.get('/previous/:idUser', function (req, res) {
	var retorno = [];
	const stmt = db.prepare('SELECT * FROM Orders, OrderItem, Product WHERE idUser = ? AND Orders.idOrder = orderItem.idOrder AND Product.idProduct = orderItem.idProduct');
	stmt.all(req.params.idUser, (err, orders) => {
		console.log(orders);
		orders.sort(function (a, b) {
			return a.idOrder > b.idOrder;
		});
		var temp = '';
		var j = -1;
		for (var i = 0; i < orders.length; i++) {
			if (temp == orders[i].idOrder) {
				retorno[j].products.push({
					idProduct: orders[i].idProduct,
					quantity: orders[i].quantity,
					maker: orders[i].maker,
					model: orders[i].model,
					price: orders[i].price,
					description: orders[i].description,
				});
			} else {
				j = j + 1;
				temp = orders[i].idOrder;
				retorno.push({
					idOrder: orders[i].idOrder,
					day: orders[i].day,
					products: [],
				});
				retorno[j].products.push({
					idProduct: orders[i].idProduct,
					quantity: orders[i].quantity,
					maker: orders[i].maker,
					model: orders[i].model,
					price: orders[i].price,
					description: orders[i].description,
				});
			}
		}
		if (orders.length > 0) {
			res.send(retorno);
		} else {
			console.log(orders);
			res.send('No Orders');
		}
	});
});

module.exports = router;