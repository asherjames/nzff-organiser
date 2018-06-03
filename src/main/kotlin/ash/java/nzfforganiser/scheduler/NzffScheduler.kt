package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Session

interface NzffScheduler
{
  fun getSchedule(movieTimes: Map<Movie, List<Session>>)
}

class NzffSchedulerImpl : NzffScheduler
{
  override fun getSchedule(movieTimes: Map<Movie, List<Session>>)
  {

  }
}