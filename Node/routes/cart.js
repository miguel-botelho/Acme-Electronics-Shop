const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
var db = new sqlite3.Database('database/database.db');

// send cart info given id of the user
router.get('/:idUser', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Cart, CartItem, Product WHERE idUser = ? AND Cart.idCart = CartItem.idCart AND Product.idProduct = CartItem.idProduct');
	console.log(req.params.idUser);
	stmt.get(req.params.idUser, (err, rows) => {
		if (rows != undefined && rows != null) {
			res.json(rows); // id is valid
		} else {
			console.log(rows);
			res.json('No Cart'); // id doesn't exist
		}
	});
});

// add product to cart
router.get('/add/:idUser/:idProduct', function (req, res) {
	db.configure('busyTimeout', 5000);
	var stmt = db.prepare('SELECT * FROM Cart WHERE idUser = ?');
	stmt.get(req.params.idUser, (err, cart) => {
		console.log(cart);
		stmt.finalize();
		if (cart == undefined) {
			stmt = db.prepare('INSERT INTO Cart (idUser) VALUES (?)');
			stmt.run(req.params.idUser, (err, r) => {
				stmt.finalize();
				console.log("cart creating" + err);
				stmt = db.prepare('SELECT * FROM Cart WHERE idUser = ?');
				stmt.get(req.params.idUser, (err, teste) => {
					stmt.finalize();
					stmt = db.prepare('INSERT INTO CartItem (quantity, idProduct, idCart) VALUES (?, ?, ?)');
					stmt.get([1, req.params.idProduct, teste.idCart], (err, result) => {
						console.log("cart inserting" + err);
						stmt.finalize();
						res.json('Success adding a different product');
					});
				});
			});
		}
		else {
			if (cart.idUser == req.params.idUser) {
				stmt = db.prepare('SELECT * FROM CartItem WHERE idCart = ? AND idProduct = ?');
				stmt.all([cart.idCart, req.params.idProduct], (err, ro) => {
					stmt.finalize();
					console.log(ro);
					if (ro.length > 0) {
						var stmt2 = db.prepare('UPDATE CartItem SET quantity = ? WHERE idProduct = ? AND idCart = ?');
						stmt2.get([ro[0].quantity + 1, req.params.idProduct, cart.idCart], (err, result) => {
							stmt2.finalize();
							console.log("cart updating" + err);
							res.json('Success adding another product of the same type');
						});
					} else {
						var stmt1 = db.prepare('INSERT INTO CartItem (quantity, idProduct, idCart) VALUES (?, ?, ?)');
						stmt1.get([1, req.params.idProduct, cart.idCart], (err, result) => {
							console.log("cart inserting" + err);
							stmt1.finalize();
							res.json('Success adding a different product');
						});
					}
				});
			} else {
				console.log(cart);
				res.json('Error');
			}
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
	console.log(req.params);
	var stmt = db.prepare('SELECT * FROM Cart, CartItem WHERE idUser = ? AND Cart.idCart = CartItem.idCart');
	stmt.get(req.params.idUser, (err, cart) => {
		if (cart.idUser == req.params.idUser) {
			if (cart.idProduct == req.params.idProduct) {
				stmt = db.prepare('UPDATE CartItem SET quantity = ? WHERE idProduct = ? AND idCart = ?');
				stmt.get([cart.quantity + req.params.quantity, cart.idProduct, cart.idCart], (err, result) => {
					console.log(result);
					console.log(err);
					res.json('Success adding another product of the same type');
				});
			} else {
				console.log(cart);
				console.log("1");
				res.json('That product doesn\'t exist in the cart');
			}
		} else {
			console.log(cart);
			console.log("2");
			res.json('Error');
		}
	});
});

// subtract quantity to product on cart
router.get('/remove/:idUser/:idProduct/:quantity', function (req, res) {
	console.log("HELLO");
	var stmt = db.prepare('SELECT * FROM Cart, CartItem WHERE idUser = ? AND Cart.idCart = CartItem.idCart');
	stmt.get(req.params.idUser, (err, cart) => {
		if (cart.idUser == req.params.idUser) {
			if (cart.idProduct == req.params.idProduct) { 
				if (cart.quantity - req.params.quantity < 0) {
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