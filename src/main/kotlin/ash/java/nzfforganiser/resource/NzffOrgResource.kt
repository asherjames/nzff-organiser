package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.dao.NzffDao
import ash.java.nzfforganiser.model.Movie
import ash.java.nzfforganiser.model.Request
import ash.java.nzfforganiser.model.Session
import ash.java.nzfforganiser.scheduler.NzffScheduler
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.inject.Singleton
import javax.ws.rs.*
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Component
@Singleton
@Path("/nzfforg")
class NzffOrgResource @Autowired constructor(private val nzffDao: NzffDao, private val scheduler: NzffScheduler)
{
  private val logger = LoggerFactory.getLogger(NzffOrgResource::class.java)

  @POST
  @Path("/wishlist")
  @Consumes(MediaType.APPLICATION_JSON)
  @Produces(MediaType.APPLICATION_JSON)
  fun getOrganisedWishlist(request: Request): Response
  {
    if (request.id.isBlank())
    {
      logger.info("id was blank, returning 400")
      return Response
          .status(Response.Status.NOT_ACCEPTABLE)
          .entity("id query parameter must be populated").build()
    }

    val wishlist = nzffDao.retrieveWishlist(request.id)

    if (wishlist.isEmpty())
    {
      logger.info("Wishlist was empty, returning 404")
      return Response
          .status(Response.Status.NOT_FOUND)
          .entity("Wishlist not found or is empty").build()
    }

    val movieSessions: Map<Movie, List<Session>> = wishlist
        .map { it to nzffDao.getMovieTimes(it) }
        .toMap()

    val suggestion = scheduler.getSchedule(movieSessions)

    return Response.ok(suggestion).build()
  }

  @GET
  @Path("/ping")
  fun pong(): Response
  {
    return Response.ok("pong!").build()
  }
}