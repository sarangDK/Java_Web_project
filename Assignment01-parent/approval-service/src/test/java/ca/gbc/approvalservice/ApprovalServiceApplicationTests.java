package ca.gbc.approvalservice;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
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

import static com.github.tomakehurst.wiremock.client.WireMock.configureFor;
import io.restassured.RestAssured;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ApprovalServiceApplicationTests {

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
    void createApprovalTest() {
        String requestBody = """
            {
                "userId": 45465456,
                "eventId": "123123123",
                "eventType": "Learning",
                "isApproved": false
            }
            """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/approval")
                .then()
                .log().all()
                .statusCode(201)
                .body("approvalId", Matchers.notNullValue())
                .body("userId", Matchers.equalTo(45465456))
                .body("eventId", Matchers.equalTo("123123123"))
                .body("isApproved", Matchers.equalTo("false"));
    }

}
