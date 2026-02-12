package com.unionbankng.monitor.realtime.repository;

import com.unionbankng.monitor.realtime.model.Application;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

/**
 * Repository for accessing Application metadata from the database.
 *
 * IMPORTANT: This class only executes queries, no business logic.
 * This repository NEVER queries raw application tables directly.
 * It only queries the metadata tables (applications, team_applications).
 */
@Repository
public class ApplicationRepository {

    private final JdbcTemplate jdbcTemplate;

    public ApplicationRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all enabled applications grouped by team.
     *
     * @return List of all enabled applications with team associations
     */
    public List<Application> findAllEnabled() {
        String sql = "SELECT a.app_id, a.app_code, a.app_name, a.enabled, a.created_date " +
                "FROM applications a " +
                "WHERE a.enabled = 1 " +
                "ORDER BY a.app_name";

        return jdbcTemplate.query(sql, new ApplicationRowMapper());
    }

    /**
     * Retrieves applications for a specific team.
     *
     * @param teamId The team ID
     * @return List of applications belonging to the team
     */
    public List<Application> findByTeam(Long teamId) {
        String sql = "SELECT a.app_id, a.app_code, a.app_name, a.enabled, a.created_date " +
                "FROM applications a " +
                "INNER JOIN team_applications ta ON a.app_id = ta.app_id " +
                "WHERE ta.team_id = ? AND a.enabled = 1 " +
                "ORDER BY a.app_name";

        return jdbcTemplate.query(sql, new ApplicationRowMapper(), teamId);
    }

    /**
     * RowMapper to convert ResultSet to Application object.
     */
    private static class ApplicationRowMapper implements RowMapper<Application> {
        @Override
        public Application mapRow(ResultSet rs, int rowNum) throws SQLException {
            Application app = new Application();
            app.setAppId(rs.getLong("app_id"));
            app.setAppCode(rs.getString("app_code"));
            app.setAppName(rs.getString("app_name"));
            app.setEnabled(rs.getInt("enabled") == 1);

            Timestamp createdTs = rs.getTimestamp("created_date");
            if (createdTs != null) {
                app.setCreatedDate(createdTs.toLocalDateTime());
            }

            return app;
        }
    }
}
