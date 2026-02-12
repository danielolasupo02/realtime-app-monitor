package com.unionbankng.monitor.realtime.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

/**
 * Database configuration for Oracle connectivity.
 *
 * This configuration:
 * - Sets up JdbcTemplate for database access
 * - Uses HikariCP connection pooling (autoconfigured by Spring Boot)
 * - Configuration values come from application.properties
 *
 * IMPORTANT: This class contains NO application metadata.
 * Application definitions, metrics, and teams are stored in the database.
 */
@Configuration
public class DatabaseConfig {
    /**
     * Creates JdbcTemplate bean for database operations.
     *
     * @param dataSource Autoconfigured by Spring Boot from application.properties
     * @return JdbcTemplate instance for database queries
     */
    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
