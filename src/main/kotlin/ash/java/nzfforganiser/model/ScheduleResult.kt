package ash.java.nzfforganiser.model

data class ScheduleResult(
  val scheduleSuggestion: List<Movie>,
  val unavailableMovies: List<String>
)