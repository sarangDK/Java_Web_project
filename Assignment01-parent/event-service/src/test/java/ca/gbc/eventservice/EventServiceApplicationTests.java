package ca.gbc.eventservice;

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
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.MongoDBContainer;
import ca.gbc.eventservice.stub.userClientStub;

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import static io.restassured.path.json.JsonPath.from;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class EventServiceApplicationTests {

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
    void createEventTest() {

        String requestBody = """
            {
                "userId": "1",
                "eventName": "Workshop",
                "eventType": "Learning",
                "expectedAttendees": 1
            }
            """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/event")
                .then()
                .log().all()
                .statusCode(201)
                .body("eventId", Matchers.notNullValue())
                .body("userId", Matchers.equalTo("1"))
                .body("eventName", Matchers.equalTo("Workshop"))
                .body("eventType", Matchers.equalTo("Learning"))
                .body("expectedAttendees", Matchers.equalTo(1));
    }

    @Test
    void getAllEventsTest() {

        String requestBody = """
            {
                "userId": "1",
                "eventName": "Workshop",
                "eventType": "Learning",
                "expectedAttendees": 1
            }
            """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/event")
                .then()
                .log().all()
                .statusCode(201)
                .body("eventId", Matchers.notNullValue())
                .body("userId", Matchers.equalTo("1"))
                .body("eventName", Matchers.equalTo("Workshop"))
                .body("eventType", Matchers.equalTo("Learning"))
                .body("expectedAttendees", Matchers.equalTo(1));

        RestAssured.given()
                .when()
                .get("/api/v1/event")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].eventId", Matchers.notNullValue())
                .body("[0].userId", Matchers.equalTo("1"))
                .body("[0].eventName", Matchers.equalTo("Workshop"))
                .body("[0].eventType", Matchers.equalTo("Learning"))
                .body("[0].expectedAttendees", Matchers.equalTo(1));
    }

    @Test
    void shouldUserCreateEvent() {
        String requestBody = """
            {
                "userId": "1",
                "eventName": "Workshop",
                "eventType": "Learning",
                "expectedAttendees": 1
            }
            """;

        userClientStub.stubUserCall("1");

        var response = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/event")
                .then()
                .log().all()
                .statusCode(201)
                .extract()
                .body().asString();

        assertThat(from(response).getString("userId"), equalTo("1"));
        assertThat(from(response).getString("eventName"), equalTo("Workshop"));
        assertThat(from(response).getString("eventType"), equalTo("Learning"));
        assertThat(from(response).getInt("expectedAttendees"), equalTo(1));
    }

}
