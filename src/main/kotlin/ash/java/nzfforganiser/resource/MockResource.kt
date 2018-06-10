package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.model.Movie
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.Duration
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Singleton
import javax.ws.rs.core.Response

@Component
@Singleton
@Profile("test")
class MockResource : NzffResource
{
  override fun getOrganisedWishlist(id: String, from: LocalTime, to: LocalTime, disabledDays: List<DayOfWeek>): Response
  {
    return Response.ok(listOf(
        Movie(
            title = "The Atlantic",
            websiteUrl = "/2018/auckland/the-atlantic/",
            duration = Duration.ofMinutes(76),
            startTime = LocalDateTime.parse("2018-07-18T18:00:00"),
            thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/bi/9a/k3/f9/Atlanten_001%20KEY_web-2000-2000-1125-1125-crop-fill.jpg"
        ),
        Movie(
            title = "Lucky",
            websiteUrl = "/2018/auckland/lucky/",
            duration = Duration.ofMinutes(88),
            startTime = LocalDateTime.parse("2018-07-19T18:00:00"),
            thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/lv/yl/xa/2t/6_Harry%20Dean%20Stanton%20and%20Ron%20Livingston%20in%20LUCKY%2C%20a%20Magnolia%20Pictures%20release.%20Photo%20courtesy%20of%20Magnolia%20Pictures.-2000-2000-1125-1125-crop-fill.jpg"
        ),
        Movie(
            title = "Orlando",
            websiteUrl = "/2018/auckland/orlando/",
            duration = Duration.ofMinutes(94),
            startTime = LocalDateTime.parse("2018-07-20T18:00:00"),
            thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/v1/lh/vz/o2/afd1d26df64939eef7cda51b45159920-2000-2000-1125-1125-crop-fill.jpg"
        )
    )).build()
  }
}