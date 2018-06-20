package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.exception.NoAcceptableScheduleFoundException
import ash.java.nzfforganiser.exception.TooManySchedulesException
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.ScheduleFilter
import com.google.common.collect.Lists
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

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
      // Skip schedules with excluded days
      if (schedule
              .map { s -> s.startTime.dayOfWeek }
              .any { d -> excludedDays.contains(d) })
      {
        continue
      }

      // Skip schedules with sessions in unacceptable periods
      if (schedule.any { s ->
            val period = excludedPeriods[s.startTime.dayOfWeek]
            s.startTime.toLocalTime().isAfter(period?.first)
                && s.endTime.toLocalTime().isBefore(period?.second)
           })
      {
        continue
      }

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

      return schedule
    }

    throw NoAcceptableScheduleFoundException()
  }
}