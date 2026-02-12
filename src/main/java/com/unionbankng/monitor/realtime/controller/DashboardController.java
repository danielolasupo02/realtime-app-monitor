package com.unionbankng.monitor.realtime.controller;


import org.springframework.ui.Model;
import com.unionbankng.monitor.realtime.model.DashboardData;
import com.unionbankng.monitor.realtime.service.MetricService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for the monitoring dashboard.
 *
 * This controller:
 * - Handles HTTP requests
 * - Calls the service layer
 * - Adds data to the Thymeleaf model
 * - Returns view names
 *
 * This controller does NOT:
 * - Access the database directly
 * - Calculate metrics
 * - Contain business logic
 */
@Controller
public class DashboardController {

    private final MetricService metricService;

    public DashboardController(MetricService metricService) {
        this.metricService = metricService;
    }

    /**
     * Displays the main monitoring dashboard.
     *
     * Supports optional team filtering via query parameter:
     * - GET / → shows all teams and applications
     * - GET /?teamId=1 → shows applications for team 1 only
     *
     * @param teamId Optional team ID for filtering
     * @param model Thymeleaf model
     * @return View name (dashboard.html)
     */
    @GetMapping("/")
    public String showDashboard(
            @RequestParam(required = false) Long teamId,
            Model model) {

        DashboardData dashboardData;

        if (teamId != null) {
            // Filter by specific team
            dashboardData = metricService.getDashboardDataForTeam(teamId);
            model.addAttribute("selectedTeamId", teamId);
        } else {
            // Show all teams
            dashboardData = metricService.getDashboardData();
        }

        model.addAttribute("dashboardData", dashboardData);

        return "dashboard";
    }

    /**
     * Health check endpoint.
     *
     * @return Simple text response
     */
    @GetMapping("/health")
    public String health() {
        return "OK";
    }
}

