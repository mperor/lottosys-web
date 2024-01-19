CREATE TABLE lotto_plus_statement (
    lps_id BIGINT NOT NULL GENERATED BY DEFAULT AS IDENTITY,
    date DATE NOT NULL,
    l1 INT NOT NULL,
    l2 INT NOT NULL,
    l3 INT NOT NULL,
    l4 INT NOT NULL,
    l5 INT NOT NULL,
    l6 INT NOT NULL,
    p1 INT NOT NULL,
    p2 INT NOT NULL,
    p3 INT NOT NULL,
    p4 INT NOT NULL,
    p5 INT NOT NULL,
    p6 INT NOT NULL,
    PRIMARY KEY (lps_id)
);