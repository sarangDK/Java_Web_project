package ca.gbc.bookingservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceApplicationTests {

    @ServiceConnection

    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");


    @LocalServerPort
    public Integer port;

    static {
        mongoDbContainer.start();
        System.out.println("MongoDB container started: " + mongoDbContainer.isRunning());
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost:" + port;
        if(port != null) {
            RestAssured.port = port;
            System.out.println("Using port: " + port);
        } else {
            throw new IllegalStateException("Port is not initialized");
        }
    }
    @Test
    void createBookingTests() {

        String requestBody = """
            {
                "user_id": "1",
                "room_id": 1,
                "start_time": "2021-10-10T10:00:00.000+00:00",
                "end_time": "2021-10-10T11:00:00.000+00:00",
                "purpose": "Meeting"
            }
            """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/booking")
                .then()
                .log().all()
                .statusCode(201)
                .body("user_id", Matchers.notNullValue())
                .body("room_id", Matchers.equalTo(1))
                .body("start_time", Matchers.equalTo("2021-10-10T10:00:00.000+00:00"))
                .body("end_time", Matchers.equalTo("2021-10-10T11:00:00.000+00:000"))
                .body("purpose", Matchers.equalTo("Meeting"));
    }

    @Test
    void getAllBookingsTests() {

        String requestBody = """
            {
                "user_id": "1",
                "room_id": 1,
                "start_time": "2021-10-10T10:00:00",
                "end_time": "2021-10-10T11:00:00",
                "purpose": "Meeting"
            }
            """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/booking")
                .then()
                .log().all()
                .statusCode(201)
                .body("user_id", Matchers.notNullValue())
                .body("room_id", Matchers.equalTo(1))
                .body("start_time", Matchers.equalTo("2021-10-10T10:00:00.000+00:00"))
                .body("end_time", Matchers.equalTo("2021-10-10T11:00:00.000+00:00"))
                .body("purpose", Matchers.equalTo("Meeting"));

        RestAssured.given()
                .when()
                .get("/api/v1/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].user_id", Matchers.notNullValue())
                .body("[0].room_id", Matchers.equalTo(1))
                .body("[0].start_time", Matchers.equalTo("2021-10-10T10:00:00.000+00:00"))
                .body("[0].end_time", Matchers.equalTo("2021-10-10T11:00:00.000+00:00"))
                .body("[0].purpose", Matchers.equalTo("Meeting"));
    }

}