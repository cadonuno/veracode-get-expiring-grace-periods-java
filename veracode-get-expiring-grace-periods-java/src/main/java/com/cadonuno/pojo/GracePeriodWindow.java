package com.cadonuno.pojo;

import java.time.Instant;

public class GracePeriodWindow {
    private final Instant startDate;
    private final Instant endDate;

    public GracePeriodWindow(Instant startDate, Instant endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    @Override
    public String toString() {
        return "GracePeriodWindow{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
