import java.io.{BufferedInputStream, File, FileInputStream, FileOutputStream}
import java.net.URL
import java.nio.file.{Files, Path, Paths}
import java.util.zip.ZipInputStream

trait IntegrationTool {

  def download(url: String, outputFilename: String): Unit = {
    createDir(Paths.get(outputFilename).getParent)
    val stream = new BufferedInputStream(new URL(url).openStream())
    val fos = new FileOutputStream(new File(outputFilename));
    val buf = new Array[Byte](4096);
    Stream.continually(stream.read(buf)).takeWhile(_ != -1).foreach {
      fos.write(buf, 0, _)
    };
    fos.close()
  }


  def unzip(zipFile: String, outputFolder: String): Unit = {
    val destination = Paths.get(outputFolder)
    createDir(destination)
    val zis = new ZipInputStream(new FileInputStream(new File(zipFile)));
    Stream.continually(zis.getNextEntry).takeWhile(_ != null).foreach { file =>
      if (!file.isDirectory) {
        val outPath = destination.resolve(file.getName)
        outPath.getParent != null && outPath.getParent.toFile.mkdirs()
        val fos = new FileOutputStream(outPath.toFile)
        val buffer = new Array[Byte](4096)
        Stream.continually(zis.read(buffer)).takeWhile(_ != -1).foreach(fos.write(buffer, 0, _))
        fos.close()
      }
    }
  }
  private def createDir(path: Path): Unit = {
    if (path != null) {
      path.toFile.mkdirs()
    }
  }



}
