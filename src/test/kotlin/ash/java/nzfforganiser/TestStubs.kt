package ash.java.nzfforganiser

import ash.java.nzfforganiser.dao.NzffDao
import ash.java.nzfforganiser.dao.ScraperClient
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Wishlist
import ash.java.nzfforganiser.model.WishlistMovie
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.io.File

class WishlistScraperClientStub : ScraperClient {
  override fun getDocument(url: String): Document {
    val wishlistUrl = this.javaClass.classLoader.getResource("wishlist.html")
    return Jsoup.parse(File(wishlistUrl.toURI()), "UTF-8")
  }
}

class WishlistScraperClientStub_Duplicates : ScraperClient {
  override fun getDocument(url: String): Document {
    val wishlistUrl = this.javaClass.classLoader.getResource("wishlist_duplicates.html")
    return Jsoup.parse(File(wishlistUrl.toURI()), "UTF-8")
  }
}

class FilmInfoScraperClientStub : ScraperClient {
  override fun getDocument(url: String): Document {
    val filmInfoUrl = this.javaClass.classLoader.getResource("movieTimes.html")
    return Jsoup.parse(File(filmInfoUrl.toURI()), "UTF-8")
  }
}

class EmptyWishlistNzffDao : NzffDao {
  override fun getWishlist(id: String): Wishlist {
    return Wishlist()
  }

  override fun getMovieTimes(wishlistMovie: WishlistMovie): List<Movie> {
    TODO("not implemented")
  }
}