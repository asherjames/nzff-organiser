package ash.java.nzfforganiser

import com.github.benmanes.caffeine.cache.Caffeine
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CacheConfig
{
  private val logger = LoggerFactory.getLogger(CacheConfig::class.java)

  @Value("\${nzff.cache.wishlist.expire.seconds}")
  var wishlistCacheExpireSec: Long? = null

  @Value("\${nzff.cache.wishlist.maxsize}")
  var wishlistCacheMaxsize: Long? = null

  @Value("\${nzff.cache.movietimes.maxsize}")
  var movietimesCacheMaxsize: Long? = null

  @Bean
  fun cacheManager(): CacheManager
  {
    val wishlistCache = CaffeineCache("wishlist", Caffeine.newBuilder()
        .expireAfterAccess(wishlistCacheExpireSec!!, TimeUnit.SECONDS)
        .maximumSize(wishlistCacheMaxsize!!)
        .build())

    val movieTimeCache = CaffeineCache("movieTimes", Caffeine.newBuilder()
        .maximumSize(movietimesCacheMaxsize!!)
        .build())

    logger.info("""

      -------Cache config-------
      wishlistCacheExpireSec: $wishlistCacheExpireSec
      wishlistCacheMaxsize: $wishlistCacheMaxsize
      movietimesCacheMaxsize: $movietimesCacheMaxsize
      --------------------------
    """.trimIndent())

    val cacheManager = SimpleCacheManager()

    cacheManager.setCaches(listOf(wishlistCache, movieTimeCache))

    return cacheManager
  }
}