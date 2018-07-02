package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.NzffResponse
import ash.java.nzfforganiser.model.ScheduleFilter
import org.springframework.context.annotation.Profile
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import java.time.LocalDateTime

@Component
@Profile("dev")
class MockResource : NzffResource
{
  override fun getOrganisedWishlist(@RequestParam("id") id: String,
                                    @RequestParam("jimMode", required = false) jimMode: Boolean,
                                    @RequestBody(required = false) filters: List<ScheduleFilter>?): ResponseEntity<NzffResponse>
  {
    return ResponseEntity.ok(NzffResponse(
        message = "Filters: $filters",
        movieList = listOf(
            Movie(
                title = "The Atlantic",
                websiteUrl = "/2018/auckland/the-atlantic/",
                startTime = LocalDateTime.parse("2018-07-18T18:00:00"),
                endTime = LocalDateTime.parse("2018-07-18T20:00:00"),
                thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/bi/9a/k3/f9/Atlanten_001%20KEY_web-2000-2000-1125-1125-crop-fill.jpg"
            ),
            Movie(
                title = "Lucky",
                websiteUrl = "/2018/auckland/lucky/",
                startTime = LocalDateTime.parse("2018-07-19T18:00:00"),
                endTime = LocalDateTime.parse("2018-07-19T20:00:00"),
                thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/lv/yl/xa/2t/6_Harry%20Dean%20Stanton%20and%20Ron%20Livingston%20in%20LUCKY%2C%20a%20Magnolia%20Pictures%20release.%20Photo%20courtesy%20of%20Magnolia%20Pictures.-2000-2000-1125-1125-crop-fill.jpg"
            ),
            Movie(
                title = "Orlando",
                websiteUrl = "/2018/auckland/orlando/",
                startTime = LocalDateTime.parse("2018-07-20T18:00:00"),
                endTime = LocalDateTime.parse("2018-07-20T20:00:00"),
                thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/v1/lh/vz/o2/afd1d26df64939eef7cda51b45159920-2000-2000-1125-1125-crop-fill.jpg"
            ),
            Movie(
                title = "Desert Hearts",
                websiteUrl = "/2018/auckland/desert-hearts/",
                startTime = LocalDateTime.parse("2018-07-20T09:00:00"),
                endTime = LocalDateTime.parse("2018-07-20T11:00:00"),
                thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/pk/49/tb/rp/29139id_245%20KEY-RGB%20crop-2000-2000-1125-1125-crop-fill.jpg"
            ),
            Movie(
                title = "Liquid Sky",
                websiteUrl = "/2018/auckland/liquid-sky/",
                startTime = LocalDateTime.parse("2018-07-22T09:00:00"),
                endTime = LocalDateTime.parse("2018-07-22T11:00:00"),
                thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/pd/7m/v2/2j/Liquid_Sky_1.1.4-800-800-450-450-crop-fill.jpg"
            ),
            Movie(
                title = "The Swimming Pool",
                websiteUrl = "/2018/auckland/the-swimming-pool/",
                startTime = LocalDateTime.parse("2018-07-23T17:00:00"),
                endTime = LocalDateTime.parse("2018-07-23T19:00:00"),
                thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/ti/pc/7k/45/MP_PH_19_TE_XXX_XXX_BD-2000-2000-1125-1125-crop-fill.jpg"
            ),
            Movie(
                title = "Zama",
                websiteUrl = "/2018/auckland/zama/",
                startTime = LocalDateTime.parse("2018-07-23T17:00:00"),
                endTime = LocalDateTime.parse("2018-07-23T19:00:00"),
                thumbnailUrl = "https://www.nziff.co.nz/assets/resized/sm/upload/sn/ah/7b/l6/ZAMA_5-800-800-450-450-crop-fill.jpg"
            )
        )
    ))
  }
}