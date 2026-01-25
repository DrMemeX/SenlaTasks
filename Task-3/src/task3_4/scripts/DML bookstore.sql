BEGIN;

TRUNCATE TABLE request_orders, order_books, requests, orders, books, customers RESTART IDENTITY CASCADE;

INSERT INTO books (title, author, price, status, release_date, added_date, description) VALUES
('Чистый код',               'Роберт Мартин',    1499.00,   'AVAILABLE', '2008-08-01', CURRENT_DATE, 'Практики написания читаемого кода.'),
('Паттерны проектирования',  'Банда четырех',    2390.00,   'AVAILABLE', '1994-10-21', CURRENT_DATE, 'Классические паттерны GoF.'),
('Философия Java',           'Брюс Эккель',      1790.00,   'MISSING',   '2018-01-10', CURRENT_DATE, 'Глубокое введение в Java.'),
('Грокаем алгоритмы',        'Адитья Бхаргава',  990.00,    'AVAILABLE', '2016-05-01', CURRENT_DATE, 'Алгоритмы на простом языке.'),
('PostgreSQL. Основы',       'Н. Петров',        1200.00,   'AVAILABLE', '2020-03-15', CURRENT_DATE, 'SQL, индексы, транзакции.'),
('Кафка на пляже',           'Харуки Мураками',  850.00,    'MISSING',   '2002-09-12', CURRENT_DATE, 'Роман.'),
('1984',                     'Джордж Оруэлл',    600.00,    'AVAILABLE', '1949-06-08', CURRENT_DATE, 'Классика антиутопии.'),
('Человек в высоком замке',  'Филип Дик',        780.00,    'AVAILABLE', '1962-01-01', CURRENT_DATE, 'Альтернативная история.');

INSERT INTO customers (name, phone, email, address) VALUES
('Иван Петров',   '+7-900-111-22-33', 'ivan.petrov@mail.ru',    'Москва, ул. Ленина, 10'),
('Анна Смирнова', '+7-900-222-33-44', 'anna.smirnova@mail.ru',  'СПб, Невский пр., 25'),
('Олег Кузнецов', '+7-900-333-44-55', 'oleg.kuz@mail.ru',       'Казань, ул. Баумана, 7'),
('Мария Иванова', '+7-900-444-55-66', 'm.ivanova@mail.ru',      'Екатеринбург, ул. Мира, 18'),
('Денис Орлов',   '+7-900-555-66-77', 'denis.orlov@mail.ru',    'Новосибирск, Красный пр., 3');

INSERT INTO orders (customer_id, status, creation_date, completion_date, total_price) VALUES
(1, 'NEW',       CURRENT_DATE - INTERVAL '2 day',  NULL, 0),
(2, 'COMPLETED', CURRENT_DATE - INTERVAL '10 day', CURRENT_DATE - INTERVAL '7 day', 0),
(3, 'NEW',       CURRENT_DATE - INTERVAL '1 day',  NULL, 0),
(1, 'CANCELLED', CURRENT_DATE - INTERVAL '20 day', CURRENT_DATE - INTERVAL '19 day', 0);

INSERT INTO order_books (order_id, book_id) VALUES
(1, 1),
(1, 5);

INSERT INTO order_books (order_id, book_id) VALUES
(2, 2),
(2, 7),
(2, 4);

INSERT INTO order_books (order_id, book_id) VALUES
(3, 3),
(3, 8);

INSERT INTO order_books (order_id, book_id) VALUES
(4, 6);

UPDATE orders o
SET total_price = x.sum_price
FROM (
    SELECT ob.order_id, SUM(b.price) AS sum_price
    FROM order_books ob
    JOIN books b ON b.id = ob.book_id
    GROUP BY ob.order_id
) x
WHERE o.id = x.order_id;

INSERT INTO requests (book_id, resolved) VALUES
(3, FALSE),
(6, FALSE);

INSERT INTO request_orders (request_id, order_id) VALUES
(1, 3),
(2, 4);

COMMIT;