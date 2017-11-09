const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

// send cart info given id of the user
router.get('/:idUser', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Cart, CartItem, Product WHERE idUser = ? AND Cart.idCart = CartItem.idCart AND Product.idProduct = CartItem.idProduct');
	console.log(req.params.idUser);
	stmt.get(req.params.idUser, (err, rows) => {
		if (rows.idUser != undefined && rows.idUser != null) {
			res.json(rows); // id is valid
		} else {
			console.log(rows);
			res.json('Error'); // id doesn't exist
		}
	});
});

// add product to cart
router.get('/add/:idUser/:idProduct', function (req, res) {
	var stmt = db.prepare('SELECT * FROM Cart, CartItem WHERE idUser = ? AND Cart.idCart = CartItem.idCart');
	stmt.get(req.params.idUser, (err, cart) => {
		if (cart.idUser == req.params.idUser) {
			if (cart.idProduct == req.params.idProduct) {
				stmt = db.prepare('UPDATE CartItem SET quantity = ? WHERE idProduct = ? AND idCart = ?');
				stmt.get([cart.quantity + 1, cart.idProduct, cart.idCart], (err, result) => {
					console.log(result);
					res.json('Success adding another product of the same type');
				});
			} else {
				stmt = db.prepare('INSERT INTO CartItem (quantity, idProduct, idCart) VALUES (?, ?, ?)');
				stmt.get([1, req.params.idProduct, cart.idCart], (err, result) => {
					console.log(result);
					res.json('Success adding a different product');
				});
			}
		} else {
			console.log(cart);
			res.json('Error');
		}
	});
});

// remove product from cart
router.get('/remove/:idUser/:idProduct', function (req, res) {
	var stmt = db.prepare('SELECT * FROM Cart, CartItem WHERE idUser = ? AND Cart.idCart = CartItem.idCart');
	stmt.get(req.params.idUser, (err, cart) => {
		if (cart.idUser == req.params.idUser) {
			if (cart.idProduct == req.params.idProduct) {
				stmt = db.prepare('DELETE FROM CartItem WHERE idProduct = ? AND idCart = ?');
				stmt.get([cart.idProduct, cart.idCart], (err, result) => {
					console.log(result);
					res.json('Success removing product');
				});
			} else {
				console.log(cart);
				res.json('That product doesn\'t exist in the cart');
			}
		} else {
			console.log(cart);
			res.json('Error');
		}
	});
});

// add quantity to product on cart
router.get('/add/:idUser/:idProduct/:quantity', function (req, res) {
	var stmt = db.prepare('SELECT * FROM Cart, CartItem WHERE idUser = ? AND Cart.idCart = CartItem.idCart');
	stmt.get(req.params.idUser, (err, cart) => {
		if (cart.idUser == req.params.idUser) {
			if (cart.idProduct == req.params.idProduct) {
				stmt = db.prepare('UPDATE CartItem SET quantity = ? WHERE idProduct = ? AND idCart = ?');
				stmt.get([cart.quantity + req.params.quantity, cart.idProduct, cart.idCart], (err, result) => {
					console.log(result);
					res.json('Success adding another product of the same type');
				});
			} else {
				console.log(cart);
				res.json('That product doesn\'t exist in the cart');
			}
		} else {
			console.log(cart);
			res.json('Error');
		}
	});
});

// subtract quantity to product on cart
router.get('/remove/:idUser/:idProduct/:quantity', function (req, res) {
	var stmt = db.prepare('SELECT * FROM Cart, CartItem WHERE idUser = ? AND Cart.idCart = CartItem.idCart');
	stmt.get(req.params.idUser, (err, cart) => {
		if (cart.idUser == req.params.idUser) {
			if (cart.idProduct == req.params.idProduct) {
				if (cart.quantity - req.params.quantity < 0 ) {
					stmt = db.prepare('DELETE FROM CartItem WHERE idProduct = ? AND idCart = ?');
					stmt.get([cart.idProduct, cart.idCart], (err, result) => {
						console.log(result);
						res.json('Success removing product');
					});
				} else {
					stmt = db.prepare('UPDATE CartItem SET quantity = ? WHERE idProduct = ? AND idCart = ?');
					stmt.get([cart.quantity - req.params.quantity, cart.idProduct, cart.idCart], (err, result) => {
						console.log(result);
						res.json('Success adding another product of the same type');
					});
				}
			} else {
				console.log(cart);
				res.json('That product doesn\'t exist in the cart');
			}
		} else {
			console.log(cart);
			res.json('Error');
		}
	});
});

module.exports = router;