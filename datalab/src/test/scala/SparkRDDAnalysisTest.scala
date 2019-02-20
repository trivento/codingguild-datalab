import java.io.File

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.apache.spark.{SparkConf, SparkContext}

/*
 * https://spark.apache.org/docs/2.1.1/programming-guide.html
 */
class SparkRDDAnalysisTest extends FunSuite with BeforeAndAfter with DataLab {

  // data sets
  val url = "http://archive.ics.uci.edu/ml/machine-learning-databases/00222/bank.zip"
  val bankZip = "target/bank.zip"
  val dataDir = "target/data"
  val bankCsv = dataDir + "/" + "bank.csv"

  // download
  before {
    if (!new File(bankZip).exists()) {
      download(url, bankZip);
      unzip(bankZip, dataDir)
    }
  }

  // Spark Context => for processing data using the RDD API (Resilient Distributed Data Set).
  // RDD are serialized using Java serialization
  val sparkContext = {
    val conf = new SparkConf().setAppName("DataLab").setMaster("local")
    new SparkContext(conf)
  }
  lazy val bankDataTextRDD: RDD[String] = sparkContext.textFile(bankCsv)
  lazy val bankRDD : RDD[Bank]= bankDataTextRDD.map(s => s.split(";")).filter(s => s(0) != "\"age\"").map(Bank.apply)

  // Spark Session => for processing data using Dataset and DataFrame API.
  // Used in Spark SQL, Spark Hive, Spark Streaming
  // DataFrame is a type alias of Dataset[Row]
  // DataSets are similar to RDD's and serialized using special Encoders (more efficient than Java serializations).
  val sparkSession = SparkSession
    .builder()
    .appName("DataLab")
    .master("local")
    .getOrCreate()
  import sparkSession.implicits._
  lazy val bankDS: Dataset[Bank] = bankRDD.toDS()

  test("dev") {

  }

  test("data quality: record count, headers") {
    assert(bankRDD.count() == 4521)
    assert(bankDS.count() == 4521)

  }

  test("data quality: check some records") {
    val expected = List(Bank(30, "unemployed", "married", "primary", "no", 1787, "no", "no", "cellular", 19, "oct", 79, 1, -1, 0, "unknown", "unknown")
      , Bank(33, "services", "married", "secondary", "no", 4789, "yes", "yes", "cellular", 11, "may", 220, 1, 339, 4, "failure", "failure")
      , Bank(35, "management", "single", "tertiary", "no", 1350, "yes", "no", "cellular", 16, "apr", 185, 1, 330, 1, "failure", "failure")
      , Bank(30, "management", "married", "tertiary", "no", 1476, "yes", "yes", "unknown", 3, "jun", 199, 4, -1, 0, "unknown", "unknown")
      , Bank(59, "blue-collar", "married", "secondary", "no", 0, "yes", "no", "unknown", 5, "may", 226, 1, -1, 0, "unknown", "unknown")
    )

    expected.foreach { it =>
      assert(bankRDD.filter(it == _).first() == it)
    }

    expected.foreach { it =>
      assert(bankDS.filter(it == _).first() == it)
    }

  }

}