import java.io.File

import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.scalatest.{BeforeAndAfter, FunSuite}


/*
 * https://spark.apache.org/docs/2.1.1/sql-programming-guide.html
 */
class SparkDFDSAnalysisTest extends FunSuite with BeforeAndAfter with DataLab {

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

  // Spark Session => for processing data using DataSet and DataFrame API.
  // Used in Spark SQL, Spark Hive, Spark Streaming
  // DataFrame is a type alias of Dataset[Row]
  // DataSets are similiar to RDD's and serialized using special Encoders (more efficient than Java serializations).
  // operations on the RDD are spread around the spark cluster
  lazy val sparkSession = SparkSession
    .builder()
    .appName("DataLab")
    .master("local")
    .getOrCreate()
  import sparkSession.implicits._

  lazy val bankDF : DataFrame = sparkSession
    .read.options(Map("delimiter" -> ";", "header" -> "true"))
    .csv(bankCsv)
    .withColumnRenamed("default", "defaultCredit")
    .withColumnRenamed("y", "yes")
    .withColumn("age", 'age.cast(IntegerType))
    .withColumn("balance", 'balance.cast(IntegerType))
    .withColumn("day", 'day.cast(IntegerType))
    .withColumn("duration", 'duration.cast(IntegerType))
    .withColumn("campaign", 'campaign.cast(IntegerType))
    .withColumn("pdays", 'pdays.cast(IntegerType))
    .withColumn("previous", 'previous.cast(IntegerType))


  lazy val bankDS: Dataset[Bank] = bankDF.as[Bank]

  test("dev") {
    bankDF.show(5)
  }

  test("data quality: record count using DataFrame API") {
    assert(bankDF.select().count() == 4521)
  }

  test("data quality: record count using SQL") {
     bankDF.createOrReplaceTempView("BANK")
     assert(sparkSession.sql("SELECT COUNT(1) as TOTAL_COUNT FROM BANK").map(r => r.getLong(0)).first() == 4521)
  }

  test("data quality: check some records") {
    bankDS.foreach(println(_))
    val expected = List(Bank(30, "unemployed", "married", "primary", "no", 1787, "no", "no", "cellular", 19, "oct", 79, 1, -1, 0, "unknown", "unknown")
      , Bank(33, "services", "married", "secondary", "no", 4789, "yes", "yes", "cellular", 11, "may", 220, 1, 339, 4, "failure", "failure")
      , Bank(35, "management", "single", "tertiary", "no", 1350, "yes", "no", "cellular", 16, "apr", 185, 1, 330, 1, "failure", "failure")
      , Bank(30, "management", "married", "tertiary", "no", 1476, "yes", "yes", "unknown", 3, "jun", 199, 4, -1, 0, "unknown", "unknown")
      , Bank(59, "blue-collar", "married", "secondary", "no", 0, "yes", "no", "unknown", 5, "may", 226, 1, -1, 0, "unknown", "unknown")
    )

    var i = 0;

    expected.foreach { it =>
      assert(bankDS.filter(it == _).first() == it)
    }
  }


}
