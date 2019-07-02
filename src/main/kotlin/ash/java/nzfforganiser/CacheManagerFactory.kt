package ash.java.nzfforganiser

import com.github.benmanes.caffeine.cache.Caffeine
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.cache.CacheManager
import org.springframework.cache.caffeine.CaffeineCache
import org.springframework.cache.support.SimpleCacheManager
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.util.concurrent.TimeUnit

@Configuration
class CacheManagerFactory @Autowired constructor(private val config: NzffOrgConfig) {
  companion object {
    const val WISHLISTS = "wishlists"
    const val SESSIONS = "sessions"
  }

  @Bean
  fun cacheManager(): CacheManager {
    val wishlistCache = createCache(WISHLISTS, config.wishlistCacheConfig)
    val sessionCache = createCache(SESSIONS, config.sessionCacheConfig)

    val cacheManager = SimpleCacheManager()

    cacheManager.setCaches(listOf(wishlistCache, sessionCache))

    return cacheManager
  }

  private fun createCache(name: String, cacheConfig: NzffOrgConfig.CacheConfig): CaffeineCache {
    return CaffeineCache(name, Caffeine.newBuilder()
        .expireAfterAccess(cacheConfig.expireSec, TimeUnit.SECONDS)
        .maximumSize(cacheConfig.maxSize)
        .build())
  }
}