package ash.java.nzfforganiser

import com.github.benmanes.caffeine.cache.Caffeine
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
@EnableConfigurationProperties(NzffOrgConfig::class)
class CacheManagerFactory @Autowired constructor(private val config: NzffOrgConfig)
{
  private val logger = LoggerFactory.getLogger(CacheManagerFactory::class.java)

  companion object
  {
    const val WISHLISTS = "wishlists"
    const val SESSIONS = "sessions"
  }

  @Bean
  fun cacheManager(): CacheManager
  {
    val wishlistCache = createCache(WISHLISTS, config.wishlistCacheConfig)
    val sessionCache = createCache(SESSIONS, config.sessionCacheConfig)

    logger.info("""

      -------Cache config-------
      wishlistCache: ${config.wishlistCacheConfig}
      sessionCache: ${config.sessionCacheConfig}
      --------------------------

    """.trimIndent())

    val cacheManager = SimpleCacheManager()

    cacheManager.setCaches(listOf(wishlistCache, sessionCache))

    return cacheManager
  }

  private fun createCache(name: String, cacheConfig: CacheConfig): CaffeineCache
  {
    return CaffeineCache(name, Caffeine.newBuilder()
        .expireAfterAccess(cacheConfig.expireSec, TimeUnit.SECONDS)
        .maximumSize(cacheConfig.maxSize)
        .build())
  }
}