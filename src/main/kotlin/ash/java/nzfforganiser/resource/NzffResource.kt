package ash.java.nzfforganiser.resource

import java.time.DayOfWeek
import java.time.LocalTime
import javax.ws.rs.GET
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.QueryParam
import javax.ws.rs.core.MediaType
import javax.ws.rs.core.Response

@Path("/nzfforg")
interface NzffResource
{
  @GET
  @Path("/wishlist")
  @Produces(MediaType.APPLICATION_JSON)
  fun getOrganisedWishlist(@QueryParam("id") id: String,
                           @QueryParam("from") from: LocalTime,
                           @QueryParam("to") to: LocalTime,
                           @QueryParam("disabledDay") disabledDays: List<DayOfWeek>): Response
}