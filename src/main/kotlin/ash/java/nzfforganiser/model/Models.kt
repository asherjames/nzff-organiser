package ash.java.nzfforganiser.model

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime

data class Request(val id: String = "", val excludedDays: List<DayOfWeek> = emptyList())

data class WishlistItem(val title: String,
                        val websiteUrl: String)

data class Movie(val title: String,
                 val websiteUrl: String,
                 val duration: Duration,
                 val startTime: LocalDateTime)