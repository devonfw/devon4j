-- *** BinaryObject (BLOBs) ***
CREATE TABLE BinaryObject (
  id                  BIGINT NOT NULL AUTO_INCREMENT,
  modificationCounter INTEGER NOT NULL,
  content             BLOB(2147483647),
  filesize            BIGINT NOT NULL,
  mimeType            VARCHAR(255),
  PRIMARY KEY (ID)
);
