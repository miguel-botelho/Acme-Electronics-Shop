CREATE TABLE User(
    email TEXT PRIMARY KEY,
    name TEXT NOT NULL,
    password TEXT NOT NULL,
    NIF TEXT NOT NULL,
    address TEXT NOT NULL,
    publicKey TEXT NOT NULL
);

CREATE Table CreditCard(
    idCreditCard INTEGER PRIMARY KEY,
    type TEXT NOT NULL,
    number TEXT NOT NULL,
    validity DATE NOT NULL,
    email TEXT REFERENCES User(email)
);


CREATE TABLE Product(
    idProduct INTEGER PRIMARY KEY,
    maker TEXT NOT NULL,
    model TEXT NOT NULL,
    price INTEGER NOT NULL,
    description TEXT
);

CREATE TABLE Cart(
    idCart INTEGER PRIMARY KEY,
    email TEXT REFERENCES User(email)
);

CREATE TABLE CartItem(
    idCartItem INTEGER PRIMARY KEY,
    quantity INTEGER NOT NULL,
    idProduct INTEGER REFERENCES Product(idProduct),
    idCart INTEGER REFERENCES Cart(idCart)
);

CREATE TABLE Orders(
    idOrder TEXT PRIMARY KEY,
    email TEXT REFERENCES User(email)
);

CREATE TABLE OrderItem(
    idOrderItem INTEGER PRIMARY KEY,
    quantity INTEGER NOT NULL,
    idProduct INTEGER REFERENCES Product(idProduct),
    idOrder TEXT REFERENCES Orders(idOrder)
);