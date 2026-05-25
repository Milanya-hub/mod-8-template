-- Задача 02: вывести тех производ, которые производят принтеры
SELECT DISTINCT maker
FROM Product
WHERE type = 'Printer'
