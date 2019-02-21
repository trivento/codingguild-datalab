
/**
  * http://archive.ics.uci.edu/ml/datasets/Bank+Marketing
  * "age""job""marital""education""default""balance""housing""loan""contact""day""month""duration""campaign""pdays""previous""poutcome""y"
  */
case class Bank(age: Int,
                job: String,
                marital: String,
                education: String,
                defaultCredit: String,
                balance: Int,
                housing: String,
                loan: String,
                contact: String,
                day: Int,
                month: String,
                duration: Int,
                campaign: Int,
                pdays: Int,
                previous: Int,
                poutcome: String,
                yes: String
               )

object Bank  extends StringTool {
  def apply(record: Array[String]): Bank = {
    val r = record.map(trimQuotes(_))
    Bank(
      age = r(0).toInt,
      job = r(1),
      marital = r(2),
      education = r(3),
      defaultCredit = r(4),
      balance = r(5).toInt,
      housing = r(6),
      loan = r(7),
      contact = r(8),
      day = r(9).toInt,
      month = r(10),
      duration = r(11).toInt,
      campaign = r(12).toInt,
      pdays = r(13).toInt,
      previous = r(14).toInt,
      poutcome = r(15),
      yes = r(16))
  }
}
