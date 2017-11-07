const express = require('express');
const router = express.Router();
const jsrsassign = require('jsrsassign');
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

// receive payment order and verify the signature and later emit a token
router.post('/', function (req, res) {
	const email = req.params.email;
	const encrypted = req.params.encrypted; // hashed with sha1. encrypted with private key. now i have to verify it with public key
	const stmt = db.prepare('SELECT * FROM User WHERE email = ?');
	stmt.get(email, (err, user) => {
		if (user.email == email) {
			// carry on
			
		} else {
			console.log(user);
			res.send('Error');
		}
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
router.get('/previous/:idUser', function (req, res ) {
	const stmt = db.prepare('SELECT * FROM Orders, OrderItem WHERE Orders.idOrder = OrderItem.idOrder AND idUser = ?');
	stmt.get(req.params.idUser, (err, orders) => {
		if (orders.idUser == req.params.idUser) {
			res.send(orders);
		} else {
			console.log(orders);
			res.send('Error');
		}
	});
});

module.exports = router;