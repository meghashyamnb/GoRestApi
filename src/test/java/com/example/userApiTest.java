package com.example;

import Base.BaseTest;
import Base.StatusCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import com.github.javafaker.Faker;


public class userApiTest extends BaseTest {

    private static final String CSV_FILE_PATH = "src/test/resources/user_ids.csv"; //csv file
    private static final String JSON_FILE_PATH = "src/test/resources/users1.json"; // JSON file path

    @Test
    public void testGetUsers() {
        Response response = listUser(getRequestSpec());

        // Print response
        response.then().log().all();

        // Validate Status Code
        //response.then().statusCode(200);
        Assert.assertEquals(response.statusCode(), StatusCode.OK.getCode());

        // Validate Response Body (Ensure at least one user exists)
        //Assert.assertFalse(response.jsonPath().getList("").isEmpty(), "User list should not be empty");
        Assert.assertFalse(response.statusCode() == StatusCode.INTERNAL_SERVER_ERROR.getCode(),
                "Response should not be an Internal Server Error (500)");
    }



    @Test
    public void testCaptureAllUserIds() {
        Response response = listUser(getRequestSpec());
        // Validate response status
        response.then().statusCode(200);
        // Extract all IDs from JSON response
        List<Integer> userIds = response.jsonPath().getList("id");
        // Print all IDs
        System.out.println("Captured User IDs: " + userIds);
        // Validate that we received at least one ID
        Assert.assertFalse(userIds.isEmpty(), "User ID list should not be empty");
        // Store IDs in CSV file
        saveIdsToCSV(userIds);
    }

    // Method to save IDs to CSV file
    private void saveIdsToCSV(List<Integer> userIds) {
        File file = new File(CSV_FILE_PATH);
        try (FileWriter writer = new FileWriter(file)) {
            writer.append("User ID\n"); // CSV Header
            for (Integer id : userIds) {
                writer.append(id.toString()).append("\n");
            }
            writer.flush();
            System.out.println(" User IDs saved to: " + CSV_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            Assert.fail("Failed to write user IDs to CSV file.");
        }
    }


    @Test
    public void testCreateMultipleUsers() {
        List<Map<String, String>> userList = generateUsers(10);

        // Save generated users to a JSON file
        saveUsersToJson(userList);

        // Send API requests for each user
        for (Map<String, String> user : userList) {
            Response response = addUser(
                    getRequestSpec(),
                    user.get("name"),
                    user.get("gender"),
                    user.get("email"),
                    user.get("status")
            );

            // Print response for each user
            response.then().log().all();

            // Validate status code
            //response.then().statusCode(201);
            Assert.assertEquals(response.statusCode(), StatusCode.CREATED.getCode());
        }
    }

    // Generate 20 unique users
    private List<Map<String, String>> generateUsers(int count) {
        List<Map<String, String>> users = new ArrayList<>();
        Faker faker = new Faker();

        for (int i = 0; i < count; i++) {
            Map<String, String> user = new HashMap<>();
            user.put("name", faker.name().fullName() + i); // Unique name
            user.put("gender", (i % 2 == 0) ? "male" : "female"); // Alternating gender
            user.put("email", "user" + i + "@test" + UUID.randomUUID().toString().substring(0, 5) + ".com"); // Unique email
            user.put("status", "active");

            users.add(user);
        }

        return users;
    }

    // Save generated users to a JSON file
    private void saveUsersToJson(List<Map<String, String>> users) {
        File file = new File(JSON_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, users);
            System.out.println("Users saved to JSON file: " + JSON_FILE_PATH);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Failed to save users to JSON file.");
        }
    }






}
