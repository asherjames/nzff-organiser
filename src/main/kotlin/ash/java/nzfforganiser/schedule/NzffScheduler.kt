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

    val excludedPeriods = filters
        .map { it.day to Pair(it.from, it.to) }
        .toMap()

    for (schedule in scheduleIterator)
    {
      if (hasExcludedDays(schedule, excludedDays) ||
          hasExcludedPeriods(schedule, excludedPeriods)) continue

      if (schedule.size > 1)
      {
        val sortedSchedule = schedule
            .sortedBy { s -> s.startTime }

        for (i in 1..sortedSchedule.size)
        {
          // Skip schedules with clashing sessions
          if (sortedSchedule[i - 1].endTime.isAfter(sortedSchedule[i].startTime))
          {
            continue
          }
        }
      }

      return schedule
    }

    throw NoAcceptableScheduleFoundException()
  }

  internal fun hasExcludedDays(schedule: MutableList<Movie>, excludedDays: List<DayOfWeek>): Boolean
  {
    return (schedule
        .map { s -> s.startTime.dayOfWeek }
        .any { d -> excludedDays.contains(d) })
  }

  internal fun hasExcludedPeriods(schedule: MutableList<Movie>, excludedPeriods: Map<DayOfWeek, Pair<LocalTime, LocalTime>>): Boolean
  {
    return (schedule.any { s ->
      val period = excludedPeriods[s.startTime.dayOfWeek]
      period?.let {
        return@any s.startTime.toLocalTime().isAfter(period.first)
            && s.endTime.toLocalTime().isBefore(period.second)
      }
      false
    })
  }
}