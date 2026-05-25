-- Задача 10: найти модели принтеров, имеющих самую высокую цену
SELECT model, price
FROM Printer
WHERE price = (SELECT MAX(price) FROM Printer)
