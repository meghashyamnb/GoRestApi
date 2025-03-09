package Base;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseTest implements RefLink {

    private static final String TOKEN = "ae00394692c50bbc5bae7f4d032a9326c61cf2a6def5d3a53919c7f6bd46129d";
    private static final String BASE_URI = "https://gorest.co.in";
    private static final String USERS_ENDPOINT = "public/v2/users";
    protected static ExtentReports extentReports;
    protected static ExtentTest extentTest;
    protected static final Logger logger = LogManager.getLogger(BaseTest.class);

    public BaseTest() {
        RestAssured.baseURI = BASE_URI;
    }
    @BeforeSuite
    public void setUpReporting() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter("target/extent-report.html");
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        logger.info("Extent Reports Initialized");
    }
    @AfterSuite
    public void tearDownReporting() {
        extentReports.flush();
        logger.info("Extent Reports Generated");
    }
    // Common Request Specification with Authentication
    protected RequestSpecification getRequestSpec() {
        return RestAssured.given()
                .filter(new AllureRestAssured())
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + TOKEN)
                .log().all();
    }
    // Implementing the getUsers() method
    @Override
    public Response listUser(RequestSpecification request) {
        return request.when().get(USERS_ENDPOINT);
    }
    @Override
    public Response addUser(RequestSpecification request, String name, String gender, String email, String status) {
        // Creating request payload dynamically
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("gender", gender);
        requestBody.put("email", email);
        requestBody.put("status", status);

        // Sending POST request with JSON body
        return request.body(requestBody)
                .when().post(USERS_ENDPOINT);
    }
    @Override
    public Response deleteUser(RequestSpecification request, int userId){
        return request.when().delete(USERS_ENDPOINT + "/" + userId);
    }



}
