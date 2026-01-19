DROP TABLE IF EXISTS Printer CASCADE;
DROP TABLE IF EXISTS Laptop  CASCADE;
DROP TABLE IF EXISTS PC      CASCADE;
DROP TABLE IF EXISTS Product CASCADE;

CREATE TABLE Product (
    maker  VARCHAR(10) NOT NULL,
    model   VARCHAR(50) NOT NULL,
    type    VARCHAR(50) NOT NULL,
    CONSTRAINT pk_product PRIMARY KEY (model),
    CONSTRAINT chk_product_type CHECK (type IN('PC', 'Laptop', 'Printer'))
    );

CREATE TABLE PC (
    code    INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    model   VARCHAR(50) NOT NULL,
    speed   SMALLINT NOT NULL,
    ram     SMALLINT NOT NULL,
    hd      REAL NOT NULL,
    cd      VARCHAR(10) NOT NULL,
    price   MONEY,
    CONSTRAINT fk_pc_product FOREIGN KEY (model) REFERENCES Product(model)
);

CREATE TABLE Laptop (
    code   INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    model  VARCHAR(50) NOT NULL,
    speed  SMALLINT NOT NULL,
    ram    SMALLINT NOT NULL,
    hd     REAL NOT NULL,
    price  MONEY,
    screen SMALLINT NOT NULL,
    CONSTRAINT fk_laptop_product FOREIGN KEY (model) REFERENCES Product(model)
);

CREATE TABLE Printer (
    code  INTEGER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    model VARCHAR(50) NOT NULL,
    color CHAR(1) NOT NULL,
    type  VARCHAR(10) NOT NULL,
    price MONEY,
    CONSTRAINT fk_printer_product FOREIGN KEY (model) REFERENCES Product(model),
    CONSTRAINT chk_printer_color CHECK (color IN ('y', 'n')),
    CONSTRAINT chk_printer_type  CHECK (type IN ('Laser', 'Jet', 'Matrix'))
);