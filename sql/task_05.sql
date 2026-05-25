-- Задача 05: несколько условий на пк
SELECT model, speed, hd
FROM PC
WHERE (cd = '12x' or cd = '24x') and price < 600
