DROP PROCEDURE IF EXISTS add_friend;
DELIMITER ;;
CREATE PROCEDURE add_friend(
    a_user_id INT,
    a_friend_id INT
)
BEGIN
	INSERT INTO user_friends (user_id, friend_id, friends_since)
    VALUES (a_user_id, a_friend_id, NOW());
	INSERT INTO user_friends (user_id, friend_id, friends_since)
    VALUES (a_friend_id, a_user_id, NOW());


	
END
;;
DELIMITER ;



DROP PROCEDURE IF EXISTS show_friends;
DELIMITER ;;
CREATE PROCEDURE show_friends(
    a_user_id INT
)
BEGIN
    SELECT
		ua.full_name,
        ua.phone_number,
        ue.friends_since
        FROM user_account AS ua
        LEFT JOIN user_friends AS ue
        ON ua.id = ue.friend_id
        WHERE ue.user_id = a_user_id;
        
END
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS delete_friend;
DELIMITER ;;
CREATE PROCEDURE delete_friend(
    a_user_id INT,
    a_friend_id INT
)
BEGIN
    DELETE FROM user_friends WHERE a_user_id = user_id AND a_friend_id = friend_id;
    DELETE FROM user_friends WHERE a_friend_id = user_id AND a_user_id = friend_id;
        
END
;;
DELIMITER ;


DROP PROCEDURE IF EXISTS show_friends_leaderboard;
DELIMITER ;;
CREATE PROCEDURE show_friends_leaderboard(
    a_user_id INT
)
BEGIN
    SELECT
		ua.full_name,
        ua.user_experience
        FROM user_account AS ua
        LEFT JOIN user_friends AS ue
        ON ua.id = ue.friend_id
        WHERE ue.user_id = a_user_id
        ORDER BY ua.user_experience DESC;
        
END
;;
DELIMITER ;






