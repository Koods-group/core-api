package ci.koodysgroup.services.exceptions;

/**
 * Exception pour les erreurs de validation de fichiers
 */
public class FileValidationException extends FileProcessingException {
    public FileValidationException(String message) {
        super(message);
    }

    public FileValidationException(String message, Throwable cause) {
        super(message, cause);
    }
}

