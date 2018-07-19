package ash.java.nzfforganiser.model

import com.fasterxml.jackson.annotation.JsonIgnore
import java.time.LocalDateTime

data class Movie(val title: String,
                 val websiteUrl: String,
                 val thumbnailUrl: String,
                 val startTime: LocalDateTime,
                 val endTime: LocalDateTime,
                 @JsonIgnore
                 val cinema: Cinema = Cinema.UNKNOWN,
                 val cinemaDisplayName: String = Cinema.UNKNOWN.displayName)