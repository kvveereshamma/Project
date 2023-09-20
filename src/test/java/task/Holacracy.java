package task;

import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONObject;

import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Holacracy {

public void createUser() throws IOException {
	  RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
        String tokenEndpoint = "/v1.0/neo4j/node/tenant";
        RequestSpecification request = RestAssured.given();
        System.out.println("User hits create universe request");     
     // Load the Excel file
        FileInputStream file = new FileInputStream("data/testData.xlsx");
        Workbook workbook = new XSSFWorkbook(file);
        Sheet sheet = workbook.getSheet("Body"); // Replace "Sheet1" with the actual sheet name
       // Read the input JSON body from Excel
        String bodyString = sheet.getRow(1).getCell(1).getStringCellValue();
        JSONObject bodyJson = new JSONObject(bodyString);
        request.body(bodyJson.toString());
        request.contentType(ContentType.JSON);
        System.out.println("User valid Body");
        // Send the request and validate the response
        Response response = request.post(tokenEndpoint);
       
        response.then().log().all();
        String responseBody = response.getBody().asString();
       
   System.out.println("Response Body: " + responseBody);
   
   String s = response.jsonPath().getString("id");
   
   System.out.println(" Universeid created "+String);
   
// Close the workbook and file
        workbook.close();      
        if(response.statusCode()==201)
        {
        test.log(LogStatus.PASS, "Universe creation test passed", "Response statuscode: " + response.statusCode());
        }
        else
        {
        test.log(LogStatus.FAIL, "Universe creation test failed", "Response statuscode: " + response.statusCode());
        }
}

public void retrieveUniverseById()
{

       RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com/neo4j-service";
       String tokenEndpoint = "/v1.0/neo4j/node/tenant/"+Divya_Tenant;
       RequestSpecification request = RestAssured.given();
       System.out.println("User hits retrieve universe request"); 
       
    // Send the request and validate the response
       Response response = request.get(tokenEndpoint);
       
       response.then().log().all();      
       if(response.statusCode()==200)
       {
    	   System.out.println("Retrieve universe test passed", "Response statuscode: " + response.statusCode());
      }
       else
       {
    	   System.out.println("Retrieve universe test failed", "Response statuscode: " + response.statusCode());
       }
       
}

}

