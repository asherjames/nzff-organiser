package ash.java.nzfforganiser.scheduler

import ash.java.nzfforganiser.model.Movie
import org.springframework.stereotype.Service

interface NzffScheduler
{
  fun getSchedule(allMovieTimes: List<List<Movie>>): Sequence<List<Movie>>
}

@Service
class NzffSchedulerImpl : NzffScheduler
{
  override fun getSchedule(allMovieTimes: List<List<Movie>>): Sequence<List<Movie>>
  {
//    val allMovieTimes = wishlistItemTimes.map { e -> e.value }.toList()
//
//    val p = mutableListOf<WishlistItem>()
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

  private fun suggestionInvalid(schedule: List<Movie?>): Boolean
  {
    return true
  }
}