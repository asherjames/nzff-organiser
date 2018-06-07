package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Session
import org.springframework.stereotype.Service

interface NzffScheduler
{
  fun getSchedule(movieTimes: Map<Movie, List<Session>>): Sequence<List<Session>>
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  override fun getSchedule(movieTimes: Map<Movie, List<Session>>): Sequence<List<Session>>
  {
//    val allMovieTimes = movieTimes.map { e -> e.value }.toList()
//
//    val p = mutableListOf<Movie>()
//
//    val max = allMovieTimes.size - 1
//
//    val lens = mutableListOf<Int>()
//
//    for (n in allMovieTimes.size..0)
//    {
//      lens[n] = allMovieTimes[n].size
//    }
//
//    fun dive(i: Int)
//    {
//      var a = allMovieTimes[i]
//      var len = lens[i]
//
//      if (i == max)
//      {
//        for (n in 0 until len)
//        {
//          p[i] = a[n]
//        }
//      }
//    }

    TODO()
  }

  private fun suggestionInvalid(schedule: List<Session?>): Boolean
  {
    return true
  }
}