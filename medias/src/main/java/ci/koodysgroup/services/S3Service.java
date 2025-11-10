package ci.koodysgroup.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class S3Service {

    private static final Map<String, String> CONTENT_TYPE_EXTENSIONS = new HashMap<>();

    static {
        CONTENT_TYPE_EXTENSIONS.put("image/jpeg", ".jpg");
        CONTENT_TYPE_EXTENSIONS.put("image/png", ".png");
        /*CONTENT_TYPE_EXTENSIONS.put("image/gif", ".gif");
        CONTENT_TYPE_EXTENSIONS.put("image/webp", ".webp");
        CONTENT_TYPE_EXTENSIONS.put("application/pdf", ".pdf");
        CONTENT_TYPE_EXTENSIONS.put("text/plain", ".txt");
        CONTENT_TYPE_EXTENSIONS.put("application/json", ".json"); */
    }

    @Autowired
    private S3Client client;

    @Value("${r2-bucket-name}")
    private String bucket;

    public String toUpload(MultipartFile file, String folder) throws IOException {
        String fileName = generateUniqueFileName(file);

        client.putObject(
                PutObjectRequest.builder()
                        .bucket(bucket)
                        .key(folder + "/" + fileName)
                        .contentType(file.getContentType())
                        .build(),
                RequestBody.fromBytes(file.getBytes())
        );

        return fileName;
    }

    public void toDelete(String key) throws IOException {

        client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucket)
                        .key(key)
                        .build()
        );
    }

    private String generateUniqueFileName(MultipartFile file) {
        String extension = resolveExtension(file.getContentType());
        return System.currentTimeMillis() + "_" + UUID.randomUUID().toString().replace("-", "") + extension;
    }

    private String resolveExtension(String contentType) {
        if (contentType != null) {
            String normalizedContentType = contentType.toLowerCase();
            if (CONTENT_TYPE_EXTENSIONS.containsKey(normalizedContentType)) {
                return CONTENT_TYPE_EXTENSIONS.get(normalizedContentType);
            }
        }

        return "";
    }
}
