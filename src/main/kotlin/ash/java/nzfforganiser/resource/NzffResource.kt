package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.model.NzffResponse
import ash.java.nzfforganiser.model.ScheduleRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@CrossOrigin
@RestController
@RequestMapping("/wishlist")
interface NzffResource
{
  @PostMapping
  @ResponseBody
  fun getOrganisedWishlist(id: String, requestFilters: ScheduleRequest?): ResponseEntity<NzffResponse>
}