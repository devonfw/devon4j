import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths

// the path where the project got generated
File rootDir = new File(request.outputDirectory + "/" + request.artifactId)

// the properties available to the archetype
Properties properties = request.properties

// dbMigration is either liquibase or flyway
String dbMigration = properties.get("dbMigration")


if(dbMigration == "flyway") {
  // delete the changelog folder as it is not required for flyway
  new File(rootDir, "src/main/resources/db/changelog").deleteDir()
}else if(dbMigration == "liquibase") {
  // delete the changelog folder as it is not required for flyway
  new File(rootDir, "src/main/resources/db/migration").deleteDir()
  }