-- Задача 08: найти производителя, выпускающего ПК, но не ноутбуки.
SELECT maker
FROM Product
WHERE type = 'PC'

EXCEPT

SELECT maker
FROM Product
WHERE type = 'Laptop'
