package ash.java.nzfforganiser

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties
class NzffOrgConfig(
    val nzffPath: String,
    val nzffBase: String,
    val wishlistCacheConfig: CacheConfig,
    val sessionCacheConfig: CacheConfig
)

class CacheConfig(
    val expireSec: Long,
    val maxSize: Long
)