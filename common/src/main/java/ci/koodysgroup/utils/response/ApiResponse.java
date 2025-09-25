package ci.koodysgroup.utils.response;

import ci.koodysgroup.interfaces.IApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements IApiResponse {

    // Getters et Setters
    private T content;
    private String title;
    private String message;
    private boolean success;
    private Object errors;
    private String code;

    public ApiResponse() {}

    public ApiResponse(String title,T content, String message, boolean success, String code) {
        this.content = content;
        this.title = title;
        this.message = message;
        this.success = success;
        this.code = code;
    }

    public ApiResponse(String title, T content, String message, boolean success, String code, Object errors) {
        this.content = content;
        this.title = title;
        this.message = message;
        this.success = success;
        this.code = code;
        this.errors = errors;
    }


    public static <T> ApiResponse<T> ok(String title, T data, String message, String code) {
        return new ApiResponse<>(title, data, message, true, code);
    }

    public static <T> ApiResponse<T> error(String title, String message, String code, Object errors) {
        return new ApiResponse<>(title,null, message, false, code, errors);
    }

    public static <T> ApiResponse<T> error(String title, String message, String code) {
        return new ApiResponse<>(title, null, message, false, code);
    }


}
