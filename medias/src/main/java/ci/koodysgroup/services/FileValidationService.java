package ci.koodysgroup.services;

import ci.koodysgroup.services.exceptions.FileValidationException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * Service pour la validation de fichiers et vidéos
 */
@Service
public class FileValidationService {

    // Formats vidéo acceptés
    private static final List<String> ACCEPTED_VIDEO_FORMATS = Arrays.asList(
            "video/mp4", "video/avi", "video/mkv", "video/mov", 
            "video/wmv", "video/flv", "video/webm", "video/quicktime",
            "video/x-msvideo", "video/x-matroska"
    );

    // Extensions vidéo acceptées
    private static final List<String> ACCEPTED_VIDEO_EXTENSIONS = Arrays.asList(
            ".mp4", ".avi", ".mkv", ".mov", ".wmv", ".flv", ".webm"
    );

    // Formats de fichiers généraux acceptés
    private static final List<String> ACCEPTED_FILE_FORMATS = Arrays.asList(
            "image/jpeg", "image/png", "image/gif", "image/webp",
            "application/pdf", "text/plain", "application/json"
    );

    // Extension de fichiers acceptées
    private static final List<String> ACCEPTED_FILE_EXTENSIONS = Arrays.asList(
            ".jpg", ".jpeg", ".png"
    );

    // Taille maximale pour les vidéos (100 Mo en octets)
    private static final long MAX_VIDEO_SIZE = 100L * 1024L * 1024L; // 100 Mo

    // Taille maximale pour les fichiers généraux (100 Mo)
    private static final long MAX_FILE_SIZE = 100L * 1024L * 1024L; // 100 Mo

    /**
     * Valide un fichier vidéo
     * 
     * @param file Le fichier à valider
     * @throws FileValidationException Si la validation échoue
     */
    public void validateVideo(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("Le fichier vidéo est vide ou null");
        }

        // Validation de la taille
        validateVideoSize(file.getSize());

        // Validation du type MIME
        validateVideoMimeType(file.getContentType());

        // Validation de l'extension
        validateVideoExtension(getFileExtension(file.getOriginalFilename()));

