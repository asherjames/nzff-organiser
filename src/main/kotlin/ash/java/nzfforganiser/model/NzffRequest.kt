package ash.java.nzfforganiser.model

import java.time.DayOfWeek
import java.time.LocalTime

data class ScheduleRequest(val excludedCinemas: List<Cinema>,
                           val scheduleFilters: List<ScheduleFilter>)

data class ScheduleFilter(val day: DayOfWeek,
                          val from: LocalTime = LocalTime.MIN,
                          val to: LocalTime = LocalTime.MAX,
                          val excluded: Boolean = false)