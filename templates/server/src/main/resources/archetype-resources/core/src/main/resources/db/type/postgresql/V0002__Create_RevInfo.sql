-- *** RevInfo (Commit log for envers audit trail) ***
CREATE TABLE REVINFO(
    id        BIGINT NOT NULL,
    timestamp BIGINT NOT NULL,
    userLogin VARCHAR(255)
);
