package ash.java.nzfforganiser.dao

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.io.IOException

interface ScraperClient
{
  fun getDocument(url: String): Document
}

@Service
class ScraperClientImpl : ScraperClient
{
  private val logger = LoggerFactory.getLogger(ScraperClientImpl::class.java)

  override fun getDocument(url: String): Document
  {
    try
    {
      return Jsoup.connect(url).get()
    }
    catch (e: IOException)
    {
      logger.error("Error while attempting to retrieve wishlist details from $url", e)
      throw e
    }
  }
}