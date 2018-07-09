package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.model.Cinema
import ash.java.nzfforganiser.model.WishlistItem
import ash.java.nzfforganiser.model.Movie
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.stereotype.Service
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

interface NzffDao
{
  fun getWishlist(id: String): List<WishlistItem>

  fun getMovieTimes(wishlistItem: WishlistItem): List<Movie>
}

@Service
class NzffDaoImpl @Autowired constructor(private val scraperClient: ScraperClient,
                                         @Value("\${nzff.wishlist.path}")
                                         private val nzffWishlistPath: String,
                                         @Value("\${nzff.base.url}")
                                         private val nzffBaseUrl: String) : NzffDao
{
  private val logger = LoggerFactory.getLogger(NzffDaoImpl::class.java)

  private val aTag = "a"
  private val imgTag = "img"

  private val contentAttribute = "content"
  private val hrefAttribute = "href"
  private val itempropAttribute = "itemprop"
  private val srcAttribute = "src"

  private val wishlistItemClass = "sessions"
  private val sessionInfoClass = "session-info film-info"

  private val mediaClassSelect = "[class=\"media\"]"
  private val filmDetailSelect = "[class=\"detail\"]"

  @Cacheable("wishlists")
  override fun getWishlist(id: String): List<WishlistItem>
  {
    val doc = scraperClient.getDocument("$nzffBaseUrl$nzffWishlistPath$id")
    val wishlistElements = doc.getElementsByClass(wishlistItemClass)
    val wishlist = mutableListOf<WishlistItem>()

    if (wishlistElements.isEmpty())
    {
      logger.info("Could not find any wishlist elements")
      return wishlist
    }

    for (wishlistElement in wishlistElements)
    {
      val titleElement = wishlistElement.getElementsByClass(sessionInfoClass).first()
      val link = titleElement.getElementsByTag(aTag)

      wishlist.add(WishlistItem(
          title = link.text(),
          websiteUrl = link.attr(hrefAttribute)
      ))
    }

    return wishlist.distinctBy { w -> w.title }
  }

  @Cacheable("sessions", key = "#wishlistItem.title")
  override fun getMovieTimes(wishlistItem: WishlistItem): List<Movie>
  {
    val doc = scraperClient.getDocument("$nzffBaseUrl${wishlistItem.websiteUrl}")
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
      val cinema = Cinema.findValue(locationElement.text())

      movieTimes.add(Movie(
          title = wishlistItem.title,
          websiteUrl = wishlistItem.websiteUrl,
          thumbnailUrl = thumbnailUrl,
          startTime = startDate,
          endTime = startDate.plus(duration),
          cinema = cinema
      ))
    }

    return movieTimes
  }
}