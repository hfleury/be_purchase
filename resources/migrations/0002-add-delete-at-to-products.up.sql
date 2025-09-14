ALTER TABLE products
ADD COLUMN IF NOT EXISTS deleted_at TIMESTAMP WITH TIME ZONE DEFAULT NULL;
--;;
COMMENT ON COLUMN products.deleted_at IS 'Timestamp when the product was virtually deleted. NULL means active.';
