package com.unionbankng.monitor.realtime.model;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * View model for the dashboard page.
 *
 * Aggregates all data needed by the Thymeleaf template:
 * - Teams
 * - Applications grouped by team
 * - Metrics for each application
 *
 * This class performs minimal transformation (grouping),
 * but NO business logic or metric calculations.
 */
public class DashboardData {
    private List<Team> teams;
    private Map<Long, List<Application>> applicationsByTeam;
    private Map<String, Map<String, Double>> metricsByApp;

    // Default constructor
    public DashboardData() {
        this.teams = new ArrayList<>();
        this.applicationsByTeam = new HashMap<>();
        this.metricsByApp = new HashMap<>();
    }

    // Getters and Setters
    public List<Team> getTeams() {
        return teams;
    }

    public void setTeams(List<Team> teams) {
        this.teams = teams;
    }

    public Map<Long, List<Application>> getApplicationsByTeam() {
        return applicationsByTeam;
    }

    public void setApplicationsByTeam(Map<Long, List<Application>> applicationsByTeam) {
        this.applicationsByTeam = applicationsByTeam;
    }

    public Map<String, Map<String, Double>> getMetricsByApp() {
        return metricsByApp;
    }

    public void setMetricsByApp(Map<String, Map<String, Double>> metricsByApp) {
        this.metricsByApp = metricsByApp;
    }

    /**
     * Helper method to get metrics for a specific application.
     * Returns empty map if no metrics found.
     */
    public Map<String, Double> getMetricsForApp(String appCode) {
        return metricsByApp.getOrDefault(appCode, new HashMap<>());
    }

    /**
     * Helper method to get applications for a specific team.
     * Returns empty list if no applications found.
     */
    public List<Application> getApplicationsForTeam(Long teamId) {
        return applicationsByTeam.getOrDefault(teamId, new ArrayList<>());
    }

    @Override
    public String toString() {
        return "DashboardData{" +
                "teams=" + teams.size() +
                ", applicationsByTeam=" + applicationsByTeam.size() +
                ", metricsByApp=" + metricsByApp.size() +
                '}';
    }
}
