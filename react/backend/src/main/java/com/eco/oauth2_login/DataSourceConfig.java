package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class DataSourceConfig {

    @Value("${SPRING_DATASOURCE_URL:jdbc:postgresql://localhost:5433/ekonomisti}")
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
        
        if (jdbcUrl == null || jdbcUrl.isEmpty()) {
            jdbcUrl = "jdbc:postgresql://localhost:5433/ekonomisti";
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
                    
                    jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=prefer", host, port, database);
                } else {
                    URI uri = new URI(jdbcUrl);
                    String host = uri.getHost();
                    int port = uri.getPort() == -1 ? 5432 : uri.getPort();
                    String path = uri.getPath();
                    if (path.startsWith("/")) {
                        path = path.substring(1);
                    }
                    jdbcUrl = String.format("jdbc:postgresql://%s:%d/%s?sslmode=prefer", host, port, path);
                }
            } catch (Exception e) {
                jdbcUrl = "jdbc:" + jdbcUrl;
            }
        } else if (!jdbcUrl.startsWith("jdbc:")) {
            jdbcUrl = "jdbc:" + jdbcUrl;
        }
        
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(dbUsername);
        config.setPassword(dbPassword);
        config.setDriverClassName("org.postgresql.Driver");
        config.setAutoCommit(false); // Disable autoCommit for transaction management
        config.setConnectionTimeout(60000);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTestQuery("SELECT 1");
        
        return new HikariDataSource(config);
    }
}

