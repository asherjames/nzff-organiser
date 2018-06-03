package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Session
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface NzffDao
{
  fun retrieveWishlist(id: String): List<Movie>

  fun getMovieTimes(movie: Movie): List<Session>
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

  override fun getMovieTimes(movie: Movie): List<Session>
  {
    TODO("not implemented")
  }
}