const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

// -> Register a client index.js
// Validate a client index.js

// Validate a payment order.js // Emit a token order.js // When ready to pay, need to send private key SHA1withRSA signature algorithm. The server verifies it and sends a UUID token (the UUID of the order) order.js
// -> Retrieve information of an order through said token order.js 
// -> Consult past transactions order.js
// -> Printer gives token back to server and server sends customer info, purchase info and total value order.js

// -> Consult cart cart.js
// -> Manage cart cart.js

// -> Consult product info given product ID product.js

router.post('/register', function(req, res) {
	console.log(req.body);
	const email = req.body.email;
	const name = req.body.name;
	const password = req.body.password;
	const NIF = req.body.nif;
	const address = req.body.address;
	const publicKey = req.body.publicKey; // public key of the client
	const typeCreditCard = req.body.type;
	const numberCreditCard = req.body.number;
	const validityCreditCard = req.body.validity;

	var stmt = db.prepare('INSERT INTO User (email, name, password, NIF, address, publicKey) VALUES (?, ?, ?, ?, ?, ?)');
	stmt.get([email, name, password, NIF, address, publicKey], (err, row) => {
		stmt = db.prepare('SELECT * FROM User WHERE email = ?');
		stmt.get(email, (err, user) => {
			console.log(user);
			stmt = db.prepare('INSERT INTO CreditCard (type, number, validity, idUser) VALUES (?, ?, ?, ?');
			stmt.get([typeCreditCard, numberCreditCard, validityCreditCard, user.idUser], (err, row) => {
				// check row response
				console.log(row);
				res.send('User registred successfully');
			});
		});
	});
});

module.exports = router;