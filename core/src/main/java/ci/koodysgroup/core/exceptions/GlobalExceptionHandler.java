package ci.koodysgroup.core.exceptions;

import ci.koodysgroup.utils.response.ApiResponse;
import ci.koodysgroup.utils.response.ApiResponseUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGlobalException(Exception ex) {
        ApiResponse<Object> response = ApiResponseUtil.error(
                "Internal Error",
                "An unexpected error occurred: " + ex.getMessage(),
                "internal_error"
        );
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFound(NoHandlerFoundException ex) {
        ApiResponse<Object> response = ApiResponseUtil.notFound();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    // Gestion des exceptions de traitement de fichiers/vidéos
    @ExceptionHandler({
        ci.koodysgroup.services.exceptions.FileProcessingException.class,
        ci.koodysgroup.services.exceptions.FileValidationException.class,
        ci.koodysgroup.services.exceptions.VideoProcessingException.class
    })
    public ResponseEntity<ApiResponse<Object>> handleFileProcessingException(RuntimeException ex) {
        ApiResponse<Object> response = ApiResponseUtil.badRequest(
                "File Processing Error",
                ex.getMessage(),
                "file_processing_error"
        );
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
}