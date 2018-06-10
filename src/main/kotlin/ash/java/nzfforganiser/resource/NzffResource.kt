package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.model.NzffResponse
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
                           @RequestParam("from") from: LocalTime,
                           @RequestParam("to") to: LocalTime,
                           @RequestParam("disabledDay") disabledDays: List<DayOfWeek>): ResponseEntity<NzffResponse>
}