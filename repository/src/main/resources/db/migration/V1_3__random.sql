CREATE TABLE random_ticket (
    random_ticket_id BIGINT NOT NULL AUTO_INCREMENT,
    n1 INT NOT NULL,
    n2 INT NOT NULL,
    n3 INT NOT NULL,
    n4 INT NOT NULL,
    n5 INT NOT NULL,
    n6 INT NOT NULL,
    lps_id BIGINT UNIQUE,
    PRIMARY KEY(random_ticket_id),
    CONSTRAINT random_fk FOREIGN KEY (lps_id) REFERENCES lotto_plus_statement(lps_id)
);

-- INSERT INTO random_ticket VALUES 
-- (1,35,36,37,40,41,42,1),
-- (2,1,2,3,4,5,6,2),
-- (3,11,22,13,14,15,16,3),
-- (4,21,22,23,24,25,26,4),
-- (5,41,42,43,44,45,46,5),
-- (6,1,10,20,30,45,46,6),
-- (7,5,6,10,14,35,46,7);
