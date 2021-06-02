import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.LinkOption

// the path where the project got generated
Path projectPath = Paths.get(request.outputDirectory, request.artifactId)

// the properties available to the archetype
Properties properties = request.properties

String batch = properties.get("batch")

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

if (batch != "batch") {
  // delete folder batch recursively
  Path rootPath = projectPath.resolve("batch")
  deleteDirectoryRecursion(rootPath)
}

void deleteDirectoryRecursion(Path path) {
  if (Files.isDirectory(path, LinkOption.NOFOLLOW_LINKS)) {
    try {
      for (Path entry : Files.newDirectoryStream(path)) {
        deleteDirectoryRecursion(entry);
      }
    } catch(IOException e) {
      e.printStackTrace()
    }
  }
  Files.delete(path);
}