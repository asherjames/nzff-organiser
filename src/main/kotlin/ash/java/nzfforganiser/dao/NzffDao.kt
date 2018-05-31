package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.model.Movie
import org.springframework.stereotype.Service

interface NzffDao
{
  fun retrieveWishlist(id: String): List<Movie>
}

@Service
class NzffDaoImpl : NzffDao
{
  override fun retrieveWishlist(id: String): List<Movie>
  {
    TODO("not implemented")
  }
}