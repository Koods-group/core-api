package ci.koodysgroup.utils.response;

import ci.koodysgroup.interfaces.IApiResponse;
import com.fasterxml.jackson.annotation.JsonInclude;


@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements IApiResponse {

    private T content;
    private String message;
    private boolean success;
    private Object errors;
    private String code;

    public ApiResponse() {}

    public ApiResponse(T content, String message, boolean success, String code) {
        this.content = content;
        this.message = message;
        this.success = success;
        this.code = code;
    }

    public ApiResponse(T content, String message, boolean success, String code, Object errors) {
        this.content = content;
        this.message = message;
        this.success = success;
        this.code = code;
        this.errors = errors;
    }

    // Getters et Setters
    public T getContent() {
        return content;
    }

    public void setContent(T content) {
        this.content = content;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public Object getErrors() {
        return errors;
    }

    public void setErrors(Object errors) {
        this.errors = errors;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }


    public static <T> ApiResponse<T> ok(T data, String message, String code) {
        return new ApiResponse<>(data, message, true, code);
    }

    public static <T> ApiResponse<T> error(String message, String code, Object errors) {
        return new ApiResponse<>(null, message, false, code, errors);
    }

    public static <T> ApiResponse<T> error(String message, String code) {
        return new ApiResponse<>(null, message, false, code);
    }


}
