const express = require('express');
const logger = require('morgan');
const cookieParser = require('cookie-parser');
const bb = require('express-busboy');
const bodyParser = require('body-parser');

var index = require('./routes/index');
var order = require('./routes/order');
var cart = require('./routes/cart');
var product = require('./routes/product');

const app = express();

app.use(logger('dev'));
app.use(bodyParser.json({ limit: '5mb' }));
app.use(bodyParser.urlencoded({ limit: '5mb', extended: true }));
app.use(cookieParser());

bb.extend(app, {
    upload: true,
    allowedPath: /./,
});

app.use('/', index);
app.use('/order', order);
app.use('/cart', cart);
app.use('/product', product);

app.use(function (req, res, next) {
    var err = new Error('Not Found');
    err.status = 404;
    next(err);
});

console.log('ruun');

module.exports = app;