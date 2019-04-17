package ash.java.nzfforganiser.model

data class NzffResponse(
  val message: String,
  val name: String = "",
  val movieList: List<Movie> = emptyList(),
  val invalidMovies: List<String> = emptyList())