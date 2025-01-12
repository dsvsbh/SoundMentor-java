package com.soundmentor.soundmentorweb.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "organization")
@Data
public class OrganizationProperties {
    private Integer defaultOrganizationCapacity;
    private Integer MaxOrganizations;
}
