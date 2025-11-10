package ci.koodysgroup.services;

import ci.koodysgroup.services.exceptions.FileProcessingException;
import ci.koodysgroup.services.exceptions.FileValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
 * Service pour le traitement de fichiers généraux
 */
@Service
public class FileProcessingService {

    @Autowired
    private FileValidationService fileValidationService;

    @Value("${file.upload.directory:uploads/files}")
    private String uploadDirectory;

    /**
     * Traite et sauvegarde un fichier
     * 
     * @param file Le fichier à traiter
     * @return Le chemin du fichier sauvegardé
     * @throws FileProcessingException Si le traitement échoue
     * @throws FileValidationException Si la validation échoue
     */
    public String processAndSaveFile(MultipartFile file) {
        // Validation du fichier
        fileValidationService.validateFile(file);

        try {
            // Création du répertoire s'il n'existe pas
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Génération d'un nom de fichier unique
            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String uniqueFilename = generateUniqueFilename(extension);
            String relativePath = getSubdirectoryPath(extension);

            // Création du sous-répertoire selon le type de fichier
            Path subdirectory = uploadPath.resolve(relativePath);
            if (!Files.exists(subdirectory)) {
                Files.createDirectories(subdirectory);
            }

            // Sauvegarde du fichier
            Path filePath = subdirectory.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            // Retourne le chemin relatif du fichier
            return Paths.get(relativePath).resolve(uniqueFilename).toString().replace("\\", "/");

        } catch (IOException e) {
            throw new FileProcessingException("Error while saving the file : " + e.getMessage(), e);
        }
    }

    /**
     * Traite et sauvegarde un fichier avec un nom personnalisé
     * 
     * @param file Le fichier à traiter
     * @param customName Le nom personnalisé (sans extension)
     * @return Le chemin du fichier sauvegardé
     */
    public String processAndSaveFileWithCustomName(MultipartFile file, String customName) {
        fileValidationService.validateFile(file);

        try {
            Path uploadPath = Paths.get(uploadDirectory);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            String originalFilename = file.getOriginalFilename();
            String extension = getFileExtension(originalFilename);
            String sanitizedCustomName = sanitizeFilename(customName);
            String uniqueFilename = sanitizedCustomName + "_" + UUID.randomUUID().toString().substring(0, 8) + extension;
            String relativePath = getSubdirectoryPath(extension);

            Path subdirectory = uploadPath.resolve(relativePath);
            if (!Files.exists(subdirectory)) {
                Files.createDirectories(subdirectory);
            }

            Path filePath = subdirectory.resolve(uniqueFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            return Paths.get(relativePath).resolve(uniqueFilename).toString().replace("\\", "/");

        } catch (IOException e) {
            throw new FileProcessingException("Error while saving the file : " + e.getMessage(), e);
        }
    }

    /**
     * Supprime un fichier
     * 
     * @param filePath Le chemin relatif du fichier à supprimer
     * @return true si le fichier a été supprimé, false sinon
     */
    public boolean deleteFile(String filePath) {
        try {
            Path fullPath = Paths.get(uploadDirectory, filePath);
            if (Files.exists(fullPath)) {
                Files.delete(fullPath);
                return true;
            }
            return false;
        } catch (IOException e) {
            throw new FileProcessingException("Error deleting file : " + e.getMessage(), e);
        }
    }

    /**
     * Vérifie si un fichier existe
     * 
     * @param filePath Le chemin relatif du fichier
     * @return true si le fichier existe, false sinon
     */
    public boolean fileExists(String filePath) {
        Path fullPath = Paths.get(uploadDirectory, filePath);
        return Files.exists(fullPath);
    }

    /**
     * Obtient la taille d'un fichier
     * 
     * @param filePath Le chemin relatif du fichier
     * @return La taille du fichier en octets, ou -1 si le fichier n'existe pas
     */
    public long getFileSize(String filePath) {
        try {
            Path fullPath = Paths.get(uploadDirectory, filePath);
            if (Files.exists(fullPath)) {
                return Files.size(fullPath);
            }
            return -1;
        } catch (IOException e) {
            throw new FileProcessingException("Error reading file size : " + e.getMessage(), e);
        }
    }

    /**
     * Génère un nom de fichier unique
     */
    private String generateUniqueFilename(String extension) {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String uuid = UUID.randomUUID().toString().substring(0, 8);
        return timestamp + "_" + uuid + extension;
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
     * Obtient le sous-répertoire selon le type de fichier
     */
    private String getSubdirectoryPath(String extension) {
        extension = extension.toLowerCase(Locale.ROOT);

        if (extension.matches("\\.(jpg|jpeg|png|gif|webp)")) {
            return "images";
        } else if (extension.equals(".pdf")) {
            return "documents";
        } else if (extension.matches("\\.(txt|json)")) {
            return "text";
        } else {
            return "others";
        }
    }

    /**
     * Obtient le chemin complet du fichier
     * 
     * @param relativePath Le chemin relatif du fichier
     * @return Le chemin complet
     */
    public Path getFullFilePath(String relativePath) {
        return Paths.get(uploadDirectory, relativePath);
    }
}

