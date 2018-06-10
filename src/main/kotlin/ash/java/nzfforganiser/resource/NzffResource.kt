package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.model.NzffResponse
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.DayOfWeek
import java.time.LocalTime

@RestController
@RequestMapping("/nzfforg")
interface NzffResource
{
  @GetMapping("/wishlist")
  @ResponseBody
  fun getOrganisedWishlist(@RequestParam("id") id: String,
                           @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
                           @RequestParam("from", defaultValue = "00:00", required = false)
                           from: LocalTime,
                           @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
                           @RequestParam("to", defaultValue = "23:59:59", required = false)
                           to: LocalTime,
                           @RequestParam("disabledDay", defaultValue = "", required = false)
                           disabledDays: List<DayOfWeek>): ResponseEntity<NzffResponse>
}