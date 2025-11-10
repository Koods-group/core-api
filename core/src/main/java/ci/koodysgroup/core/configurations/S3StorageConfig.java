package ci.koodysgroup.core.configurations;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import java.net.URI;

@Configuration
public class S3StorageConfig {

    @Value("${r2-access-key-id}")
    private String accessKey;
    @Value("${r2-secret-access-key}")
    private String secretKey;
    @Value("${r2-endpoint}")
    private String endpointUrl;

    @Bean
    public S3Client ClientS3() {
        return S3Client.builder()
                .region(Region.of("auto"))
                .credentialsProvider(
                        StaticCredentialsProvider.create(
                                AwsBasicCredentials.create(accessKey, secretKey)
                        )
                )
                .endpointOverride(URI.create(endpointUrl))
                .build();
    }

}
