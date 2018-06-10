package ash.java.nzfforganiser.model

import java.time.Duration
import java.time.LocalDateTime

data class WishlistItem(val title: String,
                        val websiteUrl: String)

data class Movie(val title: String,
                 val websiteUrl: String,
                 val thumbnailUrl: String,
                 val duration: Duration,
                 val startTime: LocalDateTime)