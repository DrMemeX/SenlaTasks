BEGIN;

DROP TABLE IF EXISTS request_orders;
DROP TABLE IF EXISTS order_books;
DROP TABLE IF EXISTS requests;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS books;
DROP TABLE IF EXISTS customers;

CREATE TABLE books (
    id           BIGSERIAL PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    author       VARCHAR(255) NOT NULL,
    price        NUMERIC(10,2) NOT NULL CHECK (price >= 0),
    status       VARCHAR(20) NOT NULL,
    release_date DATE,
    added_date   DATE,
    description  TEXT
);

CREATE TABLE customers (
    id         BIGSERIAL PRIMARY KEY,
    name       VARCHAR(255) NOT NULL,
    phone      VARCHAR(50),
    email      VARCHAR(255),
    address    VARCHAR(255)
);

CREATE TABLE orders (
    id              BIGSERIAL PRIMARY KEY,
    customer_id     BIGINT NOT NULL,
    status          VARCHAR(20) NOT NULL,
    creation_date   DATE NOT NULL,
    completion_date DATE,
    total_price     NUMERIC(10,2) NOT NULL CHECK (total_price >= 0),

        CONSTRAINT fk_orders_customer
        FOREIGN KEY (customer_id)
        REFERENCES customers(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE requests (
    id       BIGSERIAL PRIMARY KEY,
    book_id  BIGINT NOT NULL,
    resolved BOOLEAN NOT NULL DEFAULT FALSE,

    CONSTRAINT fk_requests_book
        FOREIGN KEY (book_id)
        REFERENCES books(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE order_books (
    order_id BIGINT NOT NULL,
    book_id  BIGINT NOT NULL,

    CONSTRAINT pk_order_books PRIMARY KEY (order_id, book_id),

    CONSTRAINT fk_order_books_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_order_books_book
        FOREIGN KEY (book_id)
        REFERENCES books(id)
        ON DELETE RESTRICT
        ON UPDATE CASCADE
);

CREATE TABLE request_orders (
    request_id BIGINT NOT NULL,
    order_id   BIGINT NOT NULL,

    CONSTRAINT pk_request_orders PRIMARY KEY (request_id, order_id),

    CONSTRAINT fk_request_orders_request
        FOREIGN KEY (request_id)
        REFERENCES requests(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE,

    CONSTRAINT fk_request_orders_order
        FOREIGN KEY (order_id)
        REFERENCES orders(id)
        ON DELETE CASCADE
        ON UPDATE CASCADE
);

CREATE INDEX idx_orders_customer_id ON orders(customer_id);

CREATE INDEX idx_requests_book_id ON requests(book_id);

CREATE INDEX idx_order_books_book_id ON order_books(book_id);
CREATE INDEX idx_order_books_order_id ON order_books(order_id);

CREATE INDEX idx_request_orders_order_id ON request_orders(order_id);
CREATE INDEX idx_request_orders_request_id ON request_orders(request_id);

COMMIT;