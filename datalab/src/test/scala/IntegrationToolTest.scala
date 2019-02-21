import java.io.File

import org.scalatest.{BeforeAndAfter, FunSuite}

class IntegrationToolTest extends FunSuite with IntegrationTool with BeforeAndAfter {

  before {
  }


  test("bank.zip should contain three files") {
    download(ClassLoader.getSystemResource("bank.zip").toString, outputFilename = "target/data/bank.zip")
    unzip(zipFile = "target/data/bank.zip", outputFolder = "target/data")

    val dir = "target/data"
    List("bank.csv", "bank-full.csv", "bank-names.txt").foreach {
      f => assert(new File(dir + "/" + f).exists());
    }

  }


}