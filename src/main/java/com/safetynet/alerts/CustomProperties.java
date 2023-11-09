package com.safetynet.alerts;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="com.safetynet.alerts")
public class CustomProperties {

    private String apiUrl;

}
