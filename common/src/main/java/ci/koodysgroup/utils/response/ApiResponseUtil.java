package ci.koodysgroup.utils.response;


import java.util.List;

public class ApiResponseUtil {

    private ApiResponseUtil() {
    }

    public static <T> ApiResponse<T> ok(String title, T data, String message, String code) {
        return ApiResponse.ok(title, data, message, code);
    }

    public static <T> ApiResponse<T> error(String title, String message, String code, Object errors) {
        return ApiResponse.error(title, message, code, errors);
    }

    public static <T> ApiResponse<T> error(String title, String message, String code) {
        return ApiResponse.error(title, message, code);
    }

    public static <T> ApiResponse<T> success(String title, T data) {
        return ok(title, data, "Operation successful", "success");
    }

    public static <T> ApiResponse<T> created(String title, T data) {
        return ok(title, data, "Resource created successfully", "created");
    }

    public static <T> ApiResponse<T> notFound() {
        return error("Resource not non-existent", "The requested resource is not available, Please try again", "not_found");
    }

    public static <T> ApiResponse<T> notFound(String message) {
        return error("Resource not non-existent", message, "not_found");
    }

    public static <T> ApiResponse<T> conflict(String title, String message) {
        return error(title, message, "conflict");
    }

    public static <T> ApiResponse<T> badRequest(String title, String message, Object errors) {
        return error(title, message, "bad_request", errors);
    }

    public static <T> ApiResponse<T> badRequest(String title, String message) {
        return error(title, message, "bad_request");
    }

    public static <T> ApiResponse<T> unauthorized(String title, String message) {
        return error(title, message, "unauthorized");
    }

    public static <T> ApiResponse<T> internalError() {
        return error("Error", "Internal server error", "internal_error");
    }

    public static <T> ApiResponse<T> internalError(String message) {
        return error("Error", message, "internal_error");
    }
}