package ash.java.nzfforganiser.model

import java.time.LocalDateTime

data class Movie(val title: String,
                 val websiteUrl: String,
                 val thumbnailUrl: String,
                 val startTime: LocalDateTime,
                 val endTime: LocalDateTime,
                 val cinema: Cinema = Cinema.UNKNOWN)