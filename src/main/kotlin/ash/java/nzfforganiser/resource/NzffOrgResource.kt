package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.dao.NzffDao
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import javax.inject.Singleton
import javax.ws.rs.Path
import javax.ws.rs.QueryParam
import javax.ws.rs.core.Response

@Component
@Singleton
@Path("/nzfforg")
class NzffOrgResource @Autowired constructor(private val nzffDao: NzffDao)
{
  private val logger = LoggerFactory.getLogger(NzffOrgResource::class.java)

  @Path("/wishlist")
  fun getOrganisedWishlist(@QueryParam("id") id: String = ""): Response
  {
    if (id.isBlank())
    {
      logger.info("id was blank, returning 400")
      return Response
          .status(Response.Status.NOT_ACCEPTABLE)
          .entity("id query parameter must be populated").build()
    }

    val wishlist = nzffDao.retrieveWishlist(id)

    if (wishlist.isEmpty())
    {
      logger.info("wishlist was empty, returning 404")
      return Response
          .status(Response.Status.NOT_FOUND)
          .entity("Wishlist not found or is empty").build()
    }

    return Response.ok().build()
  }

  @Path("/ping")
  fun pong(): Response
  {
    return Response.ok("pong!").build()
  }
}