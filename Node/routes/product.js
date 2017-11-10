const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

// send product info with id given
router.get('/:id', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Product WHERE idProduct = ?');
	stmt.get(req.params.id, (err, rows) => {
		console.log(rows);
		stmt.finalize();
		if (rows != undefined) {
			res.json(rows); // id is valid
		} else {
			res.json('Error'); // id doesn't exist
		}
	});
});

// send all products in db
router.get('/', function (req, res) {
	const stmt = db.prepare('SELECT * FROM Product');
	stmt.get((err, rows) => {
		stmt.finalize();
		res.json(rows);
	});
});

module.exports = router;