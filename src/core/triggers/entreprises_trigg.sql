DROP TRIGGER IF EXISTS entreprises_insert;
CREATE TRIGGER entreprises_insert
    BEFORE INSERT ON entreprises
    FOR EACH ROW
    SET NEW.dateC = NOW(), NEW.dateU = NOW();;

DROP TRIGGER IF EXISTS entreprises_update;
CREATE TRIGGER entreprises_update
    BEFORE UPDATE ON entreprises
    FOR EACH ROW
    SET NEW.dateU = NOW();