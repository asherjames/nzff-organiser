package ash.java.nzfforganiser.model

data class NzffResponse(val message: String,
                        val movieList: List<Movie> = emptyList())