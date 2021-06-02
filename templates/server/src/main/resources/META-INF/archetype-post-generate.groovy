import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// the path where the project got generated
Path projectPath = Paths.get(request.outputDirectory, request.artifactId)

// the properties available to the archetype
Properties properties = request.properties

// dbMigration is either flyway or liquibase
String dbMigration = properties.get("dbMigration")

if (dbMigration == "flyway") {
  // delete the changelog folder as it is not required for flyway
  Files.deleteIfExists projectPath.resolve("src/main/resources/db/changelog/changelog-master.xml")
  Files.deleteIfExists projectPath.resolve("src/main/resources/db/changelog/changelog-v1.0.xml")
  Files.deleteIfExists projectPath.resolve("src/test/resources/db/changelog/changelog-master.xml")
  Files.deleteIfExists projectPath.resolve("src/test/resources/db/changelog/changelog-v1.0.xml")
  
} else if(dbMigration == "liquibase") {
  // delete the changelog folder as it is not required for flyway
   Files.deleteIfExists projectPath.resolve("src/main/resources/db/migration/1.0/V0001_Create_Sequence.sql")
   Files.deleteIfExists projectPath.resolve("src/main/resources/db/test/V0001_InitDb.sql")
}