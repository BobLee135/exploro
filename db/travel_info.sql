DROP PROCEDURE IF EXISTS add_travel_to_user;
DELIMITER ;;
CREATE PROCEDURE add_travel_to_user(
    a_id VARCHAR(40),
    a_place VARCHAR(100),
    a_city VARCHAR(100),
    a_country VARCHAR(100)
)
BEGIN
	INSERT INTO user_travel (user_id,place, city, country, visit_date)
    VALUES (a_id,a_place, a_city, a_country, NOW());
    UPDATE user_account
    SET user_experience = user_experience + 10
    WHERE id = a_id;


	
END
;;
DELIMITER ;
CALL add_travel_to_user(1, "Sunkiga Kryddos", "Karlskrona", "Sweden", NOW());
SELECT * FROM user_travel;