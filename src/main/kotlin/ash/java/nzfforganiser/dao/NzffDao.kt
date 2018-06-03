package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.MovieTime
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface NzffDao
{
  fun retrieveWishlist(id: String): List<Movie>

  fun getMovieTimes(movie: Movie): List<MovieTime>
}

@Service
class NzffDaoImpl : NzffDao
{
  @Value("\${nzff.baseUrl}")
  private lateinit var nzffBaseUrl: String

  override fun retrieveWishlist(id: String): List<Movie>
  {
    TODO("not implemented")
  }

  override fun getMovieTimes(movie: Movie): List<MovieTime>
  {
    TODO("not implemented")
  }
}