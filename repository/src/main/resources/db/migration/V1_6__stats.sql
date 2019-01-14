CREATE TABLE stats (
    id INT NOT NULL AUTO_INCREMENT,
    year SMALLINT NOT NULL,
    l0 INT NOT NULL,
    l1 INT NOT NULL,
    l2 INT NOT NULL,
    l3 INT NOT NULL,
    l4 INT NOT NULL,
    l5 INT NOT NULL,
    l6 INT NOT NULL,
    l_all INT NOT NULL,
    l_acc DOUBLE NOT NULL,
    l_bank INT NOT NULL,
    p0 INT NOT NULL,
    p1 INT NOT NULL,
    p2 INT NOT NULL,
    p3 INT NOT NULL,
    p4 INT NOT NULL,
    p5 INT NOT NULL,
    p6 INT NOT NULL,
    p_acc DOUBLE NOT NULL,
    p_bank INT NOT NULL,
    ticket_ordinal INT NOT NULL,
    PRIMARY KEY (id)
);

-- INSERT INTO stats VALUES 
-- (1,'2012',6,5,4,3,2,1,0,21,0.23,5,6,5,4,3,2,1,0,0.52,-25, 0),
-- (2,'2013',6,5,4,3,2,1,0,21,0.43,10,6,5,4,3,2,1,0,0.52,-25, 1),
-- (3,'2014',6,5,4,3,2,1,0,21,0.93,15,6,5,4,3,2,1,0,0.52,-25, 2);


