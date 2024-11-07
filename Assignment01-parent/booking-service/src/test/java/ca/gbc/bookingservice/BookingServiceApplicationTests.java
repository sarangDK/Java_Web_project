package ca.gbc.bookingservice;
import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import ca.gbc.bookingservice.stub.roomClientStub;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MongoDBContainer;
import static io.restassured.path.json.JsonPath.from;



@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class BookingServiceApplicationTests {

    private static WireMockServer wireMockServer;


    @ServiceConnection

    static MongoDBContainer mongoDbContainer = new MongoDBContainer("mongo:latest");


    @LocalServerPort
    public Integer port;

    static {
        mongoDbContainer.start();
        System.out.println("MongoDB container started: " + mongoDbContainer.isRunning());
    }


    @BeforeAll
    static void setupWireMockServer() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        configureFor("localhost", wireMockServer.port());
        System.out.println("WireMock server started on port: " + wireMockServer.port());
    }

    @AfterAll
    static void teardown() {
        wireMockServer.stop();
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
                "bookingNumber": "BOOKING-1",
                "userId": "1",
                "roomId": 1,
                "checkIn": "2021-10-10T10:00:00.000+00:00",
                "checkOut": "2021-10-10T11:00:00.000+00:00",
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
                .body("bookingNumber", Matchers.equalTo("BOOKING-1"))
                .body("userId", Matchers.notNullValue())
                .body("roomId", Matchers.equalTo(1))
                .body("checkIn", Matchers.equalTo("2021-10-10T10:00:00.000+00:00"))
                .body("checkOut", Matchers.equalTo("2021-10-10T11:00:00.000+00:00"))
                .body("purpose", Matchers.equalTo("Meeting"));
    }

    @Test
    void getAllBookingsTests() {

        String requestBody = """
            {
                
                "bookingNumber": "BOOKING-1",
                "userId": "1",
                "roomId": 1,
                "checkIn": "2021-10-10T10:00:00",
                "checkOut": "2021-10-10T11:00:00",
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
                .body("bookingNumber", Matchers.equalTo("BOOKING-1"))
                .body("userId", Matchers.notNullValue())
                .body("roomId", Matchers.equalTo(1))
                .body("checkIn", Matchers.equalTo("2021-10-10T10:00:00.000+00:00"))
                .body("checkOut", Matchers.equalTo("2021-10-10T11:00:00.000+00:00"))
                .body("purpose", Matchers.equalTo("Meeting"));

        RestAssured.given()
                .when()
                .get("/api/v1/booking")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].bookingNumber", Matchers.equalTo("BOOKING-1"))
                .body("[0].userId", Matchers.notNullValue())
                .body("[0].roomId", Matchers.equalTo(1))
                .body("[0].checkIn", Matchers.equalTo("2021-10-10T10:00:00.000+00:00"))
                .body("[0].checkOut", Matchers.equalTo("2021-10-10T11:00:00.000+00:00"))
                .body("[0].purpose", Matchers.equalTo("Meeting"));
    }


    @Test
    void shouldUserBook() {
        String requestBody = """
            {
                "bookingNumber": "BOOKING-1",
                "userId": "1",
                "roomId": 1,
                "checkIn": "2021-10-10T10:00:00",
                "checkOut": "2021-10-10T11:00:00",
                "purpose": "Meeting",
                "availability": false
            }
            """;

        roomClientStub.stubRoomCall(1L, false);

        var response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/booking")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(from(response).getString("bookingNumber"), equalTo("BOOKING-1"));
        assertThat(from(response).getString("userId"), equalTo("1"));
        assertThat(from(response).getInt("roomId"), equalTo(1));
        assertThat(from(response).getString("checkIn"), equalTo("2021-10-10T10:00:00.000+00:00"));
        assertThat(from(response).getString("checkOut"), equalTo("2021-10-10T11:00:00.000+00:00"));
        assertThat(from(response).getString("purpose"), equalTo("Meeting"));

    }




}