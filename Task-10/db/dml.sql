TRUNCATE TABLE Printer, Laptop, PC, Product RESTART IDENTITY;

INSERT INTO Product (maker, model, type) VALUES
('A', 'A-PC-100', 'PC'),
('A', 'A-PC-200', 'PC'),
('A', 'A-PC-300', 'PC'),
('A', 'A-LT-1000', 'Laptop'),

('B', 'B-PC-100', 'PC'),
('B', 'B-LT-1100', 'Laptop'),
('B', 'B-PR-100', 'Printer'),
('B', 'B-PR-200', 'Printer'),

('C', 'C-PR-050', 'Printer'),
('C', 'C-LT-050', 'Laptop'),

('D', 'D-PC-100', 'PC'),
('D', 'D-PC-200', 'PC'),

('E', 'E-LT-900', 'Laptop'),
('E', 'E-LT-1200', 'Laptop');


INSERT INTO PC (model, speed, ram, hd, cd, price) VALUES
('A-PC-100', 800,  64,  10.0, '12x', 499),
('A-PC-200', 900, 128,  20.0, '24x', 599),
('A-PC-300', 700, 256,  20.0, '8x',  650),

('B-PC-100', 750,  32,   8.0, '24x', 400),
('D-PC-100', 450,  64,  10.0, '12x', 450),
('D-PC-200', 900, 128,  30.0,  '8x', 700);

INSERT INTO Laptop (model, speed, ram, hd, screen, price) VALUES
('A-LT-1000', 700, 128, 120.0, 15, 1100),
('B-LT-1100', 800, 256,  80.0, 14, 1300),
('C-LT-050',  300,  32,  40.0, 12,  500),
('E-LT-900',  650,  64,  60.0, 13,  900),
('E-LT-1200', 850, 128, 200.0, 17, 1500);

INSERT INTO Printer (model, color, type, price) VALUES
('B-PR-100', 'y', 'Laser', 200),
('B-PR-200', 'y', 'Jet',   200),
('C-PR-050', 'n', 'Matrix',900);
