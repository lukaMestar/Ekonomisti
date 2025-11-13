package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.net.URI;

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
        
        System.out.println("=== DataSourceConfig: Original URL = " + jdbcUrl);
        System.out.println("=== DataSourceConfig: Original username = " + dbUsername);
        
        // If URL is in postgresql:// format (from Render), parse it
        if (jdbcUrl.startsWith("postgresql://")) {
            try {
                // Parse postgresql://user:pass@host:port/dbname
                URI uri = new URI(jdbcUrl);
                String userInfo = uri.getUserInfo();
                System.out.println("=== DataSourceConfig: Parsed userInfo = " + userInfo);
                
                if (userInfo != null && userInfo.contains(":")) {
                    String[] credentials = userInfo.split(":", 2);
                    dbUsername = credentials[0];
                    dbPassword = credentials[1];
                    System.out.println("=== DataSourceConfig: Extracted username = " + dbUsername);
                }
                
                String host = uri.getHost();
                int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                String path = uri.getPath();
                if (path.startsWith("/")) {
                    path = path.substring(1);
                }
                
                // Build JDBC URL: jdbc:postgresql://host:port/database
                jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s", host, port, path);
                System.out.println("=== DataSourceConfig: Built JDBC URL = " + jdbcUrl);
            } catch (Exception e) {
                System.err.println("=== DataSourceConfig: Error parsing URI: " + e.getMessage());
                e.printStackTrace();
                // Fallback: just add jdbc: prefix
                jdbcUrl = "jdbc:" + jdbcUrl;
            }
        } else if (!jdbcUrl.startsWith("jdbc:")) {
            // If it doesn't start with jdbc:, add it
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

