    -- 1. Найти номер модели, скорость и размер жесткого диска для всех ПК стоимостью менее 500 долларов.
SELECT model, speed, hd
FROM PC
WHERE price < 500::MONEY;

    -- 2. Найти производителей принтеров. Вывести поля: maker.
SELECT DISTINCT p.maker
FROM Product p
WHERE p.type = 'Printer';

    -- 3. Найти номер модели, объем памяти и размеры экранов ноутбуков, цена которых превышает 1000 долларов.
SELECT model, ram, screen
FROM Laptop
WHERE price > 1000::MONEY;

    -- 4. Найти все записи таблицы Printer для цветных принтеров.
SELECT *
FROM Printer
WHERE color = 'y';

    -- 5. Найти номер модели, скорость и размер жесткого диска для ПК, имеющих скорость cd 12x или 24x и цену менее 600 долларов.
SELECT model, speed, hd
FROM PC
WHERE cd IN ('12x', '24x')
    AND price < 600::MONEY;

    -- 6. Указать производителя и скорость для тех ноутбуков, которые имеют жесткий диск объемом не менее 100 Гбайт.
SELECT p.maker, l.speed
FROM Laptop l
JOIN Product p ON p.model = l.model
WHERE l.hd >= 100;

    -- 7. Найти номера моделей и цены всех продуктов (любого типа), выпущенных производителем B (латинская буква).
SELECT p.model,
       COALESCE(pc.price, l.price, pr.price) AS price
FROM Product p
LEFT JOIN PC pc         ON pc.model = p.model
LEFT JOIN Laptop l      ON l.model  = p.model
LEFT JOIN Printer pr    ON pr.model = p.model
WHERE p.maker = 'B';

    -- 8. Найти производителя, выпускающего ПК, но не ноутбуки.
SELECT maker
FROM Product
WHERE type = 'PC'
EXCEPT
SELECT maker
FROM Product
WHERE type = 'Laptop';

    --9. Найти производителей ПК с процессором не менее 450 Мгц. Вывести поля: maker.
SELECT DISTINCT p.maker
FROM Product p
JOIN PC ON PC.model = p.model
WHERE PC.speed >= 450;

    -- 10. Найти принтеры, имеющие самую высокую цену. Вывести поля: model, price.
SELECT model, price
FROM Printer
WHERE price = (SELECT MAX(price) FROM Printer);

    -- 11. Найти среднюю скорость ПК.
SELECT AVG(speed) AS avg_speed
FROM PC;

    -- 12. Найти среднюю скорость ноутбуков, цена которых превышает 1000 долларов.
SELECT AVG(speed) AS avg_speed
FROM Laptop
WHERE price > 1000::MONEY;

    -- 13. Найти среднюю скорость ПК, выпущенных производителем A.
SELECT AVG(PC.speed) AS avg_speed
FROM PC
JOIN Product p ON p.model = PC.model
WHERE p.maker = 'A';

    -- 14. Для каждого значения скорости процессора найти среднюю стоимость ПК с такой же скоростью. Вывести поля: скорость, средняя цена.
SELECT speed, AVG(price::NUMERIC) AS avg_price
FROM PC
GROUP BY speed
ORDER BY speed;

    -- 15. Найти размеры жестких дисков, совпадающих у двух и более PC. Вывести поля: hd.
SELECT hd
FROM PC
GROUP BY hd
HAVING COUNT(*) >= 2;

    -- 16. Найти пары моделей PC, имеющих одинаковые скорость процессора и RAM. В результате каждая пара указывается только один раз, т.е. (i,j), но не (j,i), Порядок вывода полей: модель с большим номером, модель с меньшим номером, скорость, RAM.
SELECT pc1.model AS model_high,
       pc2.model AS model_low,
       pc1.speed,
       pc1.ram
FROM PC pc1
JOIN PC pc2
  ON pc1.speed = pc2.speed
 AND pc1.ram = pc2.ram
 AND pc1.model > pc2.model
