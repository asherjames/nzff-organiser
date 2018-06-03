package ash.java.nzfforganiser.model

import java.time.Duration
import java.time.LocalDateTime

data class Movie(val title: String,
                 val startDate: LocalDateTime,
                 val duration: Duration,
                 val websiteUrl: String,
                 val thumbnailUrl: String)

data class Session(val duration: Duration, val dateTime: LocalDateTime)

data class Suggestion(val movie: Movie, val session: Session)