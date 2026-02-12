package com.unionbankng.monitor.realtime.repository;

import com.unionbankng.monitor.realtime.model.Team;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Repository for accessing Team metadata from the database.
 *
 * IMPORTANT: This class only executes queries, no business logic.
 * All data validation and constraints are enforced by the database.
 */
@Repository
public class TeamRepository {

    private final JdbcTemplate jdbcTemplate;

    public TeamRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    /**
     * Retrieves all enabled teams.
     *
     * @return List of enabled teams
     */
    public List<Team> findAllEnabled() {
        String sql = "SELECT team_id, team_name, team_description, enabled " +
                "FROM teams " +
                "WHERE enabled = 1 " +
                "ORDER BY team_name";

        return jdbcTemplate.query(sql, new TeamRowMapper());
    }

    /**
     * RowMapper to convert ResultSet to Team object.
     */
    private static class TeamRowMapper implements RowMapper<Team> {
        @Override
        public Team mapRow(ResultSet rs, int rowNum) throws SQLException {
            Team team = new Team();
            team.setTeamId(rs.getLong("team_id"));
            team.setTeamName(rs.getString("team_name"));
            team.setTeamDescription(rs.getString("team_description"));
            team.setEnabled(rs.getInt("enabled") == 1);
            return team;
        }
    }
}
