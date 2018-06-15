package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.model.NzffResponse
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.DayOfWeek
import java.time.LocalTime

@CrossOrigin
@RestController
@RequestMapping("/wishlist")
interface NzffResource
{
  @GetMapping
  @ResponseBody
  fun getOrganisedWishlist(url: String, from: LocalTime, to: LocalTime, disabledDays: List<DayOfWeek>): ResponseEntity<NzffResponse>
}