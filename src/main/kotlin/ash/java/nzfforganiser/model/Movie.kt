package ash.java.nzfforganiser.model

import java.time.Duration
import java.time.LocalDateTime

data class Movie(val title: String,
                 val startDate: LocalDateTime,
                 val duration: Duration,
                 val websiteUrl: String,
                 val thumbnailUrl: String)
