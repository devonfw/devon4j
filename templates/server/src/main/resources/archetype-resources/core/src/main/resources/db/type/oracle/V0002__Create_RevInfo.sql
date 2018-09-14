-- *** RevInfo (Commit log for envers audit trail) ***
CREATE TABLE RevInfo (
  id            NUMBER(19),
  "timestamp"   NUMBER(19,0),
  userLogin     VARCHAR2(255 CHAR),
  CONSTRAINT PK_RevInfo_id PRIMARY KEY (id)
);