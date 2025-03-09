package com.example;

import Base.BaseTest;
import Base.StatusCode;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;
import java.io.BufferedReader;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class addListUser extends BaseTest {

    @Test
    public void testGetUsers() {
        Response response = listUser(getRequestSpec());

        // Print response
        response.then().log().all();

        // Validate Status Code
        //response.then().statusCode(200);
        Assert.assertEquals(response.statusCode(), StatusCode.OK.getCode());

        System.out.println("Response Message: " + StatusCode.OK.getMessage());

        // Validate Response Body (Ensure at least one user exists)
        Assert.assertFalse(response.jsonPath().getList("").isEmpty(), "User list should not be empty");

    }

    private static final String CSV_FILE_PATH = "src/test/resources/user_ids.csv";

    @Test
    public void testDeleteUsersFromCSV() {
        // Read user IDs from CSV file
        List<Integer> userIds = readUserIdsFromCSV(CSV_FILE_PATH);

        // Iterate and delete users
        for (int userId : userIds) {
            Response response = deleteUser(getRequestSpec(), userId);

            // Validate response status code (204 No Content)
            Assert.assertEquals(response.statusCode(), StatusCode.NO_CONTENT.getCode(),
                    "Failed to delete user with ID: " + userId);

            // Log response
            System.out.println("Deleted user with ID: " + userId);
        }
    }

    // Method to read user IDs from CSV file
    private List<Integer> readUserIdsFromCSV(String filePath) {
        List<Integer> userIds = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false; // Skip header line
                    continue;
                }
                userIds.add(Integer.parseInt(line.trim()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read CSV file: " + filePath, e);
        }

        return userIds;
    }


}
