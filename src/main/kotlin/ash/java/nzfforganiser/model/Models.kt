package ash.java.nzfforganiser.model

import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime

data class Request(val id: String = "", val excludedDays: List<DayOfWeek> = emptyList())

data class Movie(val title: String,
                 val duration: Int,
                 val websiteUrl: String,
                 val thumbnailUrl: String)

data class Session(val movie: Movie, val duration: Duration, val dateTime: LocalDateTime)