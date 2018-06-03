package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Session
import org.springframework.stereotype.Service

interface NzffScheduler
{
  fun getSchedule(movieTimes: Map<Movie, List<Session>>) : List<Session>
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  override fun getSchedule(movieTimes: Map<Movie, List<Session>>) : List<Session>
  {
    TODO("not implemented")
  }
}