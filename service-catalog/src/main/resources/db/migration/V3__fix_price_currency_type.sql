ALTER TABLE products
  ALTER COLUMN price_currency DROP DEFAULT,
  ALTER COLUMN price_currency TYPE VARCHAR(3) USING TRIM(price_currency),
  ALTER COLUMN price_currency SET DEFAULT 'PLN';