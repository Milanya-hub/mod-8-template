-- Задача 04: инфа о цветных принтерах
SELECT code, model, color, type, price
FROM Printer
WHERE color = 'y'
