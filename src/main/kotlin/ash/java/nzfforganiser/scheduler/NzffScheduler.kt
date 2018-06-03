package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Session
import ash.java.nzfforganiser.model.Suggestion
import org.springframework.stereotype.Service

interface NzffScheduler
{
  fun getSchedule(movieTimes: Map<Movie, List<Session>>) : List<Suggestion>
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  override fun getSchedule(movieTimes: Map<Movie, List<Session>>) : List<Suggestion>
  {
    TODO("not implemented")
  }
}