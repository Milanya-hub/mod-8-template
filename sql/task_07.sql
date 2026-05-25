-- Задача 07: тут нас интересовала продукция производителя В
SELECT PC.model, PC.price
FROM Product JOIN PC ON (Product.maker = 'B' and PC.model = Product.model)

UNION

SELECT Laptop.model, Laptop.price
FROM Product JOIN Laptop ON (Product.maker = 'B' and Laptop.model = Product.model)

UNION

SELECT Printer.model, Printer.price
FROM Product JOIN Printer ON (Product.maker = 'B' and Printer.model = Product.model)
