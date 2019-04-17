package ash.java.nzfforganiser

import ash.java.nzfforganiser.resource.NzffResourceImpl
import ash.java.nzfforganiser.schedule.NzffSchedulerImpl
import io.restassured.http.ContentType
import io.restassured.module.mockmvc.RestAssuredMockMvc.given
import org.junit.jupiter.api.Test

class NzffResourceTest {
  private val endpoint = "/wishlist"

  @Test
  fun `empty id returns 400`() {
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
  fun `empty wishlist returns 404`() {
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
  fun `supplying only excluded cinemas is still accepted`() {
    given()
        .standaloneSetup(
            NzffResourceImpl(
                EmptyWishlistNzffDao(),
                NzffSchedulerImpl()
            )
        )
        .contentType(ContentType.JSON)
        .body("""
          {
            "excludedCinemas": ["HOLLYWOOD"]
          }
        """.trimIndent())
    .`when`()
        .post("$endpoint?id=123")
    .then()
        .statusCode(404)
  }

  @Test
  fun `supplying only session gap is still accepted`() {
    given()
        .standaloneSetup(
            NzffResourceImpl(
                EmptyWishlistNzffDao(),
                NzffSchedulerImpl()
            )
        )
        .contentType(ContentType.JSON)
        .body("""
          {
            "sessionGap": 30
          }
        """.trimIndent())
    .`when`()
        .post("$endpoint?id=123")
    .then()
        .statusCode(404)
  }

  @Test
  fun `supplying only schedule filters is still accepted`() {
    given()
        .standaloneSetup(
            NzffResourceImpl(
                EmptyWishlistNzffDao(),
                NzffSchedulerImpl()
            )
        )
        .contentType(ContentType.JSON)
        .body("""
          {
            "scheduleFilters": [
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
          }
        """.trimIndent())
        .`when`()
        .post("$endpoint?id=123")
        .then()
        .statusCode(404)
  }

  @Test
  fun `supplying all filters is accepted`() {
    given()
        .standaloneSetup(
            NzffResourceImpl(
                EmptyWishlistNzffDao(),
                NzffSchedulerImpl()
            )
        )
        .contentType(ContentType.JSON)
        .body("""
          {
            "excludedCinemas": ["HOLLYWOOD"],
            "sessionGap": 30,
            "scheduleFilters": [
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
          }
        """.trimIndent())
        .`when`()
        .post("$endpoint?id=123")
        .then()
        .statusCode(404)
  }
}
