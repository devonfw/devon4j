-- *** RevInfo (Commit log for envers audit trail) ***
CREATE TABLE REVINFO(
    id        BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY,
    timestamp BIGINT NOT NULL,
    userLogin VARCHAR(255)
);
