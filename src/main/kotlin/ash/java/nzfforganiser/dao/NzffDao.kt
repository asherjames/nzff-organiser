package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.model.Movie
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service

interface NzffDao
{
  fun retrieveWishlist(id: String): List<Movie>
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
}