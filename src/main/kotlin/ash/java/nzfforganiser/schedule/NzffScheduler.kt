package ash.java.nzfforganiser.schedule

import ash.java.nzfforganiser.exception.NoAcceptableScheduleFoundException
import ash.java.nzfforganiser.exception.TooManySchedulesException
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.ScheduleFilter
import com.google.common.collect.Lists
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalTime

interface NzffScheduler
{
  fun getScheduleIterator(allMovieTimes: List<List<Movie>>): MutableIterator<MutableList<Movie>>

  fun findSchedule(scheduleIterator: MutableIterator<MutableList<Movie>>, filters: List<ScheduleFilter>): List<Movie>
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  private val logger = LoggerFactory.getLogger(NzffSchedulerImpl::class.java)

  override fun getScheduleIterator(allMovieTimes: List<List<Movie>>): MutableIterator<MutableList<Movie>>
  {
    try
    {
      return Lists.cartesianProduct(allMovieTimes).iterator()
    }
    catch (e: IllegalArgumentException)
    {
      logger.error("Cartesian product is too large")
      throw TooManySchedulesException(e)
    }
  }

  override fun findSchedule(scheduleIterator: MutableIterator<MutableList<Movie>>, filters: List<ScheduleFilter>): List<Movie>
  {
    val excludedDays = filters
        .filter { f -> f.excluded }
        .map { f -> f.day }

    logger.info("Excluded days: $excludedDays")

    val excludedPeriods = filters
        .map { it.day to Pair(it.from, it.to) }
        .toMap()

    logger.info("Excluded periods: $excludedPeriods")

    for (schedule in scheduleIterator)
    {
      if (hasExcludedDays(schedule, excludedDays)
          || hasExcludedPeriods(schedule, excludedPeriods)
          || hasClashingSessions(schedule)) continue

      return schedule
    }

    throw NoAcceptableScheduleFoundException()
  }

  internal fun hasExcludedDays(schedule: MutableList<Movie>, excludedDays: List<DayOfWeek>): Boolean
  {
    val violatesDays =  (schedule
        .map { s -> s.startTime.dayOfWeek }
        .any { d -> excludedDays.contains(d) })

    if (violatesDays) logger.info("Schedule contains excluded days, skipping. Schedule details: $schedule")

    return violatesDays
  }

  internal fun hasExcludedPeriods(schedule: MutableList<Movie>, excludedPeriods: Map<DayOfWeek, Pair<LocalTime, LocalTime>>): Boolean
  {
    val violatesPeriods = (schedule.any { s ->
      val period = excludedPeriods[s.startTime.dayOfWeek]
      period?.let {
        return@any !(s.startTime.toLocalTime().plusSeconds(1).isAfter(period.first)
            && s.endTime.toLocalTime().minusSeconds(1).isBefore(period.second))
      }
      false
    })

    if (violatesPeriods) logger.info("Schedule contains excluded periods, skipping. Schedule details: $schedule")

    return violatesPeriods
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
        logger.info("${sortedSchedule[i - 1]} clashes with ${sortedSchedule[i]} in schedule, skipping")
        return true
      }
    }

    return false
  }
}