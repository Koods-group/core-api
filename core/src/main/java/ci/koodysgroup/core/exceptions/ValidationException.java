package ci.koodysgroup.core.exceptions;

import ci.koodysgroup.utils.response.ApiResponse;
import ci.koodysgroup.utils.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ValidationException {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, Object>>> handleValidationExceptions(MethodArgumentNotValidException ex){
        Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        fieldError -> fieldError.getDefaultMessage(),
                        (msg1, msg2) -> msg1
                ));

        ApiResponse<Map<String, Object>> response = ApiResponseUtil.badRequest("Invalid data", "The data in the request is invalid, Please try again ." , errors);

        return new ResponseEntity<>(response ,HttpStatus.BAD_REQUEST);
    }
}
