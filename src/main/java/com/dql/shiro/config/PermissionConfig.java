package com.dql.shiro.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "permission-config")
@Data
public class PermissionConfig {

    private List<Map<String, String>> perms;

}
