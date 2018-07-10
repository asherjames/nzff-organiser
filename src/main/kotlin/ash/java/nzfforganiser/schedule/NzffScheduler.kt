package ash.java.nzfforganiser.schedule

import ash.java.nzfforganiser.exception.NoAcceptableScheduleFoundException
import ash.java.nzfforganiser.exception.TooManySchedulesException
import ash.java.nzfforganiser.model.*
import com.google.common.collect.Lists
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.LocalTime
import java.time.Month

interface NzffScheduler
{
  fun findSchedule(allMovieTimes: List<List<Movie>>, filters: ScheduleRequest): ScheduleResult
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  private val logger = LoggerFactory.getLogger(NzffSchedulerImpl::class.java)

  override fun findSchedule(allMovieTimes: List<List<Movie>>, filters: ScheduleRequest): ScheduleResult
  {
    try
    {
      val excludedDays = filters.scheduleFilters
          .filter { f -> f.excluded }
          .map { f -> f.day }

      val validPeriods = filters.scheduleFilters
          .filter { f -> !f.excluded }
          .map { it.day to Pair(it.from, it.to) }
          .toMap()

      val filteredTimes = allMovieTimes
          .map { l -> l.filter { m -> isOnValidDay(m, excludedDays) } }
          .map { l -> l.filter { m -> isInValidPeriod(m, validPeriods) } }
          .map { l -> l.filter { m -> isAtValidCinema(m, filters.excludedCinemas) } }
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
        if (hasClashingSessions(schedule, filters.sessionGap)) continue
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

  internal fun isOnValidDay(movie: Movie, excludedDays: List<DayOfWeek>): Boolean
  {
    val onExcludedDay = excludedDays.contains(movie.startTime.dayOfWeek)

    if (onExcludedDay)
    {
      logger.debug("${movie.title} session is on excluded day (${movie.startTime.dayOfWeek}), skipping. Session start time: ${movie.startTime}")
      return false
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

  internal fun isAtValidCinema(movie: Movie, excludedCinemas: List<Cinema>): Boolean
  {
    val atExcludedCinema = excludedCinemas.contains(movie.cinema)

    if (atExcludedCinema)
    {
      logger.debug("${movie.title} session is at excluded cinema (${movie.cinema}), skipping.")
      return false
    }

    return true
  }

  internal fun hasClashingSessions(schedule: MutableList<Movie>, sessionGap: Long): Boolean
  {
    if (schedule.size <= 1) return false

    val sortedSchedule = schedule
        .sortedBy { s -> s.startTime }

    for (i in 1 until sortedSchedule.size)
    {
      // Skip schedules with clashing sessions
      if (sortedSchedule[i - 1].endTime.plusMinutes(sessionGap).isAfter(sortedSchedule[i].startTime))
      {
        logger.debug("Schedule contains clash:\n${sortedSchedule[i - 1]}\n\tclashes with\n${sortedSchedule[i]} in schedule, skipping")
        return true
      }
    }

    return false
  }
}