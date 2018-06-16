package ash.java.nzfforganiser

import ash.java.nzfforganiser.resource.NzffResourceImpl
import ash.java.nzfforganiser.scheduler.NzffSchedulerImpl
import io.restassured.http.ContentType
import org.junit.Test
import io.restassured.module.mockmvc.RestAssuredMockMvc.*

class NzffResourceTest
{
  private val endpoint = "/wishlist"

  @Test
  fun `empty id returns 400`()
  {
    given()
        .standaloneSetup(
            NzffResourceImpl(
                EmptyWishlistNzffDao(),
                NzffSchedulerImpl()
            )
        )
    .`when`()
        .post(endpoint)
    .then()
        .statusCode(400)
  }

  @Test
  fun `empty wishlist returns 404`()
  {
    given()
        .standaloneSetup(
            NzffResourceImpl(
                EmptyWishlistNzffDao(),
                NzffSchedulerImpl()
            )
        )
    .`when`()
        .post("$endpoint?id=123")
    .then()
        .statusCode(404)
  }

  @Test
  fun `filters are parsed without error`()
  {
    given()
        .standaloneSetup(
            NzffResourceImpl(
                EmptyWishlistNzffDao(),
                NzffSchedulerImpl()
            )
        )
        .contentType(ContentType.JSON)
        .body("""
          [
            {
              "day": "WEDNESDAY",
              "excluded": true
            },
            {
              "day": "FRIDAY",
              "from": "09:00:00",
              "to": "22:00:00"
            }
          ]
        """.trimIndent())
    .`when`()
        .post("$endpoint?id=123")
    .then()
        .statusCode(404)
  }
}
