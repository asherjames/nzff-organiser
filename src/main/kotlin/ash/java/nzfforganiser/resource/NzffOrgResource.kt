package ash.java.nzfforganiser.resource

import ash.java.nzfforganiser.dao.NzffDao
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
  @Path("/wishlist")
  fun getOrganisedWishlist(@QueryParam("id") id: String) : Response
  {
    return Response.ok().build()
  }

  @Path("/ping")
  fun pong() : Response
  {
    return Response.ok().build()
  }
}