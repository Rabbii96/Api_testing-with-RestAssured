package Course_Service_Api;

import org.testng.annotations.Test;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class SubjectChapter {

    @Test
    void getSubjectChapter() {
        // Set base URI and port for the API
        RestAssured.baseURI = "http://10.10.10.31:28089";
        RestAssured.basePath = "/api/v1";

        given()
                .when()
                .get("/subject_chapter")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .log().all();
    }

    @Test
    void postSubjectChapter(){
        RestAssured.baseURI = "http://10.10.10.31:28089";
        RestAssured.basePath = "/api/v1/subject_lesson";

        // Request body for the POST request
        String requestBody = "{\n" +
                "  \"name\": \"Subject Lesson\",\n" +
                "  \"label\": \"Subject_lesson\",\n" +
                "  \"description\": \"This another new description\",\n" +
                "  \"coverPhotoUrl\": \"url:wfowfob\"\n" +
                "}";

        given()
                .contentType("application/json")
                .body(requestBody)
                .when()
                .post("/subject_chapter")
                .then()
                .statusCode(405)
                .body("success", is(false))
                .body("data.id", notNullValue()) // Assuming 'id' is part of the response
                .log().all();
    }
    @Test
    void getSubjectChapterById() {
        RestAssured.baseURI = "http://10.10.10.31:28089";
        RestAssured.basePath = "/api/v1";


        String createdId = given()
                .contentType("application/json")
                .body("{ \"name\": \"Subject Lesson\", \"label\": \"Subject_lesson\", \"description\": \"This another new description\", \"coverPhotoUrl\": \"url:wfowfob\" }")
                .when()
                .post("/subject_chapter")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", notNullValue())
                .extract()
                .path("data.id");


        given()
                .pathParam("id", createdId)
                .when()
                .get("/subject_chapter/{id}")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", equalTo(createdId))
                .log().all();
    }
    @Test
    void updateSubjectChapter() {
        // Set base URI and port for the API
        RestAssured.baseURI = "http://10.10.10.31:28089";
        RestAssured.basePath = "/api/v1";

        // Perform a POST request to create a new subject chapter and get its ID
        String createdId = given()
                .contentType("application/json")
                .body("{ \"name\": \"Subject Lesson\", \"label\": \"Subject_lesson\", \"description\": \"This another new description\", \"coverPhotoUrl\": \"url:wfowfob\" }")
                .when()
                .post("/subject_chapter")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", notNullValue())
                .extract()
                .path("data.id");

        // Request body for the PUT request to update the subject chapter
        String requestBody = "{\n" +
                "  \"name\": \"Updated Subject Lesson\",\n" +
                "  \"label\": \"Updated_Subject_lesson\",\n" +
                "  \"description\": \"Updated description\",\n" +
                "  \"coverPhotoUrl\": \"updated_url:wfowfob\",\n" +
                "  \"status\": \"ACTIVE\"\n" +
                "}";

        // Perform a PUT request to update the subject chapter with the created ID
        given()
                .contentType("application/json")
                .body(requestBody)
                .pathParam("id", createdId)
                .when()
                .put("/subject_chapter/{id}")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", equalTo(createdId)) // Assuming 'id' in the response should match the created ID
                .body("data.name", equalTo("Updated Subject Lesson")) // Assuming the updated name
                .body("data.label", equalTo("Updated_Subject_lesson")) // Assuming the updated label
                .body("data.description", equalTo("Updated description")) // Assuming the updated description
                .log().all();
    }
    @Test
    void deleteSubjectChapter() {
        // Set base URI and port for the API
        RestAssured.baseURI = "http://10.10.10.31:28089";
        RestAssured.basePath = "/api/v1";

        // Perform a POST request to create a new subject chapter and get its ID
        String createdId = given()
                .contentType("application/json")
                .body("{ \"name\": \"Subject Lesson\", \"label\": \"Subject_lesson\", \"description\": \"This another new description\", \"coverPhotoUrl\": \"url:wfowfob\" }")
                .when()
                .post("/subject_chapter")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data.id", notNullValue())
                .extract()
                .path("data.id");

        // Perform a DELETE request to delete the subject chapter with the created ID
        given()
                .pathParam("id", createdId)
                .when()
                .delete("/subject_chapter/{id}")
                .then()
                .statusCode(200)
                .body("success", is(true))
                .body("data", is(emptyOrNullString())) // Assuming 'data' should be empty after deletion
                .log().all();
    }

}
