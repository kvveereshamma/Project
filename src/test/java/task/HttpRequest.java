package task;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.HashMap;

import org.testng.annotations.Test;

public class HttpRequest {
	int id;
	@Test
	void getUser() {
		
	given()
	.when()
	.get("https://reqres.in/api/users?page=2")
	.then()
	           .statusCode(200)
	           .body("page",equalTo(2))
	           .log().all();
		
	}
	@Test
		void createUser() {
		HashMap data=new HashMap();
		data.put("name","omkar");
		data.put("job","trainer");
		
		id=given()
		 .contentType("application/json")
		 .body(data)
		.when()
		 .post("https://reqres.in/api/users")
		 .jsonPath().getInt("id");
		 
		//.then()
		 //.statusCode(201)
		 //.log().all();		
	}

}
