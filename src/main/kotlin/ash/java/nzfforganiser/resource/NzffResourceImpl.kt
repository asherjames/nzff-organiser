package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.dao.NzffDao
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.scheduler.NzffScheduler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Component
import java.time.DayOfWeek
import java.time.LocalTime
import javax.inject.Singleton
import javax.ws.rs.*
import javax.ws.rs.core.Response

@Component
@Singleton
@Profile("production")
class NzffResourceImpl @Autowired constructor(private val nzffDao: NzffDao,
                                              private val scheduler: NzffScheduler) : NzffResource
{
  private val logger = LoggerFactory.getLogger(NzffResourceImpl::class.java)

  override fun getOrganisedWishlist(id: String, from: LocalTime, to: LocalTime, disabledDays: List<DayOfWeek>): Response
  {
    if (id.isBlank())
    {
      logger.info("id was blank, returning 400")
      return Response
          .status(Response.Status.NOT_ACCEPTABLE)
          .entity("id query parameter must be populated").build()
    }

    val wishlist = nzffDao.getWishlist(id)

    if (wishlist.isEmpty())
    {
      logger.info("Wishlist was empty, returning 404")
      return Response
          .status(Response.Status.NOT_FOUND)
          .entity("Wishlist not found or is empty").build()
    }

    val wishlistItemSessions: List<List<Movie>> = wishlist
        .map { w -> nzffDao.getMovieTimes(w) }
        .toList()

    val suggestion = scheduler.getSchedule(wishlistItemSessions)

    return Response.ok(suggestion).build()
  }

  @GET
  @Path("/ping")
  fun pong(): Response
  {
    return Response.ok("pong!").build()
  }
}