        // Validation que le fichier n'est pas corrompu
        validateFileIntegrity(file);
    }

    /**
     * Valide un fichier générique
     * 
     * @param file Le fichier à valider
     * @throws FileValidationException Si la validation échoue
     */
    public void validateFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new FileValidationException("The file is empty or null .");
        }

        // Validation de la taille
        validateFileSize(file.getSize());

        // Validation du type MIME
        validateFileMimeType(file.getContentType());

        // Validation de l'extension
        validateFileExtension(getFileExtension(file.getOriginalFilename()));

        // Validation que le fichier n'est pas corrompu
        validateFileIntegrity(file);
    }

    /**
     * Valide la taille d'une vidéo (max 100 Mo)
     */
    private void validateVideoSize(long size) {
        if (size <= 0) {
            throw new FileValidationException("The video file size is invalid .");
        }

        if (size > MAX_VIDEO_SIZE) {
            double sizeInMB = (double) size / (1024L * 1024L);
            throw new FileValidationException(
                String.format("The size of the video file (%.2f MB) exceeds the maximum limit of 100 MB .", sizeInMB)
            );
        }
    }

    /**
     * Valide la taille d'un fichier générique (max 100 Mo)
     */
    private void validateFileSize(long size) {
        if (size <= 0) {
            throw new FileValidationException("The file size is invalid .");
        }

        if (size > MAX_FILE_SIZE) {
            double sizeInMB = (double) size / (1024L * 1024L);
            throw new FileValidationException(
                String.format("The file size (%.2f MB) exceeds the maximum limit of 100 MB .", sizeInMB)
            );
        }
    }

    /**
     * Valide le type MIME d'une vidéo
     */
    private void validateVideoMimeType(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            throw new FileValidationException("The MIME type of the video cannot be found .");
        }

        String normalizedMimeType = mimeType.toLowerCase(Locale.ROOT);
        boolean isValid = ACCEPTED_VIDEO_FORMATS.stream()
                .anyMatch(format -> normalizedMimeType.contains(format.toLowerCase(Locale.ROOT)));

        if (!isValid) {
            throw new FileValidationException(
                String.format("The MIME type ‘%s’ is not supported for videos . Accepted types: %s .",
                    mimeType, String.join(", ", ACCEPTED_VIDEO_FORMATS))
            );
        }
    }

    /**
     * Valide le type MIME d'un fichier générique
     */
    private void validateFileMimeType(String mimeType) {
        if (mimeType == null || mimeType.isEmpty()) {
            throw new FileValidationException("The MIME type of the file cannot be found .");
        }

        String normalizedMimeType = mimeType.toLowerCase(Locale.ROOT);
        boolean isValid = ACCEPTED_FILE_FORMATS.stream()
                .anyMatch(format -> normalizedMimeType.contains(format.toLowerCase(Locale.ROOT)));

        if (!isValid) {
            throw new FileValidationException(
                String.format("The MIME type ‘%s’ is not supported . Accepted types: %s .",
                    mimeType, String.join(", ", ACCEPTED_FILE_FORMATS))
            );
        }
    }

    /**
     * Valide l'extension d'une vidéo
     */
    private void validateVideoExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            throw new FileValidationException("The video file extension cannot be found .");
        }

        String normalizedExtension = extension.toLowerCase(Locale.ROOT);
        if (!ACCEPTED_VIDEO_EXTENSIONS.contains(normalizedExtension)) {
            throw new FileValidationException(
                String.format("The extension ‘%s’ is not supported for videos . Accepted extensions: %s .",
                    extension, String.join(", ", ACCEPTED_VIDEO_EXTENSIONS))
            );
        }
    }

    /**
     * Valide l'extension d'un fichier générique
     */
    private void validateFileExtension(String extension) {
        if (extension == null || extension.isEmpty()) {
            throw new FileValidationException("The file extension cannot be found .");
        }

        String normalizedExtension = extension.toLowerCase(Locale.ROOT);
        if (!ACCEPTED_FILE_EXTENSIONS.contains(normalizedExtension)) {
            throw new FileValidationException(
                String.format("The extension ‘%s’ is not supported . Accepted extensions: %s .",
                    extension, String.join(", ", ACCEPTED_FILE_EXTENSIONS))
            );
        }
    }

    /**
     * Valide l'intégrité du fichier (vérifie qu'il n'est pas corrompu)
     */
    private void validateFileIntegrity(MultipartFile file) {
        try {
            // Vérifie que le fichier peut être lu
            byte[] bytes = file.getBytes();
            if (bytes == null || bytes.length == 0) {
                throw new FileValidationException("The file is corrupted or empty .");
            }

            // Vérifie que la taille réelle correspond à la taille déclarée
            if (bytes.length != file.getSize()) {
                throw new FileValidationException("The actual file size does not match the declared size .");
            }
        } catch (IOException e) {
            throw new FileValidationException("Error reading file : " + e.getMessage(), e);
        }
    }

    /**
     * Extrait l'extension d'un nom de fichier
     */
    private String getFileExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return "";
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return "";
        }

        return filename.substring(lastDotIndex).toLowerCase(Locale.ROOT);
    }

    /**
     * Obtient la taille maximale autorisée pour les vidéos (en octets)
     */
    public static long getMaxVideoSize() {
        return MAX_VIDEO_SIZE;
    }

    /**
     * Obtient la taille maximale autorisée pour les fichiers (en octets)
     */
    public static long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }

    /**
     * Vérifie si un fichier est une vidéo basé sur son type MIME
     */
    public boolean isVideoFile(MultipartFile file) {
        if (file == null || file.getContentType() == null) {
            return false;
        }

        String mimeType = file.getContentType().toLowerCase(Locale.ROOT);
        return ACCEPTED_VIDEO_FORMATS.stream()
                .anyMatch(format -> mimeType.contains(format.toLowerCase(Locale.ROOT)));
    }
}
