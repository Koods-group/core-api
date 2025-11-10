package ci.koodysgroup.services.exceptions;

/**
 * Exception générique pour les erreurs de traitement de fichiers
 */
public class FileProcessingException extends RuntimeException {
    public FileProcessingException(String message) {
        super(message);
    }

    public FileProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

