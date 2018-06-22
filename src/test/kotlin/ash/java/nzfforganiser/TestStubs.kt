package ash.java.nzfforganiser

import ash.java.nzfforganiser.dao.NzffDao
import ash.java.nzfforganiser.dao.ScraperClient
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.WishlistItem
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

class WishlistScraperClientStub : ScraperClient
{
  override fun getDocument(url: String): Document
  {
    val wishlistUrl = this.javaClass.classLoader.getResource("wishlist.html")
    return Jsoup.parse(File(wishlistUrl.toURI()), "UTF-8")
  }
}

class FilmInfoScraperClientStub : ScraperClient
{
  override fun getDocument(url: String): Document
  {
    val filmInfoUrl = this.javaClass.classLoader.getResource("movieTimes.html")
    return Jsoup.parse(File(filmInfoUrl.toURI()), "UTF-8")
  }
}

class EmptyWishlistNzffDao : NzffDao
{
  override fun getWishlist(id: String): List<WishlistItem>
  {
    return emptyList()
  }

  override fun getMovieTimes(wishlistItem: WishlistItem): List<Movie>
  {
    TODO("not implemented")
  }
}