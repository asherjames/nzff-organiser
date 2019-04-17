package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.model.NzffResponse
import ash.java.nzfforganiser.model.ScheduleRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.bind.annotation.ResponseBody

@CrossOrigin
@RestController
@RequestMapping("/wishlist")
interface NzffResource {
  @PostMapping
  @ResponseBody
  fun getOrganisedWishlist(id: String, requestFilters: ScheduleRequest?): ResponseEntity<NzffResponse>
}