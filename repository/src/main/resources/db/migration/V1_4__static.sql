CREATE TABLE static_ticket (
    static_ticket_id BIGINT NOT NULL AUTO_INCREMENT,
    n1 INT NOT NULL,
    n2 INT NOT NULL,
    n3 INT NOT NULL,
    n4 INT NOT NULL,
    n5 INT NOT NULL,
    n6 INT NOT NULL,
    lps_id BIGINT UNIQUE,
    PRIMARY KEY(static_ticket_id),
    CONSTRAINT static_fk FOREIGN KEY (lps_id) REFERENCES lotto_plus_statement(lps_id)
);

-- INSERT INTO static_ticket VALUES 
-- (1,2,3,5,6,41,42,1),
-- (2,1,2,3,4,5,6,2),
-- (3,11,12,13,14,15,16,3),
-- (4,11,12,13,14,15,16,4),
-- (5,11,12,13,14,15,16,5),
-- (6,1,3,5,14,18,26,6),
-- (7,1,5,13,14,25,36,7);