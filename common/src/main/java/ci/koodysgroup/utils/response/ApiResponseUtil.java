package ci.koodysgroup.utils.response;


import java.util.List;

public class ApiResponseUtil {

    private ApiResponseUtil() {
    }

    public static <T> ApiResponse<T> ok(T data, String message, String code) {
        return ApiResponse.ok(data, message, code);
    }

    public static <T> ApiResponse<T> error(String message, String code, Object errors) {
        return ApiResponse.error(message, code, errors);
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return ApiResponse.error(message, code);
    }

    public static <T> ApiResponse<T> success(T data) {
        return ok(data, "Operation successful", "success");
    }

    public static <T> ApiResponse<T> created(T data) {
        return ok(data, "Resource created successfully", "created");
    }

    public static <T> ApiResponse<T> notFound() {
        return error("The requested resource is not available, Please try again", "not_found");
    }

    public static <T> ApiResponse<T> badRequest(String message, Object errors) {
        return error(message, "bad_request", errors);
    }

    public static <T> ApiResponse<T> unauthorized() {
        return error("Unauthorized access", "unauthorized");
    }

    public static <T> ApiResponse<T> internalError() {
        return error("Internal server error", "internal_error");
    }
}