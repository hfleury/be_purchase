
-- ;; Statement 1: Drop trigger
DROP TRIGGER IF EXISTS set_timestamp_products_update_at ON products;
-- ;;

-- ;; Statement 2: Drop function
DROP FUNCTION IF EXISTS trg_set_timestamp_updated_at();
-- ;;

-- ;; Statement 3: Drop index
DROP INDEX IF EXISTS idx_products_name;
-- ;;

-- ;; Statement 4: Drop table
DROP TABLE IF EXISTS products;
-- ;;

