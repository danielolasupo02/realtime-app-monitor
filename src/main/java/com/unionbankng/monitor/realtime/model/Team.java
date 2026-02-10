package com.unionbankng.monitor.realtime.model;

import jakarta.persistence.*;

/**
 * Represents a team that owns one or more applications.
 *
 * This is a simple DTO that maps to the TEAMS table.
 * No business logic - just data container.
 */
@Entity
@Table(name = "teams")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teamId;

    @Column(nullable = false)
    private String teamName;

    @Column(nullable = false)
    private String teamDescription;

    private boolean enabled;

    // Default constructor
    public Team() {
    }

    // Full constructor
    public Team(Long teamId, String teamName, String teamDescription, boolean enabled) {
        this.teamId = teamId;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.enabled = enabled;
    }

    // Getters and Setters
    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getTeamDescription() {
        return teamDescription;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public String toString() {
        return "Team{" +
                "teamId=" + teamId +
                ", teamName='" + teamName + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
