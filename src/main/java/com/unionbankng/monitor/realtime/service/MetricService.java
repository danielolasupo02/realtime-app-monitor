package com.unionbankng.monitor.realtime.service;

import com.unionbankng.monitor.realtime.model.Application;
import com.unionbankng.monitor.realtime.model.DashboardData;
import com.unionbankng.monitor.realtime.model.Metric;
import com.unionbankng.monitor.realtime.model.Team;
import com.unionbankng.monitor.realtime.repository.ApplicationRepository;
import com.unionbankng.monitor.realtime.repository.MetricRepository;
import com.unionbankng.monitor.realtime.repository.TeamRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service layer for orchestrating dashboard data retrieval.
 *
 * This service:
 * - Calls repositories to fetch data
 * - Combines data from multiple sources
 * - Performs minimal transformation (grouping only)
 *
 * This service does NOT:
 * - Calculate metrics (done in database)
 * - Contain application-specific logic
 * - Query tables directly
 */
@Service
public class MetricService {
    private final TeamRepository teamRepository;
    private final ApplicationRepository applicationRepository;
    private final MetricRepository metricRepository;
    public MetricService(TeamRepository teamRepository,
                         ApplicationRepository applicationRepository,
                         MetricRepository metricRepository) {
        this.teamRepository = teamRepository;
        this.applicationRepository = applicationRepository;
        this.metricRepository = metricRepository;
    }

    /**
     * Retrieves all data needed for the dashboard.
     *
     * Orchestrates multiple repository calls and groups the results
     * into a single DashboardData object for the view layer.
     *
     * @return Complete dashboard data
     */
    public DashboardData getDashboardData() {
        DashboardData dashboardData = new DashboardData();

        // 1. Fetch all enabled teams
        List<Team> teams = teamRepository.findAllEnabled();
        dashboardData.setTeams(teams);

        // 2. Fetch applications grouped by team
        Map<Long, List<Application>> appsByTeam = new HashMap<>();
        for (Team team : teams) {
            List<Application> apps = applicationRepository.findByTeam(team.getTeamId());
            appsByTeam.put(team.getTeamId(), apps);
        }
        dashboardData.setApplicationsByTeam(appsByTeam);

        // 3. Fetch all metrics and group by application
        List<Metric> allMetrics = metricRepository.findAllCurrentMetrics();
        Map<String, Map<String, Double>> metricsByApp = groupMetricsByApplication(allMetrics);
        dashboardData.setMetricsByApp(metricsByApp);

        return dashboardData;
    }

    /**
     * Retrieves dashboard data filtered by a specific team.
     *
     * @param teamId Team ID to filter by
     * @return Dashboard data for the specified team
     */
    public DashboardData getDashboardDataForTeam(Long teamId) {
        DashboardData dashboardData = new DashboardData();

        // Fetch all teams for the dropdown
        List<Team> allTeams = teamRepository.findAllEnabled();
        dashboardData.setTeams(allTeams);

        // Fetch applications for the selected team only
        List<Application> apps = applicationRepository.findByTeam(teamId);
        Map<Long, List<Application>> appsByTeam = new HashMap<>();
        appsByTeam.put(teamId, apps);
        dashboardData.setApplicationsByTeam(appsByTeam);

        // Fetch all metrics (filtering happens in frontend for simplicity)
        List<Metric> allMetrics = metricRepository.findAllCurrentMetrics();
        Map<String, Map<String, Double>> metricsByApp = groupMetricsByApplication(allMetrics);
        dashboardData.setMetricsByApp(metricsByApp);

        return dashboardData;
    }

    /**
     * Calculates health score based on metrics.
     */
    public double calculateScore(Map<String, Double> metrics) {
        if (metrics == null) return 0.0;

        Double lastHour = metrics.get("last_hr_count");
        Double avg15Day = metrics.get("avg_1hr_15wd");

        if (lastHour == null || avg15Day == null || avg15Day == 0) {
            return 0.0;
        }

        return (lastHour / avg15Day) * 100.0;
    }

    /**
     * Returns CSS class based on health score.
     */
    public String getStatusClass(Map<String, Double> metrics) {
        double score = calculateScore(metrics);

        if (score >= 90) {
            return "status-good";
        } else if (score >= 70) {
            return "status-warning";
        } else {
            return "status-critical";
        }
    }


    /**
     * Groups metrics by application code.
     *
     * Transforms flat list of metrics into nested map structure:
     * {
     *   "payments": {
     *     "last_hr_count": 125,
     *     "avg_1hr_15wd": 110
     *   },
     *   "orders": {
     *     "last_hr_count": 78,
     *     "avg_1hr_15wd": 82
     *   }
     * }
     *
     * @param metrics Flat list of metrics
     * @return Nested map grouped by app_code
     */
    private Map<String, Map<String, Double>> groupMetricsByApplication(List<Metric> metrics) {
        Map<String, Map<String, Double>> result = new HashMap<>();

        for (Metric metric : metrics) {
            String appCode = metric.getAppCode();

            // Get or create inner map for this application
            Map<String, Double> appMetrics = result.computeIfAbsent(appCode, k -> new HashMap<>());

            // Add metric to the application's map
            appMetrics.put(metric.getMetricCode(), metric.getMetricValue());
        }

        return result;
    }
}
