package ash.java.nzfforganiser

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "nzff")
class NzffOrgConfig {
  lateinit var path: String
  lateinit var baseUrl: String
  val wishlistCacheConfig = CacheConfig()
  var sessionCacheConfig = CacheConfig()

  class CacheConfig {
    var expireSec: Long = 0
    var maxSize: Long = 0
  }
}
