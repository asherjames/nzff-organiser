package ash.java.nzfforganiser.schedule

import ash.java.nzfforganiser.exception.NoAcceptableScheduleFoundException
import ash.java.nzfforganiser.exception.TooManySchedulesException
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.ScheduleFilter
import com.google.common.collect.Lists
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

interface NzffScheduler
{
  fun findSchedule(allMovieTimes: List<List<Movie>>, filters: List<ScheduleFilter>, jimMode: Boolean): List<Movie>
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  private val logger = LoggerFactory.getLogger(NzffSchedulerImpl::class.java)

  private val jimInvalidDays = listOf<LocalDate>(
      LocalDate.of(2018, Month.JULY, 19),
      LocalDate.of(2018, Month.JULY, 20),
      LocalDate.of(2018, Month.JULY, 21)
  )

  override fun findSchedule(allMovieTimes: List<List<Movie>>, filters: List<ScheduleFilter>, jimMode: Boolean): List<Movie>
  {
    try
    {
      val excludedDays = filters
          .filter { f -> f.excluded }
          .map { f -> f.day }

      val excludedPeriods = filters
          .map { it.day to Pair(it.from, it.to) }
          .toMap()

      val filteredTimes = allMovieTimes
          .map { l -> l.filter { m -> !isOnExcludedDay(m, excludedDays, jimMode) } }
          .map { l -> l.filter { m -> !isInExcludedPeriod(m, excludedPeriods)} }
          .filter { l -> l.isNotEmpty() }

      if (filteredTimes.isEmpty()) throw NoAcceptableScheduleFoundException()

      val schedules = Lists.cartesianProduct(filteredTimes)

      for (schedule in schedules)
      {
        if (hasClashingSessions(schedule)) continue
        return schedule
      }

      throw NoAcceptableScheduleFoundException()
    }
    catch (e: IllegalArgumentException)
    {
      logger.error("Cartesian product is too large")
      throw TooManySchedulesException(e)
    }
  }

  internal fun isOnExcludedDay(movie: Movie, excludedDays: List<DayOfWeek>, jimMode: Boolean): Boolean
  {
    var onDay = excludedDays.contains(movie.startTime.dayOfWeek)

    if (jimMode)
    {
      onDay = jimInvalidDays.contains(movie.startTime.toLocalDate())
    }

    if (onDay) logger.info("${movie.title} session is on excluded day (${movie.startTime.dayOfWeek}), skipping. Session start time: ${movie.startTime}")

    return onDay
  }

  internal fun isInExcludedPeriod(movie: Movie, excludedPeriods: Map<DayOfWeek, Pair<LocalTime, LocalTime>>): Boolean
  {
    val period = excludedPeriods[movie.startTime.dayOfWeek]

    val inPeriod = if (period != null) {
      return !(movie.startTime.toLocalTime().plusSeconds(1).isAfter(period.first)
          && movie.endTime.toLocalTime().minusSeconds(1).isBefore(period.second))
    }
    else
    {
      false
    }

    if (inPeriod) logger.info("${movie.title} session is in excluded period, skipping. Session start time: ${movie.startTime}")

    return inPeriod
  }

  internal fun hasClashingSessions(schedule: MutableList<Movie>): Boolean
  {
    if (schedule.size <= 1) return false

    val sortedSchedule = schedule
        .sortedBy { s -> s.startTime }

    for (i in 1 until sortedSchedule.size)
    {
      // Skip schedules with clashing sessions
      if (sortedSchedule[i - 1].endTime.isAfter(sortedSchedule[i].startTime))
      {
        logger.debug("Schedule contains clash:\n${sortedSchedule[i - 1]}\n\tclashes with\n${sortedSchedule[i]} in schedule, skipping")
        return true
      }
    }

    return false
  }
}