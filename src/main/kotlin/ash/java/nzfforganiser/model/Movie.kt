package ash.java.nzfforganiser.model

import java.time.LocalDateTime

data class Movie(val title: String,
                 val websiteUrl: String,
                 val thumbnailUrl: String,
                 val endTime: LocalDateTime,
                 val startTime: LocalDateTime)