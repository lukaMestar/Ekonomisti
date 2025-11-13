package com.eco.oauth2_login;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

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
        if (jdbcUrl.startsWith("postgresql://")) {
            jdbcUrl = "jdbc:" + jdbcUrl;
        }
        
        return DataSourceBuilder.create()
                .url(jdbcUrl)
                .username(username)
                .password(password)
                .driverClassName("org.postgresql.Driver")
                .build();
    }
}

