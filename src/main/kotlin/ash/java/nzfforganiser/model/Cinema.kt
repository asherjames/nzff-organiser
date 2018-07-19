package ash.java.nzfforganiser.model

enum class Cinema(val displayName: String)
{
  CIVIC("The Civic Theatre"),
  AWT("ASB Waterfront Theatre"),
  RIALTO("Rialto Cinemas Newmarket"),
  RIALTO_4("Rialto Cinemas Newmarket (Cinema 4)"),
  EVENT_QUEEN("Event Cinemas Queen Street"),
  EVENT_WEST("Event Cinemas Westgate"),
  HOLLYWOOD("Hollywood Avondale"),
  ACADEMY("Academy Cinema"),
  WINTER("The Wintergarden at The Civic"),
  UNKNOWN("Unknown cinema");

  companion object
  {
    fun findCinema(search: String): Cinema
    {
      return values().find { it.displayName == search } ?: UNKNOWN
    }
  }
}