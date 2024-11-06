package ca.gbc.roomservice;

import io.restassured.RestAssured;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.context.annotation.Import;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.TestcontainersConfiguration;


@Import(TestcontainersConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
class RoomServiceApplicationTests {

    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("room_service")
            .withUsername("admin")
            .withPassword("password");

    @LocalServerPort
    public Integer port;

    static {
        postgreSQLContainer.start();
        System.out.println("PostgreSQL Container started: " + postgreSQLContainer.isRunning());
    }

    @BeforeEach
    void setup() {
        RestAssured.baseURI = "http://localhost";
        if (port != null) {
            RestAssured.port = port;
            System.out.println("Using port: " + port);
        } else {
            throw new IllegalStateException("Port is not initialized!");
        }
    }

    @Test
    void createRoomTest() {
        String requestBody = """
                {
                    "roomName": "Sweet Room",
                    "roomCapacity": "4",
                    "roomAvailability": true,
                    "roomFeatures": "Projector, Whiteboard"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/rooms")
                .then()
                .log().all()
                .statusCode(201)
                .contentType("application/json")
                .body("roomId", Matchers.notNullValue())
                .body("roomName", Matchers.equalTo("Sweet Room"))
                .body("roomCapacity", Matchers.equalTo("4"))
                .body("roomAvailability", Matchers.equalTo(true))
                .body("roomFeatures", Matchers.equalTo("Projector, Whiteboard"));
    }

    @Test
    void getAllRoomsTest() {
        String requestBody = """
                {
                    "roomName": "Queen Size",
                    "roomCapacity": "3",
                    "roomAvailability": false,
                    "roomFeatures": "Whiteboard"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/rooms")
                .then()
                .statusCode(201)
                .contentType("application/json")
                .body("roomId", Matchers.notNullValue())
                .body("roomName", Matchers.equalTo("Queen Size"))
                .body("roomCapacity", Matchers.equalTo("3"))
                .body("roomAvailability", Matchers.equalTo(false))
                .body("roomFeatures", Matchers.equalTo("Whiteboard"));

        RestAssured.given()
                .when()
                .get("/api/rooms")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].roomName", Matchers.notNullValue())
                .body("[0].roomCapacity", Matchers.equalTo("3"))
                .body("[0].roomAvailability", Matchers.equalTo(false))
                .body("[0].roomFeatures", Matchers.equalTo("Whiteboard"));
    }
}
