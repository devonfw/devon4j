-- *** RevInfo (Commit log for envers audit trail) ***
CREATE TABLE REVINFO(
  id        BIGINT NOT NULL IDENTITY(1,1),
  timestamp BIGINT NOT NULL,
  userLogin VARCHAR(255)
);
