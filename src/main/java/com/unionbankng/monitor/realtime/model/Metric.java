package com.unionbankng.monitor.realtime.model;


import jakarta.persistence.*;

/**
 * Represents a single metric value for an application.
 *
 * This DTO maps the result of metric_api.get_current_metrics().
 * No business logic - just data container.
 *
 * The metric value is calculated by the database (PL/SQL),
 * never by Java code.
 */
@Entity
@Table(name = "metric")
public class Metric {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long metricId;
    private String appCode;
    private String metricCode;
    private Double metricValue;

    // Default constructor
    public Metric() {
    }

    // Full constructor
    public Metric(Long metricId, String appCode, String metricCode, Double metricValue) {
        this.metricId = metricId;
        this.appCode = appCode;
        this.metricCode = metricCode;
        this.metricValue = metricValue;
    }

    // Getters and Setters


    public Long getMetricId() {
        return metricId;
    }

    public void setMetricId(Long metricId) {
        this.metricId = metricId;
    }

    public String getAppCode() {
        return appCode;
    }

    public void setAppCode(String appCode) {
        this.appCode = appCode;
    }

    public String getMetricCode() {
        return metricCode;
    }

    public void setMetricCode(String metricCode) {
        this.metricCode = metricCode;
    }

    public Double getMetricValue() {
        return metricValue;
    }

    public void setMetricValue(Double metricValue) {
        this.metricValue = metricValue;
    }

    @Override
    public String toString() {
        return "Metric{" +
                "appCode='" + appCode + '\'' +
                ", metricCode='" + metricCode + '\'' +
                ", metricValue=" + metricValue +'}';
    }
}