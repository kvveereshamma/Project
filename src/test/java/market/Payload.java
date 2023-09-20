package market;

import static io.restassured.RestAssured.given;

import com.github.cliftonlabs.json_simple.JsonObject;
import com.relevantcodes.extentreports.LogStatus;

import io.restassured.RestAssured;
import io.restassured.response.Response;


public class Payload  {
	public static String updateAccount()
	{
		
		return "{\r\n"
				+ "    \"accountHolderName\": \"Citizen Bank\"\r\n"
				+ "\r\n"
				+ "}";
	}
		public static String CreateAccount(){
		return "{\r\n"
		       + "    \"accountHolderName\": \"Exception DEMO\",\r\n"
		       + "    \"accountNum\": \"0758754951521\",\r\n"
               + "    \"accountType\": \"COMPANY\"\r\n"
		       + "}";

}
	}


