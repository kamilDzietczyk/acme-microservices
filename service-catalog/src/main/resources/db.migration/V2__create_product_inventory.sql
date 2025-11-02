CREATE TABLE IF NOT EXISTS products (
  id UUID PRIMARY KEY,
  sku VARCHAR(64) NOT NULL,
  name VARCHAR(255) NOT NULL,
  description TEXT,
  price_amount NUMERIC(19,2) NOT NULL,
  price_currency CHAR(3) NOT NULL DEFAULT 'PLN',
  CONSTRAINT uk_products_sku UNIQUE (sku)
);

CREATE INDEX IF NOT EXISTS idx_products_sku ON products(sku);

CREATE TABLE IF NOT EXISTS inventory (
  id UUID PRIMARY KEY,
  product_id UUID NOT NULL,
  location_code VARCHAR(64) NOT NULL,
  available_qty INT NOT NULL,
  reserved_qty INT NOT NULL,
  CONSTRAINT fk_inventory_product FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
  CONSTRAINT uk_inventory_product_location UNIQUE (product_id, location_code),
  CONSTRAINT ck_inventory_qty_nonnegative CHECK (available_qty >= 0 AND reserved_qty >= 0)
);

CREATE INDEX IF NOT EXISTS idx_inventory_product ON inventory(product_id);
CREATE INDEX IF NOT EXISTS idx_inventory_location ON inventory(location_code);


