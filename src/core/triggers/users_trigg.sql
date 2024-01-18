DROP TRIGGER IF EXISTS users_insert;
CREATE TRIGGER users_insert
    BEFORE INSERT ON users
    FOR EACH ROW
    SET NEW.dateC = NOW(), NEW.dateU = NOW();;

DROP TRIGGER IF EXISTS users_update;
CREATE TRIGGER users_update
    BEFORE UPDATE ON users
    FOR EACH ROW
    SET NEW.dateU = NOW();