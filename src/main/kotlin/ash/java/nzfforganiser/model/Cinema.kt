package ash.java.nzfforganiser.model

enum class Cinema
{
  CIVIC, AWT, RIALTO, RIALTO_4, EVENT_QUEEN, EVENT_WEST, HOLLYWOOD, ACADEMY, WINTER, UNKNOWN;

  companion object
  {
    fun findValue(input: String) = when (input)
    {
      "The Civic Theatre" -> CIVIC
      "ASB Waterfront Theatre" -> AWT
      "Rialto Cinemas Newmarket" -> RIALTO
      "Rialto Cinemas Newmarket (Cinema 4)" -> RIALTO_4
      "Event Cinemas Queen Street" -> EVENT_QUEEN
      "Event Cinemas Westgate" -> EVENT_WEST
      "Hollywood Avondale" -> HOLLYWOOD
      "Academy Cinema" -> ACADEMY
      "The Wintergarden at The Civic" -> WINTER
      else -> UNKNOWN
    }
  }
}