CREATE TABLE User(
    idUser INTEGER PRIMARY KEY,
    email TEXT UNIQUE NOT NULL,
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
    validity TEXT NOT NULL,
    idUser INTEGER REFERENCES User(idUser)
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
    idUser INTEGER REFERENCES User(idUser)
);

CREATE TABLE CartItem(
    idCartItem INTEGER PRIMARY KEY,
    quantity INTEGER NOT NULL,
    idProduct INTEGER REFERENCES Product(idProduct),
    idCart INTEGER REFERENCES Cart(idCart)
);

CREATE TABLE Orders(
    idOrder TEXT PRIMARY KEY,
    day DATE NOT NULL,
    idUser INTEGER REFERENCES User(idUser)
);

CREATE TABLE OrderItem(
    idOrderItem INTEGER PRIMARY KEY,
    quantity INTEGER NOT NULL,
    idProduct INTEGER REFERENCES Product(idProduct),
    idOrder TEXT REFERENCES Orders(idOrder)
);

INSERT INTO User (email, name, password, NIF, address, publicKey) VALUES('jmbotelho95@gmail.com', 'Miguel Botelho', 'teste', '999999990', 'Rua Teste', 'chave');
INSERT INTO Product (maker, model, price, description) VALUES ('teste', 'teste', 99, 'teste');
INSERT INTO Cart (idUser) VALUES (1);
INSERT INTO CartItem (quantity, idProduct, idCart) VALUES (10, 1, 1);


INSERT INTO Orders (idOrder, day, idUser) VALUES (3, '2017-11-10', 2);
INSERT INTO Orders (idOrder, day, idUser) VALUES (4, '2017-11-08', 2);
INSERT INTO OrderItem (quantity, idProduct, idOrder) VALUES (10, 1, 3);
INSERT INTO OrderItem (quantity, idProduct, idOrder) VALUES (10, 1, 4);
INSERT INTO OrderItem (quantity, idProduct, idOrder) VALUES (2, 2, 3);
INSERT INTO Product (maker, model, price, description) VALUES ('teste2', 'teste2', 949, 'teste2');