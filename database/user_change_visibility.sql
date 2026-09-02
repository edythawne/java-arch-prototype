DELIMITER //

CREATE OR REPLACE FUNCTION user_change_visibility(
    p_user_id BIGINT
)
RETURNS BOOLEAN
DETERMINISTIC
BEGIN
    DECLARE v_exists INT DEFAULT 0;

    -- Validar si el usuario existe
SELECT COUNT(1) INTO v_exists
FROM user
WHERE id = p_user_id;

IF v_exists = 0 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'El ID del usuario no existe en la base de datos';
END IF;

    -- Invertir el estado activo y actualizar la fecha de modificación
UPDATE user
SET is_active = NOT is_active,
    updated_at = NOW()
WHERE id = p_user_id;

RETURN TRUE;
END //

DELIMITER ;


SELECT user_change_visibility(1);
