import java.io.File

import org.apache.spark.ml.classification.LogisticRegression
import org.apache.spark.sql.types.IntegerType
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.scalatest.{BeforeAndAfter, FunSuite}
import org.apache.spark.ml.linalg.{Vector, Vectors}

import org.apache.spark.sql.Row
/*
 * https://spark.apache.org/docs/2.1.1/ml-pipeline.html
 * https://en.wikipedia.org/wiki/Linear_predictor_function
 * https://blog.scalac.io/scala-spark-ml.html
 * https://mapr.com/blog/apache-spark-machine-learning-tutorial/
 */
class SparkMLibAnalysisTest extends FunSuite with BeforeAndAfter with DataLab {

  // data sets
  val url = "http://archive.ics.uci.edu/ml/machine-learning-databases/00222/bank.zip"
  val bankZip = "target/bank.zip"
  val dataDir = "target/data"
  val bankCsv = dataDir + "/" + "bank.csv"
  val bankFullCsv = dataDir + "/" + "bank-full.csv"

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

  lazy val bankTrainingDF : DataFrame = sparkSession
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

  lazy val bankFullDF : DataFrame = sparkSession
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

  test("train a model using, transform the full model to contain predictions") {
    val modelTrainer = new LogisticRegression()
    modelTrainer.setLabelCol("yes")
    modelTrainer.setFeaturesCol("age")
    val model = modelTrainer.fit(bankTrainingDF)

    // fails, because my Features needs to be a vector..??
    model.transform(bankFullDF)
//      .select("features", "label", "myProbability", "prediction")
      .select()
      .collect()
      .foreach(println)
//      .foreach { case Row(features: Vector, label: Double, prob: Vector, prediction: Double) =>
//        println(s"($features, $label) -> prob=$prob, prediction=$prediction")
//      }
  }



}