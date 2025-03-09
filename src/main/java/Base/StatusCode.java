package Base;

public enum StatusCode {

    OK(200, "OK"),
    CREATED(201, "Created"),
    NO_CONTENT(204, "No Content"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    INTERNAL_SERVER_ERROR(500, "Internal Server Error");

    private final int code;
    private final String message;

    // Constructor
    StatusCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    // Getter for status code
    public int getCode() {
        return code;
    }

    // Getter for status message
    public String getMessage() {
        return message;
    }

    // Get enum from code
    public static StatusCode fromCode(int code) {
        for (StatusCode status : StatusCode.values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown HTTP Status Code: " + code);
    }
}
