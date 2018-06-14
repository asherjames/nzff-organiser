package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.model.Movie
import org.springframework.stereotype.Service

interface NzffScheduler
{
  fun getSchedule(allMovieTimes: List<List<Movie>>): List<Movie>
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  override fun getSchedule(allMovieTimes: List<List<Movie>>): List<Movie>
  {
    TODO()
  }

  private fun suggestionInvalid(schedule: List<Movie?>): Boolean
  {
    return true
  }
}