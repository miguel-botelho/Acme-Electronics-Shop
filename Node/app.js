const express = require('express');
const path = require('path');
const logger = require('morgan');
const cookieParser = require('cookie-parser');
const bb = require('express-busboy');
const bodyParser = require('body-parser');
const session = require('express-session');
const sqlite3 = require('sqlite3').verbose();
const db = new sqlite3.Database('database/database.db');

var index = require('./routes/index');

const app = express();

app.use(logger('dev'));
app.use(bodyParser.json({ limit: '5mb' }));
app.use(bodyParser.urlencoded({ limit: '5mb', extended: true }));
app.use(cookieParser());

app.use(session({
    key: 'el_gato_de_nyan',
    secret: 'nyan_cat'
}));

app.use(session({ secret: 'nyan cat' }));
bb.extend(app, {
    upload: true,
    allowedPath: /./,
});

app.use('/', index);

module.exports = app;