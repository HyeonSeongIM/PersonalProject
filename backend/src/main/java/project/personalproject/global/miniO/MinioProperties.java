package project.personalproject.global.miniO;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "minio")
public class MinioProperties {
    private String url;

    private String accessKey;

    private String secretKey;

    private Bucket bucket = new Bucket();

    @Getter
    @Setter
    public static class Bucket {
        private String name;
    }
}