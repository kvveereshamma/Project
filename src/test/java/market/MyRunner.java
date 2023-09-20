package market;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.mail.EmailException;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;

public class MyRunner {
	public static String report1;

	@Test
	public void login() throws Throwable {
		// Create an object for the Market_Login class
		Market_Login keys = new Market_Login();
		// GetUser1 gu=new GetUser1();
		// GetId1 gi=new GetId1();

		// Specify the Keywords file location
		FileInputStream fi = new FileInputStream("data/testData.xlsx");
		XSSFWorkbook wb = new XSSFWorkbook(fi);
		XSSFSheet ws = wb.getSheet("Sheet3");

		// Find the number of rows
		int rowCount = ws.getLastRowNum();
		for (int i = 1; i <= rowCount; i++) {
			XSSFRow row = ws.getRow(i);

			// Read the run mode
			String runMode = row.getCell(4).getStringCellValue();
			System.out.println(runMode);

			if (runMode.equals("Y")) {
				// Read the step description
				String keyWord = row.getCell(3).getStringCellValue();
				System.out.println(keyWord);

				switch (keyWord) {
//                    case "Iamservice":
//                        keys.Iamservice();
//                        break;
//                    case "Accounts":
//                        keys.Accounts();
//                        break;
				case "getAllAccounts":
					keys.getAllAccounts();
					break;
				case "updateAccount":
					keys.updateAccount();
					break;
				case "AccountsID":
					keys.accountsID();
                    break;
		         case "CreateAccount":
					keys.createAccount();
					break;
		         case "UpdateOrganisationAccount":
		        	 keys.updateOrganisationAccount();
		        	 break;
		         case "CreatOrganizationAccount":
		        	  keys.creatOrganizationAccount();
		        	 break;
		         case "getOrganisationId":
		              keys.getOrganisationId();
		             break;
		         case "getAllOranisationData":
		        	 keys.getAllOrganisiationData();
		        	 break;
		         case "Create_Negotiation_Id":
		        	 keys.create_Negotiation_Id();
		        	 break;
		         case "GetNegotiationData":
		        	 keys.get_Negotiation_Data();
		        	break;
		         case "RejectNegotiationBySeller":
		        	 keys.rejectNegotiationBySeller();
		        	 break;
		         case "GetAllNegotiations_Between_two_parties":
		        	 keys.getAllNegotiations_Between_two_parties();
		        	 break;
		         case "RegectNegotiationByBuyer":
		        	 keys.regectNegotiationByBuyer();
		        	 break;
		         case "RegectNegotiationBySuperAdmin":
		        	 keys.regectNegotiationBySuperAdmin();
		        	 break;

                  }
				}
		}

		// Close the workbook
		wb.close();
	}
    static ExtentTest test;
	static ExtentReports report;

	@BeforeClass
	public static void startTest() {
		Date d = new Date();
		String fileName = d.toString().replace(":", "_").replace(" ", "_") + ".html";
		report1 = "Market Place Api Automation test report_" + fileName;
		System.out.println(report1);
		report = new ExtentReports(System.getProperty("user.dir") + "" + report1);

		report = new ExtentReports(System.getProperty("user.dir") + "/" + report1);

		// test = report.startTest("MarketAPI");
		// report = new
		// ExtentReports(System.getProperty("user.dir")+"/ExtentReportResults.html");
		// test = report.startTest("Market_API");
	}

	@AfterClass
	public static void endTest() throws IOException {
		// End the test and generate the report
		report.endTest(test);
		report.flush();

//    try {
//    	SendEmail.sendTestReportEmail();
//       
//    } catch (EmailException e) {
//        e.printStackTrace();
//    }

	}
}