package com.unionbankng.monitor.realtime.repository;

import com.unionbankng.monitor.realtime.model.Metric;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

/**
 * Repository for accessing metric data from the database.
 *
 * CRITICAL: This repository calls the unified metric API only.
 * It does NOT:
 * - Query raw application tables
 * - Calculate metrics
 * - Contain application-specific logic
 * - Reference timestamp column names
 *
 * All metric calculation logic resides in the database (PL/SQL).
 */
@Repository
public class MetricRepository {

    private final JdbcTemplate jdbcTemplate;
    private static final Logger log = LoggerFactory.getLogger(MetricRepository.class);

    public MetricRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all current metrics for all applications.
     *
     * Calls the unified metric API that returns standardized data:
     * - app_code: Application identifier
     * - metric_code: Metric identifier (e.g., 'last_hr_count', 'avg_1hr_15wd')
     * - metric_value: Calculated metric value
     *
     * The database (PL/SQL) handles:
     * - Finding the correct normalized view for each application
     * - Calculating metrics using the standardized event_ts column
     * - Returning results in a uniform format
     *
     * @return List of all current metrics
     */
    public List<Metric> findAllCurrentMetrics() {
        try {
            String sql = "SELECT app_code, metric_code, metric_value " +
                    "FROM TABLE(metric_api.get_current_metrics()) " +
                    "ORDER BY app_code, metric_code";

            return jdbcTemplate.query(sql, new MetricRowMapper());

        } catch (DataAccessException e) {
            // Log the error
            log.error("Error fetching metrics from database: {}", e.getMessage());

            // Return empty list instead of throwing exception
            return Collections.emptyList();
        }
    }

    /**
     * RowMapper to convert ResultSet to Metric object.
     */
    private static class MetricRowMapper implements RowMapper<Metric> {
        @Override
        public Metric mapRow(ResultSet rs, int rowNum) throws SQLException {
            Metric metric = new Metric();
            metric.setAppCode(rs.getString("app_code"));
            metric.setMetricCode(rs.getString("metric_code"));
            metric.setMetricValue(rs.getDouble("metric_value"));
            return metric;
        }
    }
}