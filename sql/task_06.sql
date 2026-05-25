-- Задача 06: Для каждого производителя, выпускающего ноуты c опред объёмом жесткого диска.
SELECT DISTINCT maker, speed
FROM Product LEFT JOIN Laptop ON Laptop.model = Product.model
WHERE Laptop.hd >= 10
order by maker, speed
