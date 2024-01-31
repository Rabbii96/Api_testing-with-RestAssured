package User_Service;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;
public class Session {

    private static final String BASE_URL = "http://10.10.10.31:28088";
    private static final String ACCESS_TOKEN = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJmYXpsZXJhYmJpLm9ubm9yb2tvbUBnbWFpbC5jb20iLCJpYXQiOjE3MDUyMDk1NDQsImV4cCI6MTcwNTI5NTk0NH0.kTcAow7NAv_idfUTGdBqYpVOwdUuft2Xje7TBXQaoPo"; // Replace with your actual access token

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = BASE_URL;
        RestAssured.basePath = "/api/v1";
    }

    @Test
    void getAllSessions() {
        given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .queryParam("status", "ACTIVE")
                .queryParam("pageNumber", 0)
                .queryParam("pageSize", 10)
                .queryParam("sortBy", "createdAt")
                .queryParam("order", "ASC")
                .when()
                .get("/sessions")
                .then()
                .statusCode(200)
                .body("totalPages", greaterThanOrEqualTo(0))
                .body("totalElements", greaterThanOrEqualTo(0))
                .body("content[0].id", notNullValue())
                .body("content[0].name", notNullValue())
                .log().all();
    }

    @Test
    void createNewSession() {
        given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .contentType("application/json")
                .body("{\"name\": \"2026-25\"}")
                .when()
                .post("/sessions")
                .then()
                .statusCode(201)  // Use 201 for a successful creation response
                .body("id", notNullValue())  // Assuming the response includes an 'id'
                .log().all();
    }

    @Test
    void getSessionById() {
        String sessionId = "3"; // Replace with an actual session ID

        Response response = given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .pathParam("id", sessionId)
                .when()
                .get("/sessions/{id}")
                .then()
                .log().all()
                .extract().response();

        if (response.getStatusCode() == 200) {
            // Session found, assert on its details
            response.then()
                    .statusCode(200)
                    .body("id", equalTo(Integer.parseInt(sessionId)))
                    .body("name", notNullValue());
        } else if (response.getStatusCode() == 404) {
            // Session not found
            response.then()
                    .statusCode(404);
        } else {
            // Handle other status codes if needed
            // ...
        }
    }


    @Test
    void updateSession() {
        given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .pathParam("id", 5)
                .contentType("application/json")
                .body("{ \"name\": \"2213-28\" }")
                .when()
                .put("/sessions/{id}")
                .then()
                .statusCode(200)
                .body("id", equalTo(3))
                .body("name", equalTo("2212-28"))
                .log().all();
    }


    @Test
    void deleteSession() {
        given()
                .header("Authorization", "Bearer " + ACCESS_TOKEN)
                .pathParam("id", 7)
                .when()
                .delete("/sessions/{id}")  // Adjust the URL to include the path parameter
                .then()
                .statusCode(200)
                .log().all();
    }

}
