const express = require('express');
const router = express.Router();
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

// receive payment order and verify the signature and later emit a token
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

router.get('/', function (req, res) {
	console.log('ggghg");');
    res.send('OLA');
});

// retrieve order information given UUID
router.get('/:uuid', function (req, res) {

});

// retrieve order information and client information
router.get('/printer/:uuid', function (req, res) {

});

// retrieve all orders of a given customer


module.exports = router;