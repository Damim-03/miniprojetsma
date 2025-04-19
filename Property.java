package com.retima.models;

import java.math.BigDecimal;

public class Property {
    private int id;
    private int agentId;
    private BigDecimal minPrice;
    private BigDecimal maxPrice;
    private int duration; // مدة الكراء بالأشهر
    private int negotiationRounds;
    private Status status;

    public enum Status {
        AVAILABLE, RENTED, NEGOTIATING
    }

    // Constructor
    public Property(int id, int agentId, BigDecimal minPrice, BigDecimal maxPrice, int duration) {
        this.id = id;
        this.agentId = agentId;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.duration = duration;
        this.negotiationRounds = 0;
        this.status = Status.AVAILABLE;
    }

    // getters و setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }

    public BigDecimal getMinPrice() {
        return minPrice;
    }

    public void setMinPrice(BigDecimal minPrice) {
        this.minPrice = minPrice;
    }

    public BigDecimal getMaxPrice() {
        return maxPrice;
    }

    public void setMaxPrice(BigDecimal maxPrice) {
        this.maxPrice = maxPrice;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getNegotiationRounds() {
        return negotiationRounds;
    }

    public void setNegotiationRounds(int negotiationRounds) {
        this.negotiationRounds = negotiationRounds;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void incrementNegotiationRounds() {
        this.negotiationRounds++;
    }

    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", agentId=" + agentId +
                ", minPrice=" + minPrice +
                ", maxPrice=" + maxPrice +
                ", duration=" + duration +
                ", negotiationRounds=" + negotiationRounds +
                ", status=" + status +
                '}';
    }
}
