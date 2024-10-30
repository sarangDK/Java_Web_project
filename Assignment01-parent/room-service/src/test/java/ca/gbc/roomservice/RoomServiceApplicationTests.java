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
                    "room_name": "Room A",
                    "room_capacity": "20",
                    "room_availability": true,
                    "room_features": "Projector, Whiteboard"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/rooms") // Ensure this matches your controller endpoint
                .then()
                .log().all()
                .statusCode(201)
                .contentType("application/json")
                .body("room_id", Matchers.notNullValue())
                .body("room_name", Matchers.equalTo("Room A"))
                .body("room_capacity", Matchers.equalTo("20"))
                .body("room_availability", Matchers.equalTo(true))
                .body("room_features", Matchers.equalTo("Projector, Whiteboard"));
    }

    @Test
    void getAllRoomsTest() {
        String requestBody = """
                {
                    "room_name": "Room B",
                    "room_capacity": "30",
                    "room_availability": false,
                    "room_features": "Whiteboard"
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
                .body("room_id", Matchers.notNullValue())
                .body("room_name", Matchers.equalTo("Room B"))
                .body("room_capacity", Matchers.equalTo("30"))
                .body("room_availability", Matchers.equalTo(false))
                .body("room_features", Matchers.equalTo("Whiteboard"));

        RestAssured.given()
                .when()
                .get("/api/rooms")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].room_name", Matchers.notNullValue())
                .body("[0].room_capacity", Matchers.equalTo("30"))
                .body("[0].room_availability", Matchers.equalTo(false))
                .body("[0].room_features", Matchers.equalTo("Whiteboard"));
    }
}
