package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.dao.NzffDao
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.NzffResponse
import ash.java.nzfforganiser.model.ScheduleFilter
import ash.java.nzfforganiser.schedule.NzffScheduler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam

@Component
class NzffResourceImpl @Autowired constructor(private val nzffDao: NzffDao,
                                              private val scheduler: NzffScheduler) : NzffResource
{
  private val logger = LoggerFactory.getLogger(NzffResourceImpl::class.java)

  override fun getOrganisedWishlist(@RequestParam("id") id: String,
                                    @RequestParam("jimMode", required = false) jimMode: Boolean,
                                    @RequestBody(required = false) filters: List<ScheduleFilter>?): ResponseEntity<NzffResponse>
  {
    val wishlist = nzffDao.getWishlist(id)

    if (wishlist.isEmpty())
    {
      logger.info("Wishlist was empty, returning 404")
      return ResponseEntity
          .status(HttpStatus.NOT_FOUND)
          .body(NzffResponse(message = "Wishlist not found or is empty"))
    }

    val wishlistItemSessions: List<List<Movie>> = wishlist
        .map { w -> nzffDao.getMovieTimes(w) }
        .toList()

    val suggestion = scheduler.findSchedule(wishlistItemSessions, filters ?: emptyList(), jimMode)

    return ResponseEntity
        .ok(NzffResponse(message = "Found schedule suggestion",
            movieList = suggestion.scheduleSuggestion,
            invalidMovies = suggestion.unavailableMovies))
  }

  @GetMapping("/ping")
  fun pong(): ResponseEntity<String>
  {
    return ResponseEntity.ok("pong!")
  }
}