package com.example;

import Base.BaseTest;
import Base.StatusCode;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import static Base.StatusCode.NOT_FOUND;

@Epic("User Management API Tests")
@Feature("User Deletion")
public class deleteUser extends BaseTest {

    @Test
    @Story("Delete multiple users from CSV")
    @Description("Reads user IDs from CSV and deletes them via API")
    @Severity(SeverityLevel.CRITICAL)
    @Step("Deleting users based on IDs from CSV file")
    public void testDeleteUser() {
        int userIdToDelete = 7757091; // Replace with an actual user ID

        Response response = deleteUser(getRequestSpec(), userIdToDelete);

        Allure.addAttachment("Response Status Code", String.valueOf(response.statusCode()));

        // Validate response status code
        Assert.assertEquals(response.statusCode(), StatusCode.NO_CONTENT.getCode(), "User deletion failed");

        if(response.statusCode() == StatusCode.NO_CONTENT.getCode()) {
            Assert.assertEquals(response.statusCode(), StatusCode.NO_CONTENT.getCode(), "User deletion failed");
        }
        else if(response.statusCode() == StatusCode.NOT_FOUND.getCode()) {
                Assert.assertFalse(response.statusCode() == NOT_FOUND.getCode(),
                        "Response should not be an Internal Server Error (500)");
            }



        // Log response
        response.then().log().all();
    }
    @Test
    public void deleteUser(){
        System.out.println("Test");
    }
}
