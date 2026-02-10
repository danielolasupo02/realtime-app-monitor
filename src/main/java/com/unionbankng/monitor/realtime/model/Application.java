package com.unionbankng.monitor.realtime.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Represents a monitored application.
 *
 * This is a simple DTO that maps to the APPLICATIONS table.
 * No business logic - just data container.
 *
 * IMPORTANT: This class does NOT contain:
 * - Raw table names
 * - Timestamp column names
 * - Metric calculation logic
 *
 * Those concerns are handled in the database layer.
 */
@Entity
@Table(name = "application")
public class Application {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long appId;

    @Column(nullable = false)
    private String appCode;

    @Column(nullable = false)
    private String appName;


    private boolean enabled;

    @Column(nullable = false)
    private LocalDateTime createdDate;

    // Default constructor
    public Application() {
    }

    // Full constructor
    public Application(Long appId, String appCode, String appName, boolean enabled, LocalDateTime createdDate) {
        this.appId = appId;
        this.appCode = appCode;
        this.appName = appName;
        this.enabled = enabled;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public Long getAppId() {
        return appId;
    }

    public void setAppId(Long appId) {
        this.appId = appId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    @Override
    public String toString() {
        return "Application{" +
                "appId=" + appId +
                ", appCode='" + appCode + '\'' +
                ", appName='" + appName + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
