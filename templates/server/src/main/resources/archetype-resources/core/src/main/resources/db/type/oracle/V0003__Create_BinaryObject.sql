-- *** BinaryObject (BLOBs) ***
CREATE TABLE BinaryObject (
  id                  NUMBER(19),
  modificationCounter NUMBER(10, 0) NOT NULL,
  content             BLOB,
  filesize            NUMBER(10, 0) NOT NULL,
  mimeType            VARCHAR(255),
  CONSTRAINT PK_BinaryObject_id PRIMARY KEY (ID)
);
