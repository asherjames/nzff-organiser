package ash.java.nzfforganiser.schedule

import ash.java.nzfforganiser.exception.NoAcceptableScheduleFoundException
import ash.java.nzfforganiser.exception.TooManySchedulesException
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.ScheduleFilter
import ash.java.nzfforganiser.model.ScheduleResult
import com.google.common.collect.Lists
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

interface NzffScheduler
{
  fun findSchedule(allMovieTimes: List<List<Movie>>, filters: List<ScheduleFilter>, jimMode: Boolean): ScheduleResult
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

  override fun findSchedule(allMovieTimes: List<List<Movie>>, filters: List<ScheduleFilter>, jimMode: Boolean): ScheduleResult
  {
    try
    {
      val excludedDays = filters
          .filter { f -> f.excluded }
          .map { f -> f.day }

      val validPeriods = filters
          .filter { f -> !f.excluded }
          .map { it.day to Pair(it.from, it.to) }
          .toMap()

      val filteredTimes = allMovieTimes
          .map { l -> l.filter { m -> isOnValidDay(m, excludedDays, jimMode) } }
          .map { l -> l.filter { m -> isInValidPeriod(m, validPeriods) } }
          .filter { l -> l.isNotEmpty() }

      val unavailableMovies = allMovieTimes
          .flatMap { l -> l.map { m -> m.title } }
          .distinct()
          .minus(
              filteredTimes.map { l -> l.firstOrNull()?.title ?: ""}
          )

      if (filteredTimes.isEmpty()) throw NoAcceptableScheduleFoundException()

      val schedules = Lists.cartesianProduct(filteredTimes)

      for (schedule in schedules)
      {
        if (hasClashingSessions(schedule)) continue
        return ScheduleResult(schedule, unavailableMovies)
      }

      throw NoAcceptableScheduleFoundException()
    }
    catch (e: IllegalArgumentException)
    {
      logger.error("Cartesian product is too large")
      throw TooManySchedulesException(e)
    }
  }

  internal fun isOnValidDay(movie: Movie, excludedDays: List<DayOfWeek>, jimMode: Boolean): Boolean
  {
    val onExcludedDay = excludedDays.contains(movie.startTime.dayOfWeek)

    if (onExcludedDay)
    {
      logger.debug("${movie.title} session is on excluded day (${movie.startTime.dayOfWeek}), skipping. Session start time: ${movie.startTime}")
      return false
    }

    if (jimMode)
    {
      return !jimInvalidDays.contains(movie.startTime.toLocalDate())
    }

    return true
  }

  internal fun isInValidPeriod(movie: Movie, validPeriods: Map<DayOfWeek, Pair<LocalTime, LocalTime>>): Boolean
  {
    val period = validPeriods[movie.startTime.dayOfWeek]

    val inValidPeriod = if (period != null)
    {
      (movie.startTime.toLocalTime().plusSeconds(1).isAfter(period.first)
          && movie.endTime.toLocalTime().minusSeconds(1).isBefore(period.second))
    }
    else
    {
      true
    }

    if (!inValidPeriod) logger.debug("${movie.title} session is in excluded period, skipping. Session start time: ${movie.startTime}")

    return inValidPeriod
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