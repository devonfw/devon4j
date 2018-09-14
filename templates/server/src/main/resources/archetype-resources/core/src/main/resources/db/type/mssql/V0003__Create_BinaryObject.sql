-- *** BinaryObject (BLOBs) ***
CREATE TABLE BINARYOBJECT (
  id                  BIGINT NOT NULL IDENTITY(10,1),
  modificationCounter INTEGER NOT NULL,
  content             varbinary(max),
  filesize            BIGINT NOT NULL,
  mimeType            VARCHAR(255),
  PRIMARY KEY (ID)
);
