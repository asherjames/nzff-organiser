package ash.java.nzfforganiser.model

import java.time.LocalDateTime

data class WishlistItem(val title: String,
                        val websiteUrl: String,
                        val thumbnailUrl: String)

data class Movie(val title: String,
                 val websiteUrl: String,
                 val thumbnailUrl: String,
                 val endTime: LocalDateTime,
                 val startTime: LocalDateTime)

data class NzffResponse(val message: String,
                        val movieList: List<Movie> = emptyList())