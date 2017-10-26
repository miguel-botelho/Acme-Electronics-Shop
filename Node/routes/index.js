const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

// encrypt with public ?? index.js
// Register a client (need to generate the keys) index.js
// Validate a client index.js

// Validate a payment order.js // Emit a token order.js // When ready to pay, need to send private key SHA1withRSA signature algorithm. The server verifies it and sends a UUID token (the UUID of the order) order.js
// Retrieve information of an order through said token order.js 
// Consult past transactions order.js
// Printer gives token back to server and server sends customer info, purchase info and total value order.js

// Consult cart cart.js
// Manage cart cart.js

// Need to generate keys on server also (only once)

// -> Consult product info given product ID product.js

router.get('/', function (req, res) {
	console.log('ggghg");');
    res.send('OLA');
});

// does this need to be encrypted in the client side first?
router.post('/register', function(req, res) {
	console.log(req.body);
	const email = req.body.email;
	const name = req.body.name;
	const password = req.body.password;
	const NIF = req.body.password;
	const address = req.body.password;
	const publicKey = req.body.publicKey; // public key of the client
	const typeCreditCard = req.body.type;
	const numberCreditCard = req.body.number;
	const validityCreditCard = req.body.validity;

	const stmt = db.prepare('INSERT INTO CreditCard (type, number, validity, email) VALUES (?, ?, ?, ?');
	stmt.get([typeCreditCard, numberCreditCard, validityCreditCard, email], (err, row) => {
		// check row response
		if (row.isGood) {
			res.send('Registration Successful');
		} else {
			res.send('Error');
		}
	});
});

module.exports = router;