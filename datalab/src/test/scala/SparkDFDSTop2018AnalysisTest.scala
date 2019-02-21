import org.apache.spark.ml.feature.RFormula
import org.apache.spark.ml.regression.LinearRegression
import org.apache.spark.sql.types.DecimalType
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.scalatest.{BeforeAndAfter, FunSuite}

/*
 * https://spark.apache.org/docs/2.1.1/sql-programming-guide.html
 */
class SparkDFDSTop2018AnalysisTest extends FunSuite with BeforeAndAfter with DataLab {

  // data sets
  val top2018CSV = "src/test/resources/top2018.csv"

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

  lazy val songsDF : DataFrame = sparkSession
    .read.options(Map("header" -> "true"))
    .csv(top2018CSV)
    .withColumn("id", 'id)
    .withColumn("name", 'name)
    .withColumn("artists", 'artists)
    .withColumn("danceability", 'danceability.cast(DecimalType(10, 3)))
    .withColumn("energy", 'energy.cast(DecimalType(10, 3)))
    .withColumn("loudness", 'loudness.cast(DecimalType(10, 3)))
    .withColumn("tempo", 'tempo.cast(DecimalType(10, 3)))


  lazy val songsDS: Dataset[Song] = songsDF.as[Song]

//  val lr = new LinearRegression

//  val model = lr.fit(songsDS)

  test("dev") {
    songsDF.show(5)
  }

  test("data quality: record count using DataFrame API") {
    assert(songsDF.select().count() == 100)
  }

  test("data quality: record count using SQL") {
     songsDF.createOrReplaceTempView("SONG")
     assert(sparkSession.sql("SELECT COUNT(1) as TOTAL_COUNT FROM SONG").map(r => r.getLong(0)).first() == 100)
  }

  test("data quality: check some records") {
    songsDS.foreach(println(_))
    val expected = List(
      Song("6DCZcSspjsKoFjzjrWoCd", "God's Plan", "Drake", BigDecimal("0.754"), BigDecimal("0.449"), BigDecimal("-9.211"), BigDecimal("77.169")),
      Song("3ee8Jmje8o58CHK66QrVC", "SAD!", "XXXTENTACION", BigDecimal("0.740"), BigDecimal("0.613"), BigDecimal("-4.88"), BigDecimal("75.023")),
      Song("0e7ipj03S05BNilyu5bRz", "rockstar (feat. 21 Savage)", "Post Malone", BigDecimal("0.587"), BigDecimal("0.535"), BigDecimal("-6.09"), BigDecimal("159.847"))
    )

    expected.foreach { it =>
      assert(songsDS.filter(it == _).first() == it)
    }
  }

  test("Linear Regression the data") {
    val formula = new RFormula()
      .setFormula("energy ~ loudness + tempo")
      .setFeaturesCol("features")
      .setLabelCol("label")

    val trainingData = formula.fit(songsDF).transform(songsDF)

    trainingData.select("features", "label").show()

    val modelTrainer = new LinearRegression()

    val model = modelTrainer.fit(trainingData)

    val testData = formula.fit(songsDF).transform(songsDF)

    model.transform(testData)
      .select("name", "loudness", "tempo", "energy", "prediction")
      .show(10)
  }
}
