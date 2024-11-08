package ca.gbc.bookingservice;

import ca.gbc.bookingservice.stub.roomClientStub;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.testcontainers.containers.MongoDBContainer;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;

import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWireMock(port = 0)
class BookingServiceApplicationTests {

    @LocalServerPort
    private int port;

    @ServiceConnection
    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");

    static {
        mongoDbContainer.start();
        System.out.println("MongoDB container started: " + mongoDbContainer.isRunning());
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    @Test
    void createBookingTests() {
        roomClientStub.stubRoomAvailabilityGet(1L, true);
        roomClientStub.stubRoomAvailabilityUpdate(1L, false);

        String requestBody = """
        {
            "userId": 1,
            "roomId": 1,
            "checkIn": "2021-10-10T10:00:00.000+00:00",
            "checkOut": "2021-10-10T11:00:00.000+00:00",
            "purpose": "Meeting"
        }
        """;

        var response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/booking")
                .then()
                .statusCode(201)
                .log().all()
                .extract().body().asString();

        // Assertions on response content
        assertThat(from(response).getString("userId"), equalTo("1"));
        assertThat(from(response).getInt("roomId"), equalTo(1));
        assertThat(from(response).getString("purpose"), equalTo("Meeting"));
    }

    @Test
    void getAllBookingsTests() {
        // Stub GET room availability for roomId=1
        roomClientStub.stubRoomAvailabilityGet(1L, true);
        roomClientStub.stubRoomAvailabilityUpdate(1L, false);


        String requestBody = """
            {
                "userId": 1,
                "roomId": 1,
                "checkIn": "2021-10-10T10:00:00.000+00:00",
                "checkOut": "2021-10-10T11:00:00.000+00:00",
                "purpose": "Meeting"
            }
            """;

        // Create a booking first
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/booking")
                .then()
                .statusCode(201);

        // Get all bookings
        RestAssured.given()
                .when()
                .get("/api/v1/booking")
                .then()
                .statusCode(200)
                .log().all()
                .body("size()", Matchers.greaterThan(0))
                .body("[0].roomId", equalTo(1))
                .body("[0].purpose", equalTo("Meeting"));
    }

    @Test
    void shouldUserBook() {
        // Stub GET request for room availability
        roomClientStub.stubRoomAvailabilityGet(1L, true);
        roomClientStub.stubRoomAvailabilityUpdate(1L, false);


        String requestBody = """
            {
                "userId": 1,
                "roomId": 1,
                "checkIn": "2021-10-10T10:00:00.000+00:00",
                "checkOut": "2021-10-10T11:00:00.000+00:00",
                "purpose": "Meeting"
            }
            """;

        var response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/booking")
                .then()
                .statusCode(201)
                .log().all()
                .extract().body().asString();

        // Assertions on response content
        assertThat(from(response).getString("bookingNumber"), Matchers.notNullValue());
        assertThat(from(response).getInt("roomId"), equalTo(1));
        assertThat(from(response).getString("purpose"), equalTo("Meeting"));
    }
}