ORDER BY pc1.model, pc2.model;

    -- 17. Найти модели ноутбуков, скорость которых меньше скорости любого из ПК. Вывести поля: type, model, speed.
SELECT 'Laptop' AS type, model, speed
FROM Laptop
WHERE speed < (SELECT MIN(speed) FROM pc);

    -- 18. Найти производителей самых дешевых цветных принтеров. Вывести поля: maker, price.
SELECT p.maker, pr.price
FROM Printer pr
JOIN Product p ON p.model = pr.model
WHERE pr.color = 'y'
    AND pr.price = (SELECT MIN(price) FROM Printer WHERE color = 'y');

    -- 19. Для каждого производителя найти средний размер экрана выпускаемых им ноутбуков. Вывести поля: maker, средний размер экрана.
SELECT p.maker, AVG(l.screen) AS avg_screen
FROM Laptop l
JOIN Product p ON p.model = l.model
GROUP BY p.maker
ORDER BY p.maker;

    -- 20. Найти производителей, выпускающих по меньшей мере три различных модели ПК. Вывести поля: maker, число моделей.
SELECT p.maker, COUNT(DISTINCT p.model) AS pc_models
FROM Product p
WHERE p.type = 'PC'
GROUP BY p.maker
HAVING COUNT(DISTINCT p.model) >= 3;

    -- 21. Найти максимальную цену ПК, выпускаемых каждым производителем. Вывести поля: maker, максимальная цена.
SELECT p.maker, MAX(pc.price) AS max_price
FROM PC
JOIN Product p ON p.model = PC.model
GROUP BY p.maker
ORDER BY p.maker;

    -- 22. Для каждого значения скорости процессора ПК, превышающего 600 МГц, найти среднюю цену ПК с такой же скоростью. Вывести поля: speed, средняя цена.
SELECT speed, AVG(price::NUMERIC) AS avg_price
FROM PC
WHERE speed > 600
GROUP BY speed
ORDER BY speed;

    -- 23. Найти производителей, которые производили бы как ПК, так и ноутбуки со скоростью не менее 750 МГц. Вывести поля: maker
SELECT maker
FROM (
    SELECT p.maker
    FROM PC
    JOIN Product p ON p.model = PC.model
    WHERE PC.speed >= 750
    INTERSECT
    SELECT p.maker
    FROM Laptop l
    JOIN Product p ON p.model = l.model
    WHERE l.speed >= 750
) t
ORDER BY maker;

    -- 24. Перечислить номера моделей любых типов, имеющих самую высокую цену по всей имеющейся в базе данных продукции.
WITH all_prices AS (
    SELECT model, price FROM PC
    UNION ALL
    SELECT model, price FROM Laptop
    UNION ALL
    SELECT model, price FROM Printer
)
SELECT model
FROM all_prices
WHERE price = (SELECT MAX(price) FROM all_prices);

    -- 25. Найти производителей принтеров, которые производят ПК с наименьшим объемом RAM и с самым быстрым процессором среди всех ПК, имеющих наименьший объем RAM. Вывести поля: maker
WITH min_ram AS (
    SELECT MIN(ram) AS ram_min FROM PC
),
fastest_among_min_ram AS (
    SELECT MAX(speed) AS max_speed
    FROM PC
    WHERE ram = (SELECT ram_min FROM min_ram)
),
target_pc_makers AS (
    SELECT DISTINCT p.maker
    FROM PC
    JOIN Product p ON p.model = PC.model
    WHERE PC.ram = (SELECT ram_min FROM min_ram)
      AND PC.speed = (SELECT max_speed FROM fastest_among_min_ram)
),
printer_makers AS (
    SELECT DISTINCT maker
    FROM Product
    WHERE type = 'Printer'
)
SELECT pm.maker
FROM printer_makers pm
JOIN target_pc_makers tm ON tm.maker = pm.maker
ORDER BY pm.maker;