trait StringTool {

  // helpers
  def trimQuotes(s: String): String = {
    s.replaceAll("^\"", "").replaceAll("\"$", "")
  }
}

