-- Leave a large ID space reserved for master-data and test-data
CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1000000;

-- hana does not support Dateadd function out of the box so we add it here to be able to use it for master-data SQLs
CREATE FUNCTION DATEADD(IN DATETYPE NVARCHAR(256), IN NUMBER INTEGER, IN TS TIMESTAMP)
RETURNS TSADD TIMESTAMP
AS
BEGIN
  IF :DATETYPE = 'DAY'
  THEN
    TSADD = ADD_DAYS(:TS, :NUMBER);
  ELSEIF :DATETYPE = 'HOUR'
  THEN
    TSADD = ADD_SECONDS(:TS, :NUMBER * 3600);
  ELSE
    SIGNAL SQL_ERROR_CODE 10000 SET MESSAGE_TEXT = 'Unsupported date type: ' || :DATETYPE;
  END IF;
END;