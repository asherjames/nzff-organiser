package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.MovieTime

interface NzffScheduler
{
  fun getSchedule(movieTimes: Map<Movie, List<MovieTime>>)
}

class NzffSchedulerImpl : NzffScheduler
{
  override fun getSchedule(movieTimes: Map<Movie, List<MovieTime>>)
  {

  }
}