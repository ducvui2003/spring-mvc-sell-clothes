DROP PROCEDURE IF EXISTS add_cart;
DELIMITER //

CREATE PROCEDURE add_cart(IN p_cart_id INT,
                          IN p_product_id INT,
                          IN p_size_id INT,
                          IN p_color_id INT,
                          IN p_quantity_add INT)
BEGIN
    IF EXISTS(SELECT *
              FROM cart_items c
              WHERE c.cart_id = p_cart_id
                AND c.product_id = p_product_id
                AND c.size_id = p_size_id
                AND c.color_id = p_color_id) THEN

UPDATE cart_items ct
SET ct.quantity = ct.quantity + p_quantity_add
WHERE ct.cart_id = p_cart_id
  AND ct.product_id = p_product_id
  AND ct.size_id = p_size_id
  AND ct.color_id = p_color_id;
ELSE
        INSERT INTO cart_items (cart_id, product_id, size_id, color_id, quantity)
        VALUES (p_cart_id, p_product_id, p_size_id, p_color_id, p_quantity_add);
END IF;
END //

DELIMITER ;
