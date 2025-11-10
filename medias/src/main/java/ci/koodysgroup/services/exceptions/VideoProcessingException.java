package ci.koodysgroup.services.exceptions;

/**
 * Exception pour les erreurs de traitement de vidéos
 */
public class VideoProcessingException extends FileProcessingException {
    public VideoProcessingException(String message) {
        super(message);
    }

    public VideoProcessingException(String message, Throwable cause) {
        super(message, cause);
    }
}

