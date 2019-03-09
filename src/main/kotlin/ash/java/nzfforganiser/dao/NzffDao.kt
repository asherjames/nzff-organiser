package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.CacheManagerFactory
import ash.java.nzfforganiser.NzffOrgConfig
import ash.java.nzfforganiser.model.Cinema
import ash.java.nzfforganiser.model.WishlistMovie
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Wishlist
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

interface NzffDao
{
  fun getWishlist(id: String): Wishlist

  fun getMovieTimes(wishlistMovie: WishlistMovie): List<Movie>
}

@Service
class NzffDaoImpl @Autowired constructor(private val scraperClient: ScraperClient,
                                         private val config: NzffOrgConfig) : NzffDao
{
  private val logger = LoggerFactory.getLogger(NzffDaoImpl::class.java)

  private val aTag = "a"
  private val headTag = "head"
  private val titleTag = "title"
  private val imgTag = "img"

  private val contentAttribute = "content"
  private val hrefAttribute = "href"
  private val itempropAttribute = "itemprop"
  private val srcAttribute = "src"

  private val wishlistItemClass = "sessions"
  private val sessionInfoClass = "session-info film-info"

  private val mediaClassSelect = "[class=\"media\"]"
  private val filmDetailSelect = "[class=\"detail\"]"

  @Cacheable(CacheManagerFactory.WISHLISTS)
  override fun getWishlist(id: String): Wishlist
  {
    val doc = scraperClient.getDocument("${config.baseUrl}${config.path}$id")
    val headElement = doc.getElementsByTag(headTag).first()
    val titleText = headElement.getElementsByTag(titleTag).text()
    val name = titleText.split(delimiters = *charArrayOf(' ')).first()

    val wishlistElements = doc.getElementsByClass(wishlistItemClass)
    val wishlistMovies = mutableListOf<WishlistMovie>()

    if (wishlistElements.isEmpty())
    {
      logger.info("Could not find any wishlist elements")
      return Wishlist()
    }

    for (wishlistElement in wishlistElements)
    {
      val titleElement = wishlistElement.getElementsByClass(sessionInfoClass).first()
      val link = titleElement.getElementsByTag(aTag)

      wishlistMovies.add(WishlistMovie(
          title = link.text(),
          websiteUrl = link.attr(hrefAttribute)
      ))
    }

    return Wishlist(
        name = name,
        movies = wishlistMovies.distinctBy { w -> w.title }
    )
  }

  @Cacheable(CacheManagerFactory.SESSIONS, key = "#wishlistMovie.title")
  override fun getMovieTimes(wishlistMovie: WishlistMovie): List<Movie>
  {
    val doc = scraperClient.getDocument("${config.baseUrl}${wishlistMovie.websiteUrl}")
    val imageElement = doc.select(mediaClassSelect).first()
    val thumbnailUrl = imageElement.getElementsByTag(imgTag).attr(srcAttribute)
    val detailElements = doc.select(filmDetailSelect)
    val movieTimes = mutableListOf<Movie>()

    if (detailElements.isEmpty())
    {
      logger.info("Could not find any film times")
      return movieTimes
    }

    val durationElement = detailElements.first().getElementsByAttributeValue(itempropAttribute, "duration")
    val duration = try
    {
      Duration.parse(durationElement.attr(contentAttribute))
    }
    catch (e: DateTimeParseException)
    {
      logger.error("Error while parsing movie session times", e)
      Duration.ZERO
    }

    for (session in detailElements)
    {
      val startDateElement = session.getElementsByAttributeValue(itempropAttribute, "startDate")
      val startDate = LocalDateTime.parse(startDateElement.attr(contentAttribute))
      val locationElement = session.getElementsByAttributeValue(itempropAttribute, "location")
      val cinema = Cinema.findCinema(locationElement.text())

      movieTimes.add(Movie(
          title = wishlistMovie.title,
          websiteUrl = wishlistMovie.websiteUrl,
          thumbnailUrl = thumbnailUrl,
          startTime = startDate,
          endTime = startDate.plus(duration),
          cinema = cinema,
          cinemaDisplayName = cinema.displayName
      ))
    }

    return movieTimes
  }
}