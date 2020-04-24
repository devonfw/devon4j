#if ($dbType == 'mysql')
-- no sequences are used in MySQL instead use AUTO_INCREMENT for every ID
#elseif ($dbType == 'mariadb')
-- no sequences are used in MariaDB instead use AUTO_INCREMENT for every ID
#elseif ($dbType == 'mssql')
-- no sequences are used in MS-SQL Server instead use IDENTITY(«seed»,1) for every ID
#else
-- Leave a large ID space reserved for master-data and test-data
CREATE SEQUENCE HIBERNATE_SEQUENCE START WITH 1000000;
#end

#if ($dbType == 'hana')
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
#end
