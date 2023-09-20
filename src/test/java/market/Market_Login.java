package market;

import static io.restassured.RestAssured.given;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.sql.DataSource;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

import org.apache.commons.mail.EmailException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.reporters.jq.ReporterPanel;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import com.relevantcodes.extentreports.model.Log;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class Market_Login extends MyRunner {
 private String baseURI;

public void Iamservice() {
        RestAssured.baseURI = "http://ingress-gateway.gaiansolutions.com";
        String tokenEndpoint = "/iam-service/oauth/token";
        RequestSpecification request = RestAssured.given();
        test.log(LogStatus.PASS, "Post call Hit Succefully");
        try {
            // Load the Excel file
            FileInputStream file = new FileInputStream("data/testData.xlsx");
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheet("Login"); // Replace "Sheet1" with the actual sheet name
            // Read the input values from Excel
            String username = sheet.getRow(1).getCell(0).getStringCellValue();
            String password = sheet.getRow(1).getCell(1).getStringCellValue();
            String grantType = sheet.getRow(1).getCell(2).getStringCellValue();
            String provider = sheet.getRow(1).getCell(3).getStringCellValue();
            String clientId = sheet.getRow(1).getCell(4).getStringCellValue();
            String applicationId = sheet.getRow(1).getCell(5).getStringCellValue();
            String checkB2B = sheet.getRow(1).getCell(6).getStringCellValue();
            // Set the request parameters
            request.param("username", username);
            test.log(LogStatus.PASS, "User Enter's valid Username");

            request.param("password", password);
            test.log(LogStatus.PASS, "User Enter's valid Password");

            request.param("grant_type", grantType);
            request.param("provider", provider);
            request.param("clientId", clientId);
            request.param("applicationId", applicationId);
            request.param("checkB2B", checkB2B);
            // Send the request and validate the response
            Response response = request.post(tokenEndpoint);
            test.log(LogStatus.PASS, "User Enter's tokenEndpoint");

            response.then().log().all();
            Assert.assertEquals(response.statusCode(), 200);

            // Log the test result in the report
            test.log(LogStatus.PASS, "access_token Test Passed", "Response Status Code: " + response.statusCode());

            // Parse the response JSON to extract the access token
            String accessToken = response.jsonPath().getString("access_token");

            Cell accessTokenCell2 = sheet.getRow(1).createCell(8); // Create a new cell in the access token column
            accessTokenCell2.setCellValue(response.statusCode());

            LocalDateTime myDateObj = LocalDateTime.now();

            DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");

            String formattedDate = myDateObj.format(myFormatObj);

            Cell accessTokenCell3 = sheet.getRow(1).createCell(9); // Create a new cell in the access token column
            accessTokenCell3.setCellValue(formattedDate);

            // Write the modified workbook back to the file
            FileOutputStream outFile = new FileOutputStream("testData.xlsx");
            workbook.write(outFile);

            // Close the workbook and file
            workbook.close();
            file.close();
            outFile.close();
        } catch (IOException e) {
            e.printStackTrace();
            // Log the test failure in the report
            test.log(LogStatus.FAIL, "Login Test Failed", e.getMessage());
        }
    
    }

public void getAllAccounts()
{
	test=report.startTest("Holacracy_GetAllAccounts");
	Response response=RestAssured.given().log().all()
	 .baseUri("https://ingress-gateway.gaiansolutions.com/market-place-beta/v1.0/accounts")
	 .get()
	 .then().log().all().extract().response();
	test.log(LogStatus.PASS,"Retrieved AllAccounts");
	int status = response.getStatusCode();
	
	if(status==200)
	{
		test.log(LogStatus.PASS,"Retrieved All Accounts Successfully. Statuscode "+status);
	}
	else
	{
		test.log(LogStatus.FAIL,"Failed To Retrieve Data "+status);
	}
	report.endTest(test);

}

public void updateAccount() throws IOException {
	test=report.startTest("Holacracy_Update Account");
	FileInputStream fis=new FileInputStream("data/testData.xlsx");
	Workbook book=new XSSFWorkbook(fis);
	Sheet sheet = book.getSheet("Sheet1");
	String s=sheet.getRow(1).getCell(1).getStringCellValue();
	RestAssured.baseURI="https://ingress-gateway.gaiansolutions.com";
	test.log(LogStatus.PASS,"UpdateAccountDetails");   			
		int statusCode = given().log().all().baseUri("https://ingress-gateway.gaiansolutions.com").body(s)
				//int statusCode = given().body(Payload.UpdateAccount())
				.when().put("/market-place-beta//v1.0/accounts/648ed5e914eeb26b09df5f16").
				     then().log().all().extract().statusCode();
				
				if(statusCode==200) {
					test.log(LogStatus.PASS,"Account Updated Successfully "+statusCode);
					
				}
				else
				{
					test.log(LogStatus.FAIL,"Failed To Update Account Details. Statuscode "+statusCode);

				}
			report.endTest(test);
		    }

public void accountsID() {
test = report.startTest("Holacracy_GetAccountID");
int r = RestAssured.given().log().all().baseUri("https://ingress-gateway.gaiansolutions.com/market-place-beta/v1.0/accounts/648ed5e914eeb26b09df5f16")
.get().then().log().all().extract().statusCode();
test.log(LogStatus.PASS, "GetAccountID");
if (r == 200) {
test.log(LogStatus.PASS, "Retrieve Data Succesfully "+r);
} else {
			test.log(LogStatus.FAIL,"Failed To Retrieve Data. Statuscode "+r);
			}
			
}
public void createAccount() throws IOException {
test = report.startTest("Holacracy_CreateAccount");
RestAssured.baseURI = "https://ingress-gateway.gaiansolutions.com";
 String token = "/market-place-beta/v1.0/accounts";
FileInputStream fis=new FileInputStream("data/testData.xlsx");
Workbook book=new XSSFWorkbook(fis);
 Sheet sheet=book.getSheet("Sheet1");
String APIRequest=sheet.getRow(2).getCell(0).getStringCellValue();
String body01=sheet.getRow(2).getCell(1).getStringCellValue();
//    
int statusCode = given().log().all().baseUri("https://ingress-gateway.gaiansolutions.com").body(body01).when().post(token)
.then().log().all().extract()
.statusCode();
test.log(LogStatus.PASS, "Create Account");
//System.out.println("result of these is"+statusCode);
// Log the test Passed in the report
       if (statusCode == 200) {
    	   test.log(LogStatus.PASS, "Account Created Successfully "+statusCode);
       }
       else {
    	   // Log the test failure in the report
    	   test.log(LogStatus.FAIL, "Failed To Create Account. Statuscode "+statusCode);
       }

       report.endTest(test);
}
public void updateOrganisationAccount() throws IOException {
	test=report.startTest("Holacracy_UpdateOrganisationAccount");
	RestAssured.baseURI="https://ingress-gateway.gaiansolutions.com";
	String tokenEndpoint="/market-place-beta/v1.0/organisations";
	FileInputStream fis=new FileInputStream("data/testData.xlsx");
	Workbook book=new XSSFWorkbook(fis);
	Sheet sheet=book.getSheet("Sheet1");
	String ApiRequest=sheet.getRow(3).getCell(0).getStringCellValue();
	String Body=sheet.getRow(3).getCell(1).getStringCellValue();
	int statusCode=given().log().all().baseUri("https://ingress-gateway.gaiansolutions.com")
			.body(Body).when().put(tokenEndpoint)
			.then().log().all().extract().statusCode();
	test.log(LogStatus.PASS,"Update Organization");
	if(statusCode==200) {
		test.log(LogStatus.PASS,"Updated Organization Succesfully "+statusCode);
	}
	else
		{
			test.log(LogStatus.FAIL,"Failed To Update Organization Data. Statuscode "+statusCode);
			}
	report.endTest(test);
       }

public void creatOrganizationAccount() throws IOException
	{
	test=report.startTest("Holacracy_CreatOrganizationAccount");
	RestAssured.baseURI="https://ingress-gateway.gaiansolutions.com";
	String endPoint="/market-place-beta/v1.0/organisations";
	    FileInputStream fis=new FileInputStream("data/testData.xlsx");
	    Workbook book=new XSSFWorkbook(fis);
	    Sheet sh=book.getSheet("Sheet1");
	    String body=sh.getRow(4).getCell(1).getStringCellValue();
	    int statusCode = given().log().all().baseUri("https://ingress-gateway.gaiansolutions.com/market-place-beta/v1.0/organisations").body(body).when().post(endPoint)
	    		.then().log().all().extract().statusCode();
	    test.log(LogStatus.PASS,"Create Organization");
	    if(statusCode==200)
	    {
	    	test.log(LogStatus.PASS, "Created Organization Successfully "+statusCode);
	    }
	    else
	    {
	    	test.log(LogStatus.FAIL, "Failed To Creat Organization. Statuscode "+statusCode);
	    }
	    report.endTest(test);
	}

public void getOrganisationId() {
	test=report.startTest("Holacracy_GetOrganisationAccountID");
	String baseUri="https://ingress-gateway.gaiansolutions.com";
	String tokenEndPoint="market-place-beta/v1.0/organisations/648ed7e014eeb26b09df5f1c";
	int i=given().log().all().baseUri(baseUri)
	.when().get(tokenEndPoint)
	.then().log().all().extract().statusCode();
	test.log(LogStatus.PASS, "GetOrganisationAccountID");
	if (i == 200) {
	test.log(LogStatus.PASS, "Retrieve Data Succesfully "+i);
	} else {
				test.log(LogStatus.FAIL,"Failed To Retrieve Data. Statuscode "+i);
				}
	report.endTest(test);
}

public void getAllOrganisiationData()
{
	test =report.startTest("Holacracy_GetAllOrganizations_Data");
	int statuscode= given().log().all().baseUri("https://ingress-gateway.gaiansolutions.com/market-place-beta/v1.0/organisations").param("Id", "648ed7e014eeb26b09df5f1c")
			.when().get("https://ingress-gateway.gaiansolutions.com/market-place-beta/v1.0/organisations").then().log().all().extract().statusCode();
	test.log(LogStatus.PASS,"Get Oraganisations Data Successful");
	if(statuscode==200)
	{
		test.log(LogStatus.PASS,"All Negotiation Data Retrived Successfully . statusCode "+statuscode);
	}
	else {
 test.log(LogStatus.FAIL,"Failed To Retrive Organizations Data .StatusCode "+statuscode);
	}
	report.endTest(test);
}
public void create_Negotiation_Id() throws IOException {
	test=report.startTest("Holacracy_Post_NegotiationId");
	String baseURI="https://ingress-gateway.gaiansolutions.com";
	String tokenEndPoint="market-place-beta/v1.0/negotiations";
	FileInputStream fis=new FileInputStream("data/testData.xlsx");
	Workbook book=new XSSFWorkbook(fis);
	Sheet sheet=book.getSheet("Sheet1");
	//String ApiRequest=sheet.getRow(3).getCell(0).getStringCellValue();
	String s=sheet.getRow(5).getCell(1).getStringCellValue();
    
	int statusCode= given().log().all().baseUri(baseURI).body(s)
			.when().post(tokenEndPoint)
             .then().log().all().extract().statusCode();	
	test.log(LogStatus.PASS,"Create Negotiation ID");
if(statusCode==200) {
	test.log(LogStatus.PASS, "Created Negotiation Successfully.statuscode "+statusCode);
}
else {
		test.log(LogStatus.FAIL,"Failed To Creat Negotiation ID.statuscode "+statusCode);
	}
		report.endTest(test);
	}
public void get_Negotiation_Data()
{
	test=report.startTest("Holacracy_Negotiation_Controller");
	String baseURI="https://ingress-gateway.gaiansolutions.com";
	String token="market-place-beta/v1.0/negotiations/48f5d0a7765f37e6e94bcdb";
	int statuscode= given().log().all().baseUri(baseURI)
			.when().get(token).then().log().all().extract().statusCode();
	test.log(LogStatus.PASS, "getNegotiation Data Successful");
	if(statuscode==200)
	{
		test.log(LogStatus.PASS,"Retrived Negotiation Data Successfully .Statuscode "+statuscode);
	}
	else
	{
		test.log(LogStatus.FAIL,"Failed To Retrive Negotiation Data .Statuscode "+statuscode);
	}
	report.endTest(test);

	
}

public void rejectNegotiationBySeller() {
	 test = report.startTest("Holacracy_GetNegotiationAccount");
	    RequestSpecification request = RestAssured.given();
	    String baseURL = "https://ingress-gateway.gaiansolutions.com";
	    String token="/market-place-beta/v1.0/negotiations/648f5d0a7765f37e6e94bcdb/approve-reject/buyer/648ef4b10476cd6d801fc555/status/ALLIANCE_BROKEN";
	 	test.log(LogStatus.PASS, "Retrieved AllUserNodes");
	    test.log(LogStatus.INFO, "Request URL: " + baseURL+token);
	 	request.baseUri(baseURL);
	 	Response response = request.get(token);
	 	response.then().log().all();
	 	int statuscode = response.getStatusCode();
	 	String responseBody = response.getBody().asString();
	 	test.log(LogStatus.INFO, "Response Body: " + responseBody);
	 	if (statuscode == 200) {
	 		test.log(LogStatus.PASS, "Retrieved GetUserNegotiation successfully statuscode:"  +statuscode);
	 		} 
	 	else 
	 	{
			test.log(LogStatus.FAIL, "Failed to Retrieved GetUserNegotiation statuscode:"   +statuscode);
	 }
	 	report.endTest(test);
	}

public void  getAllNegotiations_Between_two_parties() {
	test=report.startTest("Holacracy_GetAllNegotiationController");
	RequestSpecification request=RestAssured.given();
	String baseURl="https://ingress-gateway.gaiansolutions.com";
	String token="/market-place-beta/v1.0/negotiations/buyer/648ef4b10476cd6d801fc555/seller/648ef4840476cd6d801fc553";
	test.log(LogStatus.PASS, "Retrived Negotiation");
	test.log(LogStatus.INFO, "Request URL: "+baseURl+token);
    request.baseUri(baseURl);
    Response response=request.get(token);
    response.then().log().all();
    int statuscode=response.getStatusCode();
    String responseBody=response.getBody().asString();
    test.log(LogStatus.INFO,"Response Body: " + responseBody);
    if(statuscode==200) {
    	test.log(LogStatus.PASS,"Retrived GetAllNegotiation successfully statuscode: "+statuscode);
    }else {
    	test.log(LogStatus.FAIL,"Failed to Retrived GetUserNegotiation statuscode:"  +statuscode);
    }
    report.endTest(test);
}
		



public void regectNegotiationByBuyer()
	{
	test=report.startTest("Holacracy_NegotiationByBuyer");
	test.log(LogStatus.PASS,"Reject negotiation by Buyer");
	String baseURI="https://ingress-gateway.gaiansolutions.com";
	RequestSpecification request=RestAssured.given().baseUri(baseURI).log().all();
	request.baseUri(baseURI);
	String endPoint="/market-place-beta/v1.0/negotiations/648f5d0a7765f37e6e94bcdb/approve-reject/buyer/648ef4b10476cd6d801fc555/status/ALLIANCE_BROKEN";
	test.log(LogStatus.INFO, "Request URL: " + baseURI+endPoint);
	Response response=request.get(endPoint);
	response.then().log().all();
int statuscode=response.getStatusCode(); 
	String responseBody = response.getBody().asString();
	test.log(LogStatus.INFO, "Response Body: " + responseBody);
	if(statuscode==200)
	{
		test.log(LogStatus.PASS, "Regected Negotiation By Buyer Is Retrived Successfully.statuscode "+statuscode);
	}
	else
	{
		test.log(LogStatus.FAIL, "Failed To Retrive Regected Negotiation By Buyer .statuscode "+statuscode);
	}
	report.endTest(test);
	}
public void regectNegotiationBySuperAdmin()
	{
	test=report.startTest("Holacracy_NegotiationBySuperAdmin");
	test.log(LogStatus.PASS, "RegectNegotiationBySuperAdmin");
	String baseURI="https://ingress-gateway.gaiansolutions.com";
	String endPoint="/market-place-beta/v1.0/negotiations/648f5d0a7765f37e6e94bcdb/approve-reject/super-admin/648f00eb0476cd6d801fc559/status/INTERESTED";
	RequestSpecification request=given().baseUri(baseURI).log().all();
	test.log(LogStatus.INFO, "Request URL :"+baseURI+endPoint);
	Response response=request.get(endPoint);
	response.then().log().all();
	String responsBody=response.getBody().asString();
	test.log(LogStatus.INFO,"Response Body :"+ responsBody);
	int statuscode=response.getStatusCode();
	if(statuscode==200)
	{
		test.log(LogStatus.PASS, "Regected Negotiation By SuperAdmin Is Retrived Successfully.statuscode"+statuscode);
	}
	else
	{
		test.log(LogStatus.FAIL, "Failed To Retrive Regected Negotiation By SuperAdmin .statuscode "+statuscode);
	}
	report.endTest(test);
	}
}





