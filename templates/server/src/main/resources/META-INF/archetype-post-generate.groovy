import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.LinkOption

// the path where the project got generated
Path projectPath = Paths.get(request.outputDirectory, request.artifactId)

// the properties available to the archetype
Properties properties = request.properties
String batch = properties.get("batch")

if (batch != "batch") {
  // delete folder batch recursively
  // Path rootPath = projectPath.resolve("batch")
  // deleteDirectoryRecursion(rootPath)
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
