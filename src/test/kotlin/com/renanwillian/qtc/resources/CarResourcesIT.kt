package com.renanwillian.qtc.resources

import com.renanwillian.qtc.PostgresTestLifecycleManager
import io.quarkus.test.common.QuarkusTestResource
import io.quarkus.test.common.http.TestHTTPEndpoint
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured
import io.restassured.http.ContentType
import org.flywaydb.core.Flyway
import org.hamcrest.CoreMatchers.`is`
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import javax.inject.Inject
import javax.ws.rs.core.Response.Status


@QuarkusTest
@QuarkusTestResource(PostgresTestLifecycleManager::class)
@TestHTTPEndpoint(CarResources::class)
class CarResourcesIT {

    @Inject
    lateinit var flyway: Flyway

    @BeforeEach
    fun setUp() {
        flyway.clean()
        flyway.migrate()
    }

    @Test
    fun `when list resource is called all cars is returned`() {
        RestAssured.given()
            .`when`().get()
            .then()
            .statusCode(Status.OK.statusCode)
            .body(`is`("""[{"id":1,"make":"Mercedes-Benz","model":"500 SL","year":1992}]"""))
    }

    @Test
    fun `when create resource is called with valid data a created is returned`() {
        RestAssured.given()
            .`when`()
            .contentType(ContentType.JSON)
            .body("""{"make": "BMW", "model": "1 Series", "year": 2013}""")
            .post()
            .then()
            .statusCode(Status.CREATED.statusCode)
            .body("id", `is`(2))
            .body("make", `is`("BMW"))
            .body("model", `is`("1 Series"))
            .body("year", `is`(2013))
    }

    @Test
    fun `when create resource is called with duplicated data a bad request is returned`() {
        RestAssured.given()
            .`when`()
            .contentType(ContentType.JSON)
            .body("""{"make": "Mercedes-Benz", "model": "500 SL", "year": 1992}""")
            .post()
            .then()
            .statusCode(Status.BAD_REQUEST.statusCode)
    }

    @Test
    fun `when update resource is called with valid data a ok is returned`() {
        RestAssured.given()
            .`when`()
            .contentType(ContentType.JSON)
            .body("""{"make": "BMW", "model": "1 Series", "year": 2013}""")
            .put("/1")
            .then()
            .statusCode(Status.OK.statusCode)
            .body("id", `is`(1))
            .body("make", `is`("BMW"))
            .body("model", `is`("1 Series"))
            .body("year", `is`(2013))
    }

    @Test
    fun `when find resource is called with valid id a car is returned`() {
        RestAssured.given()
            .`when`()
            .get("/1")
            .then()
            .statusCode(Status.OK.statusCode)
            .body("id", `is`(1))
            .body("make", `is`("Mercedes-Benz"))
            .body("model", `is`("500 SL"))
            .body("year", `is`(1992))
    }

    @Test
    fun `when find resource is called with invalid id a not found is returned`() {
        RestAssured.given()
            .`when`()
            .get("/500")
            .then()
            .statusCode(Status.NOT_FOUND.statusCode)
    }

    @Test
    fun `when delete resource is called with valid id a no content is returned`() {
        RestAssured.given()
            .`when`()
            .delete("/1")
            .then()
            .statusCode(Status.NO_CONTENT.statusCode)
    }

    @Test
    fun `when delete resource is called with invalid id a not found is returned`() {
        RestAssured.given()
            .`when`()
            .delete("/500")
            .then()
            .statusCode(Status.NOT_FOUND.statusCode)
    }
}