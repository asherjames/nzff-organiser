package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Session
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service

interface NzffDao
{
  fun retrieveWishlist(id: String): List<Movie>

  fun getMovieTimes(movie: Movie): List<Session>
}

@Service
class NzffDaoImpl @Autowired constructor(private val scraperClient: ScraperClient,
                                         @Value("\${nzff.wishlist.url}")
                                         private val nzffWishlistUrl: String,
                                         @Value("\${nzff.base.url}")
                                         private val nzffBaseUrl: String) : NzffDao
{
  private val logger = LoggerFactory.getLogger(NzffDaoImpl::class.java)

  private val filmInfoClass = "session-info film-info"

  override fun retrieveWishlist(id: String): List<Movie>
  {
    val doc: Document = scraperClient.getDocument("$nzffWishlistUrl$id")
    val filmElements: Elements = doc.getElementsByClass(filmInfoClass)
    val wishlist = mutableListOf<Movie>()

    if (filmElements.isEmpty())
    {
      logger.info("Could not find any elements with class '$filmInfoClass'")
      return wishlist
    }

    for (filmElement in filmElements)
    {
      val link = filmElement.getElementsByTag("a")
      if (link != null)
      {
        wishlist.add(Movie(title = link.text(), websiteUrl = link.attr("href")))
      }
    }

    return wishlist
  }

  @Cacheable("movieTimes", key = "#movie.title")
  override fun getMovieTimes(movie: Movie): List<Session>
  {
    TODO("not implemented")
  }
}