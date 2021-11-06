import org.apache.hadoop.conf._
import org.apache.hadoop.fs._
import org.apache.hadoop.io.IOUtils

import java.net.URI

object Application {
  val conf = new Configuration()
  //private val hdfsCoreSitePath = new Path("core-site.xml")
  //private val hdfsHDFSSitePath = new Path("hdfs-site.xml")

  //conf.addResource(hdfsCoreSitePath)
  //conf.addResource(hdfsHDFSSitePath)
  val fileSystem = FileSystem.get(new URI("hdfs://localhost:9000"), conf)

  def main(args: Array[String]): Unit = {

    val inputDir = "stage/"
    val outputDir = "ods/"

    var fsIterator = fileSystem.listStatusIterator(new Path(inputDir))

    while (fsIterator.hasNext()) { // перебор папок date=-//-
      val fs = fsIterator.next()
      print(fs.getPath.getName())
      val outPath = outputDir + fs.getPath.getName
      createFolder(outPath)

      val inputPath = inputDir + fs.getPath.getName
      var fsIteratorFiles = fileSystem.listStatusIterator(new Path(inputPath))
      val fsf = fsIteratorFiles.next();
      var out = fileSystem.create(new Path(outPath + "/" + fsf.getPath.getName()))
      while (fsIteratorFiles.hasNext()) { // перебор файлов part-
        val fsf = fsIteratorFiles.next();
        val in = fileSystem.open(new Path(inputPath + "/" + fsf.getPath.getName()));
        IOUtils.copyBytes(in, out, 8096, false);
        //removeFile(inputPath + "/" + fsf.getPath.getName())
      }
      out.close();
    }
  }

  def createFolder(folderPath: String): Unit = {
    val path = new Path(folderPath)
    if (!fileSystem.exists(path)) {
      fileSystem.mkdirs(path)
    }
  }

  def removeFile(filename: String): Boolean = {
    val path = new Path(filename)
    fileSystem.delete(path, true)
  }
}
