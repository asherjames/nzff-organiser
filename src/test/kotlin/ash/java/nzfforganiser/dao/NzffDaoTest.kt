package ash.java.nzfforganiser.dao

import org.assertj.core.api.Assertions.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.Test
import java.io.File

class NzffDaoTest
{
  private val nzffDao = NzffDaoImpl(ScraperClientStub(), "", "")
  private val wishlist = nzffDao.getWishlist("")

  @Test
  fun `document loads successfully`()
  {
    assertThat(wishlist).isNotEmpty
  }

  @Test
  fun `wishlist is correct length`()
  {
    assertThat(wishlist.size).isEqualTo(14)
  }

  @Test
  fun `movies have correct titles`()
  {
    assertThat(wishlist.map { m -> m.title })
        .containsExactly(
            "The Lobster",
            "Welcome to Leith",
            "Results",
            "Dreamcatcher",
            "A Pigeon Sat on a Branch Reflecting on Existence",
            "Best of Enemies",
            "Very Semi-Serious",
            "Tangerine",
            "Peace Officer",
            "Iris",
            "Inherent Vice",
            "Steve McQueen: The Man & Le Mans",
            "Democrats",
            "Finders Keepers"
        )
  }

  @Test
  fun `movies have correct hrefs`()
  {
    assertThat(wishlist.map { m -> m.websiteUrl })
        .containsExactly(
            "https://www.nziff.co.nz/2015/auckland/the-lobster/",
            "https://www.nziff.co.nz/2015/auckland/welcome-to-leith/",
            "https://www.nziff.co.nz/2015/auckland/results/",
            "https://www.nziff.co.nz/2015/auckland/dreamcatcher/",
            "https://www.nziff.co.nz/2015/auckland/a-pigeon-sat-on-a-branch-reflecting-on-existence/",
            "https://www.nziff.co.nz/2015/auckland/best-of-enemies/",
            "https://www.nziff.co.nz/2015/auckland/very-semi-serious/",
            "https://www.nziff.co.nz/2015/auckland/tangerine/",
            "https://www.nziff.co.nz/2015/auckland/peace-officer/",
            "https://www.nziff.co.nz/2015/auckland/iris/",
            "https://www.nziff.co.nz/2015/auckland/inherent-vice/",
            "https://www.nziff.co.nz/2015/auckland/steve-mcqueen-the-man-le-mans/",
            "https://www.nziff.co.nz/2015/auckland/democrats/",
            "https://www.nziff.co.nz/2015/auckland/finders-keepers/"
        )
  }
}

class ScraperClientStub : ScraperClient
{
  override fun getDocument(url: String): Document
  {
    val wishlistUrl = this.javaClass.classLoader.getResource("wishlist.html")
    return Jsoup.parse(File(wishlistUrl.toURI()), "UTF-8")
  }
}