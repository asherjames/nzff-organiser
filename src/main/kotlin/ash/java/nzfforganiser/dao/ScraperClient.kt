package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.exception.ScraperException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException

interface ScraperClient {
  fun getDocument(url: String): Document
}

@Service
class ScraperClientImpl : ScraperClient {
  private val logger = LoggerFactory.getLogger(ScraperClientImpl::class.java)

  override fun getDocument(url: String): Document {
    try {
      logger.info("Retrieving HTML document from $url")
      return Jsoup.connect(url).get()
    } catch (e: IOException) {
      logger.error("Error while attempting to retrieve HTML document from $url", e)
      throw ScraperException("Unable to connect to NZFF site")
    }
  }
}