package ci.koodysgroup.services;

import ci.koodysgroup.services.exceptions.FileValidationException;
import ci.koodysgroup.services.exceptions.VideoProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.UUID;

/**
 * Service pour le traitement de fichiers vidéo
 * Taille maximale: 1 Go
 */
@Service
public class VideoProcessingService {

    @Autowired
    private FileValidationService fileValidationService;

    @Value("${video.upload.directory:uploads/videos}")
    private String uploadDirectory;

    // Taille maximale de 1 Go (en octets)
    private static final long MAX_VIDEO_SIZE = FileValidationService.getMaxVideoSize();

    /**
     * Traite et sauvegarde un fichier vidéo
     * 
     * @param file Le fichier vidéo à traiter
     * @return Le chemin du fichier vidéo sauvegardé
     * @throws VideoProcessingException Si le traitement échoue
     * @throws FileValidationException Si la validation échoue
     */
    public String processAndSaveVideo(MultipartFile file) {
        // Validation spécifique pour les vidéos
        fileValidationService.validateVideo(file);

        // Vérification supplémentaire de la taille
        if (file.getSize() > MAX_VIDEO_SIZE) {
            double sizeInGB = (double) file.getSize() / (1024L * 1024L * 1024L);
            throw new FileValidationException(
                String.format("The video (%.2f GB) exceeds the maximum limit of 1 GB .", sizeInGB)
            );
        }

        try {
            // Création du répertoire s'il n'existe pas
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Génération d'un nom de fichier unique
            String originalFilename = file.getOriginalFilename();
            String extension = getVideoExtension(originalFilename);
            String uniqueFilename = generateUniqueVideoFilename(extension);

            // Création des sous-répertoires par date (yyyy/MM) pour organiser les vidéos
            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            Path dateSubdirectory = uploadPath.resolve(datePath);
            if (!Files.exists(dateSubdirectory)) {
                Files.createDirectories(dateSubdirectory);
            }

            // Sauvegarde du fichier vidéo
            Path videoPath = dateSubdirectory.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);

            // Vérification post-upload que le fichier a bien été sauvegardé
            if (!Files.exists(videoPath)) {
                throw new VideoProcessingException("The video file could not be saved correctly .");
            }

            // Vérification de la taille du fichier sauvegardé
            long savedFileSize = Files.size(videoPath);
            if (savedFileSize != file.getSize()) {
                // Nettoyage en cas d'erreur
                Files.deleteIfExists(videoPath);
                throw new VideoProcessingException(
                    "The size of the saved file does not match the original size ."
                );
            }

            // Retourne le chemin relatif du fichier vidéo
            return Paths.get(datePath).resolve(uniqueFilename).toString().replace("\\", "/");

        } catch (IOException e) {
            throw new VideoProcessingException(
                "Error while saving the video : " + e.getMessage(), e
            );
        }
    }

    /**
     * Traite et sauvegarde un fichier vidéo avec un nom personnalisé
     * 
     * @param file Le fichier vidéo à traiter
     * @param customName Le nom personnalisé (sans extension)
     * @return Le chemin du fichier vidéo sauvegardé
     */
    public String processAndSaveVideoWithCustomName(MultipartFile file, String customName) {
        fileValidationService.validateVideo(file);

        if (file.getSize() > MAX_VIDEO_SIZE) {
            double sizeInGB = (double) file.getSize() / (1024L * 1024L * 1024L);
            throw new FileValidationException(
                String.format("The video (%.2f GB) exceeds the maximum limit of 1 GB .", sizeInGB)
            );
        }

        try {
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getVideoExtension(originalFilename);
            String sanitizedCustomName = sanitizeFilename(customName);
            String uniqueFilename = sanitizedCustomName + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;

            String datePath = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM"));
            Path dateSubdirectory = uploadPath.resolve(datePath);
            if (!Files.exists(dateSubdirectory)) {
                Files.createDirectories(dateSubdirectory);
            }

            Path videoPath = dateSubdirectory.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), videoPath, StandardCopyOption.REPLACE_EXISTING);

            // Vérification post-upload
            if (!Files.exists(videoPath)) {
                throw new VideoProcessingException("The video file could not be saved correctly .");
            }

            long savedFileSize = Files.size(videoPath);
            if (savedFileSize != file.getSize()) {
                Files.deleteIfExists(videoPath);
                throw new VideoProcessingException(
                    "The size of the saved file does not match the original size ."
                );
            }

            return Paths.get(datePath).resolve(uniqueFilename).toString().replace("\\", "/");

        } catch (IOException e) {
            throw new VideoProcessingException(
                "Error while saving the video : " + e.getMessage(), e
            );
        }
    }

    /**
     * Supprime un fichier vidéo
     * 
     * @param videoPath Le chemin relatif du fichier vidéo à supprimer
     * @return true si le fichier a été supprimé, false sinon
     */
    public boolean deleteVideo(String videoPath) {
        try {
            Path fullPath = Paths.get(uploadDirectory, videoPath);
            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new VideoProcessingException("Error while deleting the video : " + e.getMessage(), e);
        }
    }

    /**
     * Vérifie si un fichier vidéo existe
     * 
     * @param videoPath Le chemin relatif du fichier vidéo
     * @return true si le fichier existe, false sinon
     */
    public boolean videoExists(String videoPath) {
        Path fullPath = Paths.get(uploadDirectory, videoPath);
        return Files.exists(fullPath);
    }

    /**
     * Obtient la taille d'un fichier vidéo
     * 
     * @param videoPath Le chemin relatif du fichier vidéo
     * @return La taille du fichier en octets, ou -1 si le fichier n'existe pas
     */
    public long getVideoSize(String videoPath) {
        try {
            Path fullPath = Paths.get(uploadDirectory, videoPath);
            if (Files.exists(fullPath)) {
                return Files.size(fullPath);
            }
            return -1;
        } catch (IOException e) {
            throw new VideoProcessingException(
                "Error reading video size : " + e.getMessage(), e
            );
        }
    }

    /**
     * Vérifie si un fichier vidéo est valide après sauvegarde
     * 
     * @param videoPath Le chemin relatif du fichier vidéo
     * @return true si le fichier est valide, false sinon
     */
    public boolean validateSavedVideo(String videoPath) {
        try {
            Path fullPath = Paths.get(uploadDirectory, videoPath);
            if (!Files.exists(fullPath)) {
                return false;
            }

            long fileSize = Files.size(fullPath);
            if (fileSize <= 0 || fileSize > MAX_VIDEO_SIZE) {
                return false;
            }

            // Vérification que le fichier n'est pas vide
            return fileSize > 1024; // Au moins 1 Ko

        } catch (IOException e) {
            return false;
        }
    }

    /**
     * Génère un nom de fichier vidéo unique
     */
    private String generateUniqueVideoFilename(String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return "video_" + timestamp + "_" + uuid + extension;
    }

    /**
     * Extrait l'extension d'un fichier vidéo
     */
    private String getVideoExtension(String filename) {
        if (filename == null || filename.isEmpty()) {
            return ".mp4"; // Extension par défaut
        }

        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1 || lastDotIndex == filename.length() - 1) {
            return ".mp4";
        }

        return filename.substring(lastDotIndex).toLowerCase(Locale.ROOT);
    }

    /**
     * Nettoie le nom de fichier (supprime les caractères spéciaux)
     */
    private String sanitizeFilename(String filename) {
        if (filename == null) {
            return "";
        }

        return filename.replaceAll("[^a-zA-Z0-9._-]", "_")
                      .replaceAll("_{2,}", "_")
                      .toLowerCase(Locale.ROOT);
    }

    /**
     * Obtient le chemin complet du fichier vidéo
     * 
     * @param relativePath Le chemin relatif du fichier vidéo
     * @return Le chemin complet
     */
    public Path getFullVideoPath(String relativePath) {
        return Paths.get(uploadDirectory, relativePath);
    }

    /**
     * Obtient la taille maximale autorisée pour les vidéos
     * 
     * @return La taille maximale en octets
     */
    public static long getMaxVideoSize() {
        return MAX_VIDEO_SIZE;
    }
}

