case class Song (
  id: String,
  name: String,
  artists: String,
  danceability: BigDecimal,
  energy: BigDecimal,
//  key: BigDecimal,
  loudness: BigDecimal,
//  mode: BigDecimal,
//  speechiness: BigDecimal,
//  acousticness: BigDecimal,
//  instrumentalness: BigDecimal,
//  liveness: BigDecimal,
//  valence: BigDecimal,
  tempo: BigDecimal,
//  duration_ms: BigDecimal,
//  time_signature: BigDecimal
)

object Song extends StringTool {
  def apply(record: Array[String]): Song = {
    val r = record.map(trimQuotes(_))
    Song(
      id = r(0),
      name = r(1),
      artists = r(2),
      danceability = BigDecimal(r(3)),
      energy = BigDecimal(r(4)),
//      key = BigDecimal(r(5)),
      loudness = BigDecimal(r(6)),
//      mode = BigDecimal(r(7)),
//      speechiness = BigDecimal(r(8)),
//      acousticness = BigDecimal(r(9)),
//      instrumentalness = BigDecimal(r(10)),
//      liveness = BigDecimal(r(11)),
//      valence = BigDecimal(r(12)),
      tempo = BigDecimal(r(13))
//      duration_ms = BigDecimal(r(14)),
//      time_signature = BigDecimal(r(15))
    )
  }
}
