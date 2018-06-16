package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.exception.TooManySchedulesException
import ash.java.nzfforganiser.model.Movie
import com.google.common.collect.Lists
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

interface NzffScheduler
{
  fun getSchedule(allMovieTimes: List<List<Movie>>): MutableIterator<MutableList<Movie>>
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  private val logger = LoggerFactory.getLogger(NzffSchedulerImpl::class.java)

  override fun getSchedule(allMovieTimes: List<List<Movie>>): MutableIterator<MutableList<Movie>>
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

  private fun suggestionInvalid(schedule: List<Movie?>): Boolean
  {
    return true
  }
}