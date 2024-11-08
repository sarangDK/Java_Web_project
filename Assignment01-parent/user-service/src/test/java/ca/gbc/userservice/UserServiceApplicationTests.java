package ca.gbc.userservice;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.hamcrest.Matchers;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserServiceApplicationTests {
    @ServiceConnection
    static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:latest");


    @LocalServerPort
    private Integer port;

    @BeforeEach
        // Any @Test, when each of them runs, this runs before them
    void setup(){
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = port;
    }

    static {
        postgreSQLContainer.start();
    }

    @Test
    void createTypeTest() {
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"));
    }

    @Test
    void getAllTypesTest() {
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/type")
                .then()
                .log().all()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].type_name", Matchers.equalTo("student"));
    }



    @Test
    void updateTypeTest() {
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        Integer id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"))
                .extract()
                .path("type_id");

        Long type_id = Long.valueOf(id);


        requestBody = """
                {
                    "type_name": "student"
                }
                """;
        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/api/v1/type/"+type_id)
                .then()
                .statusCode(204)
                .header("Location", "/api/type/"+type_id);
    }

    @Test
    void deleteTypeTest(){
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        Integer id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"))
                .extract()
                .path("type_id");

        Long type_id = Long.valueOf(id);


        RestAssured.given()
                .when()
                .pathParam("type_id", type_id)
                .delete("/api/v1/type/{type_id}")
                .then()
                .log().all()
                .statusCode(204);
    }


    // integration test for User
    @Test
    void createUserTest() {

        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        Integer type_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"))
                .extract()
                .path("type_id");



        requestBody = """
                {
                  "user_name" : "John Doe",
                  "user_email": "john.doe@example.com",
                  "type_id":""" + type_id + """
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(201)  // Expect status code 201 (created)
                .body("user_id", Matchers.notNullValue())
                .body("user_name", Matchers.equalTo("John Doe"))
                .body("user_email", Matchers.equalTo("john.doe@example.com"))
                .body("type.type_name", Matchers.equalTo("student"))
                .body("type.type_id", Matchers.equalTo(type_id));
    }

    @Test
    void getAllUsersTest() {
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        Integer type_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"))
                .extract()
                .path("type_id");



        requestBody =  """
                {
                  "user_name" : "John Doe",
                  "user_email": "john.doe@example.com",
                  "type_id":""" + type_id + """
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(201)
                .body("user_id", Matchers.notNullValue())
                .body("user_name", Matchers.equalTo("John Doe"))
                .body("user_email", Matchers.equalTo("john.doe@example.com"))
                .body("type.type_name", Matchers.equalTo("student"))
                .body("type.type_id", Matchers.equalTo(type_id));

        RestAssured.given()
                .contentType("application/json")
                .when()
                .get("/api/v1/user")
                .then()
                .statusCode(200)
                .body("size()", Matchers.greaterThan(0))
                .body("[0].user_name", Matchers.equalTo("John Doe"))
                .body("[0].user_email", Matchers.equalTo("john.doe@example.com"))
                .body("[0].type.type_name", Matchers.equalTo("student"))
                .body("[0].type.type_id", Matchers.equalTo(1));
    }

    @Test
    void getUserByIdTest() {
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        Integer type_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"))
                .extract()
                .path("type_id");


        requestBody = """
                {
                  "user_name" : "John Doe",
                  "user_email": "john.doe@example.com",
                  "type_id":""" + type_id + """
                }
                """;

        Integer user_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(201)
                .body("user_id", Matchers.notNullValue())
                .body("user_name", Matchers.equalTo("John Doe"))
                .body("user_email", Matchers.equalTo("john.doe@example.com"))
                .body("type.type_name", Matchers.equalTo("student"))
                .body("type.type_id", Matchers.equalTo(type_id))
                .extract()
                .path("user_id");

        RestAssured.given()
                .when()
                .contentType("application/json")
                .get("/api/v1/user/"+user_id)
                .then()
                .statusCode(200)
                .body("user_name", Matchers.equalTo("John Doe"))
                .body("user_email", Matchers.equalTo("john.doe@example.com"))
                .body("type.type_name", Matchers.equalTo("student"))
                .body("type.type_id", Matchers.equalTo(type_id));
    }
    @Test
    void isStaffTest() {
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        Integer type_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"))
                .extract()
                .path("type_id");


        requestBody = """
                {
                  "user_name" : "John Doe",
                  "user_email": "john.doe@example.com",
                  "type_id":""" + type_id + """
                }
                """;

        Integer user_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(201)
                .body("user_id", Matchers.notNullValue())
                .body("user_name", Matchers.equalTo("John Doe"))
                .body("user_email", Matchers.equalTo("john.doe@example.com"))
                .body("type.type_name", Matchers.equalTo("student"))
                .body("type.type_id", Matchers.equalTo(type_id))
                .extract()
                .path("user_id");

        RestAssured.given()
                .when()
                .contentType("application/json")
                .get("/api/v1/user/isStaff/"+user_id)
                .then()
                .statusCode(200)
                .body(Matchers.equalTo("student"));
    }
    @Test
    void updateUserTest(){
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;


        Integer type_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"))
                .extract()
                .path("type_id");

        requestBody = """
                {
                  "user_name" : "John Doe",
                  "user_email": "john.doe@example.com",
                  "type_id":""" + type_id + """
                }
                """;

        Integer user_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(201)
                .body("user_id", Matchers.notNullValue())
                .body("user_name", Matchers.equalTo("John Doe"))
                .body("user_email", Matchers.equalTo("john.doe@example.com"))
                .body("type.type_name", Matchers.equalTo("student"))
                .body("type.type_id", Matchers.equalTo(type_id))
                .extract()
                .path("user_id");

        requestBody = """
                {
                  "user_name" : "Bob Smith",
                  "user_email": "bob.smith@example.com",
                  "type_id":""" + type_id + """
                }
                """;

        RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .put("/api/v1/user/"+user_id)
                .then()
                .statusCode(204)
                .header("Location", "/api/user/"+user_id);
    }

    @Test
    void deleteUserTest() {
        String requestBody = """
                {
                    "type_name": "student"
                }
                """;

        // Use RestAssured to send a POST request to the "/api/user" endpoint
        Integer type_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/type")
                .then()
                .statusCode(201)
                .body("type_id", Matchers.notNullValue())
                .body("type_name", Matchers.equalTo("student"))
                .extract()
                .path("type_id");

        requestBody = """
                {
                  "user_name" : "John Doe",
                  "user_email": "john.doe@example.com",
                  "type_id":""" + type_id + """
                }
                """;

        Integer user_id = RestAssured.given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/api/v1/user")
                .then()
                .statusCode(201)
                .body("user_id", Matchers.notNullValue())
                .body("user_name", Matchers.equalTo("John Doe"))
                .body("user_email", Matchers.equalTo("john.doe@example.com"))
                .body("type.type_name", Matchers.equalTo("student"))
                .body("type.type_id", Matchers.equalTo(type_id))
                .extract()
                .path("user_id");

        RestAssured.given()
                .when()
                .pathParam("user_id", user_id)
                .delete("/api/v1/user/{user_id}")
                .then()
                .statusCode(204);
    }

}
