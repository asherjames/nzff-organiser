package ash.java.nzfforganiser.dao

import ash.java.nzfforganiser.FilmInfoScraperClientStub
import ash.java.nzfforganiser.WishlistScraperClientStub
import ash.java.nzfforganiser.model.WishlistItem
import org.assertj.core.api.Assertions.*
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.junit.Test
import org.junit.jupiter.api.TestInstance
import java.io.File
import java.time.LocalDateTime

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NzffDaoTest
{
  private val wishlistNzffDao = NzffDaoImpl(WishlistScraperClientStub(), "", "")
  private val wishlist = wishlistNzffDao.getWishlist("")

  private val filmInfoNzffDao = NzffDaoImpl(FilmInfoScraperClientStub(), "", "")
  private val filmInfo = filmInfoNzffDao.getMovieTimes(WishlistItem(
      title = "The Lobster",
      thumbnailUrl = "/thumbnail.jpg",
      websiteUrl = "/the-lobster"
  ))

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
  fun `wishlist movies have correct titles`()
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

  @Test
  fun `session list is correct length`()
  {
    assertThat(filmInfo.size).isEqualTo(4)
  }

  @Test
  fun `correct values are carried over from wishlist`()
  {
    assertThat(filmInfo)
        .allMatch {
          it.title == "The Lobster"
          && it.thumbnailUrl == "/thumbnail.jpg"
          && it.websiteUrl == "/the-lobster"
        }
  }

  @Test
  fun `correct start times are parsed`()
  {
    assertThat(filmInfo.map { e -> e.startTime })
        .containsOnly(
            LocalDateTime.parse("2015-07-16T19:15:00"),
            LocalDateTime.parse("2015-07-23T15:30:00"),
            LocalDateTime.parse("2015-08-01T20:30:00"),
            LocalDateTime.parse("2015-08-02T19:30:00")
        )
  }

  @Test
  fun `correct end times are calculated`()
  {
    assertThat(filmInfo.map { e -> e.endTime })
        .containsOnly(
            LocalDateTime.parse("2015-07-16T21:13:00"),
            LocalDateTime.parse("2015-07-23T17:28:00"),
            LocalDateTime.parse("2015-08-01T22:28:00"),
            LocalDateTime.parse("2015-08-02T21:28:00")
        )
  }
}