package Base;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public interface RefLink {



    Response listUser(RequestSpecification request);
    Response addUser(RequestSpecification request, String name, String gender, String email, String status);
    Response deleteUser(RequestSpecification request, int userId);
}
