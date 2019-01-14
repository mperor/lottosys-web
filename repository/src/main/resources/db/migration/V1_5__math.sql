CREATE TABLE math_ticket (
    math_ticket_id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    n1 INT NOT NULL,
    n2 INT NOT NULL,
    n3 INT NOT NULL,
    n4 INT NOT NULL,
    n5 INT NOT NULL,
    n6 INT NOT NULL,
    lps_id BIGINT UNIQUE,
    PRIMARY KEY(math_ticket_id),
    CONSTRAINT math_fk FOREIGN KEY (lps_id) REFERENCES lotto_plus_statement(lps_id)
);

-- INSERT INTO math_ticket VALUES 
-- (1,35,36,37,40,41,42,1),
-- (2,1,2,3,4,5,6,2),
-- (3,13,22,33,34,35,46,3),
-- (4,1,2,13,14,15,36,4),
-- (5,1,12,23,24,25,33,5),
-- (6,1,3,5,24,28,36,6),
-- (7,1,5,23,24,35,46,7);
