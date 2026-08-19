package com.kawevk.vkurso.shared.storage;

import io.minio.MinioClient;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(StorageProperties.class)
public class MinioConfig {

    @Bean
    @Qualifier("internalMinioClient")
    public MinioClient internalMinioClient(StorageProperties props) {
        return MinioClient.builder()
                .endpoint(props.endpoint())
                .credentials(
                        props.accessKey(),
                        props.secretKey()
                )
                .build();
    }

    @Bean
    @Qualifier("publicMinioClient")
    public MinioClient publicMinioClient(StorageProperties props) {
        return MinioClient.builder()
                .endpoint(props.publicEndpoint())
                .credentials(
                        props.accessKey(),
                        props.secretKey()
                )
                .build();
    }
}