package ash.java.nzfforganiser

import ash.java.nzfforganiser.dao.NzffDaoImpl
import ash.java.nzfforganiser.dao.WishlistScraperClientStub
import ash.java.nzfforganiser.resource.NzffResourceImpl
import ash.java.nzfforganiser.scheduler.NzffSchedulerImpl
import org.junit.Test
import io.restassured.module.mockmvc.RestAssuredMockMvc.*
import io.restassured.module.mockmvc.matcher.RestAssuredMockMvcMatchers.*

class NzffOrganiserApplicationTests
{
  @Test
  fun `empty id returns 400`()
  {
    given()
        .standaloneSetup(NzffResourceImpl(NzffDaoImpl(WishlistScraperClientStub(), "", ""), NzffSchedulerImpl()))
        .`when`()
        .post("/wishlist")
        .then()
        .statusCode(400)
  }
}
