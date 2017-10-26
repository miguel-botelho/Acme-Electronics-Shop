const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

// send cart info given email of the user
router.post('/:email', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Product WHERE idProduct = ?');
	stmt.get(req.params.id, (err, rows) => {
		if (rows.length > 0) {
			res.send(rows); // id is valid
		} else {
			res.send('Error'); // id doesn't exist
		}
	});
});

// add product to cart

// remove product from cart

// add quantity to product on cart

// subtract quantity to product on cart

module.exports = router;