package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class DataSourceConfig {

    @Value("${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5432/ekonomisti}")
    private String dataSourceUrl;

    @Value("${SPRING_DATASOURCE_USERNAME:postgres}")
    private String username;

    @Value("${SPRING_DATASOURCE_PASSWORD:bazepodataka}")
    private String password;

    @Bean
    @Primary
    public DataSource dataSource() {
        String jdbcUrl = dataSourceUrl;
        String dbUsername = username;
        String dbPassword = password;
        
        System.out.println("=== DataSourceConfig: Starting configuration ===");
        System.out.println("=== DataSourceConfig: Original URL = " + (jdbcUrl != null ? jdbcUrl : "NULL"));
        System.out.println("=== DataSourceConfig: Original username = " + (dbUsername != null ? dbUsername : "NULL"));
        System.out.println("=== DataSourceConfig: Password provided = " + (dbPassword != null && !dbPassword.isEmpty()));
        
        // If URL is null or empty, use default
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            System.err.println("=== DataSourceConfig: ERROR - URL is null or empty!");
            jdbcUrl = "jdbc:postgresql://localhost:5432/ekonomisti";
        }
        
    
        if (jdbcUrl.startsWith("postgresql://")) {
            try {
              
                String urlWithoutScheme = jdbcUrl.substring("postgresql://".length());
                
             
                int atIndex = urlWithoutScheme.indexOf('@');
                if (atIndex > 0) {
                    String credentialsPart = urlWithoutScheme.substring(0, atIndex);
                    String hostPortDbPart = urlWithoutScheme.substring(atIndex + 1);
                    
                   
                    if (credentialsPart.contains(":")) {
                        String[] creds = credentialsPart.split(":", 2);
                        dbUsername = URLDecoder.decode(creds[0], StandardCharsets.UTF_8);
                        dbPassword = URLDecoder.decode(creds[1], StandardCharsets.UTF_8);
                        System.out.println("=== DataSourceConfig: Extracted username = " + dbUsername);
                        System.out.println("=== DataSourceConfig: Password length = " + dbPassword.length());
                    }
                    
                   
                    int slashIndex = hostPortDbPart.indexOf('/');
                    String hostPort = slashIndex > 0 ? hostPortDbPart.substring(0, slashIndex) : hostPortDbPart;
                    String database = slashIndex > 0 ? hostPortDbPart.substring(slashIndex + 1) : "";
                    
                   
                    String host;
                    int port = 5432;
                    if (hostPort.contains(":")) {
                        String[] parts = hostPort.split(":", 2);
                        host = parts[0];
                        try {
                            port = Integer.parseInt(parts[1]);
                        } catch (NumberFormatException e) {
                            port = 5432;
                        }
                    } else {
                        host = hostPort;
                    }
                    
                    System.out.println("=== DataSourceConfig: Extracted host = " + host);
                    System.out.println("=== DataSourceConfig: Extracted port = " + port);
                    System.out.println("=== DataSourceConfig: Extracted database = " + database);
                    
                   
                    jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=prefer", host, port, database);
                    System.out.println("=== DataSourceConfig: Built JDBC URL = " + jdbcUrl);
                } else {
                    // No credentials in URL, use URI parsing
                    URI uri = new URI(jdbcUrl);
                    String host = uri.getHost();
                    int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                    String path = uri.getPath();
                    if (path.startsWith("/")) {
                        path = path.substring(1);
                    }
                    jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=prefer", host, port, path);
                    System.out.println("=== DataSourceConfig: Built JDBC URL (no credentials) = " + jdbcUrl);
                }
            } catch (Exception e) {
                System.err.println("=== DataSourceConfig: Error parsing postgresql:// URL: " + e.getMessage());
                e.printStackTrace();
                
                jdbcUrl = "jdbc:" + jdbcUrl;
            }
        } else if (!jdbcUrl.startsWith("jdbc:")) {

            jdbcUrl = "jdbc:" + jdbcUrl;
        }
        
        System.out.println("=== DataSourceConfig: Final JDBC URL = " + jdbcUrl);
        System.out.println("=== DataSourceConfig: Final username = " + dbUsername);
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(dbUsername)
                .password(dbPassword)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

