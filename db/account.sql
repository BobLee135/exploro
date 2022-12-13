SELECT * from user_account;

DROP PROCEDURE IF EXISTS register_user;
DELIMITER ;;
CREATE PROCEDURE register_user(
    a_full_name VARCHAR(40),
    a_email VARCHAR(100),
    a_phone_number VARCHAR(30),
    a_user_password VARCHAR(100)
)
BEGIN
	INSERT INTO user_account (full_name, user_password, email, phone_number)
    VALUES (a_full_name, a_user_password, a_email, a_phone_number);


	
END
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS show_user;
DELIMITER ;;
CREATE PROCEDURE show_user(
    a_id INT
)
BEGIN
	SELECT * FROM user_account WHERE id = a_id;
END
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS change_password;
DELIMITER ;;
CREATE PROCEDURE change_password(
    a_id INT,
    a_password VARCHAR(200)
    
)
BEGIN
	UPDATE user_account
    SET user_password = a_password
    WHERE id = a_id;

END
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS change_email;
DELIMITER ;;
CREATE PROCEDURE change_email(
    a_id INT,
    a_email VARCHAR(100)
    
)
BEGIN
	UPDATE user_account
    SET email = a_email
    WHERE id = a_id;

END
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS show_leaderboard;
DELIMITER ;;
CREATE PROCEDURE show_leaderboard(
)
BEGIN
    SELECT full_name, user_experience FROM user_account ORDER BY user_experience DESC;
        
END
;;
DELIMITER ;

