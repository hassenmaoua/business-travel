DROP TRIGGER IF EXISTS domaines_insert;
CREATE TRIGGER domaines_insert
    BEFORE INSERT ON domaines
    FOR EACH ROW
    SET NEW.dateC = NOW(), NEW.dateU = NOW();

DROP TRIGGER IF EXISTS domaines_update;
CREATE TRIGGER domaines_update
    BEFORE UPDATE ON domaines
    FOR EACH ROW
    SET NEW.dateU = NOW();
