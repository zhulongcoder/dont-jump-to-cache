package com.xyz.somesystem.somemodule.core.model;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

public class SomeKindOfPosition {

    private int id;
    private double netOpenPositionLimit;
    private Map<LocalDate, Double> utilizedNetOpenPosition = new HashMap<>();
    private Map<LocalDate, Double> valueDateToUtilizationPercentage = new HashMap<>();
    private Map<LocalDate, SomeLimitPosition> position = new HashMap<>();
    private TreeSet<LocalDate> valueDates = new TreeSet<>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getNetOpenPositionLimit() {
        return netOpenPositionLimit;
    }

    public void setNetOpenPositionLimit(double netOpenPositionLimit) {
        this.netOpenPositionLimit = netOpenPositionLimit;
    }

    public double getUtilizedNetOpenPosition(LocalDate date) {
        if (valueDates.contains(date)) {
            return utilizedNetOpenPosition.get(date);
        } else {
            return valueDates.higher(date) != null ? utilizedNetOpenPosition.get(valueDates.higher(date)) : 0.0;
        }
    }

    public double getUtilizedPercentage(LocalDate date) {
        return valueDateToUtilizationPercentage.containsKey(date) ? valueDateToUtilizationPercentage.get(date) : 0.0;
    }

    public void setUtilizedNetOpenPosition(LocalDate date, double utilizedNOPCredit) {
        valueDates.add(date);
        this.utilizedNetOpenPosition.put(date, utilizedNOPCredit);
        this.valueDateToUtilizationPercentage.put(date, calculateUtilizationPercentage(utilizedNOPCredit));
    }

    public void addSomeLimitPosition(LocalDate date, SomeLimitPosition position) {
        valueDates.add(date);
        this.position.put(date, position);
    }

    public SomeLimitPosition getCurrentPosition() {
        if (valueDates.isEmpty()) {
            return new SomeLimitPosition();
        } else {
            return position.get(valueDates.first());
        }
    }

    public SomeLimitPosition getPosition(LocalDate date) {
        if (valueDates.contains(date)) {
            return position.get(date);
        } else if (valueDates.higher(date) != null) {
            return position.get(valueDates.higher(date));
        } else {
            return null;
        }
    }

    public Map<LocalDate, SomeLimitPosition> getPosition() {
        return position;
    }

    private Double calculateUtilizationPercentage(double utilizedCredit) {
        return (utilizedCredit / netOpenPositionLimit) * 100;
    }

    public Collection<LocalDate> getValueDatesBefore(LocalDate date) {
        return valueDates.subSet(valueDates.first(), date);
    }

    @Override
    public String toString() {
        return "SomeKindOfPosition [id=" + id + ", netOpenPositionLimit=" + netOpenPositionLimit + ", utilizedNOPCredit=" + utilizedNetOpenPosition
                + ", position=" + position + "]";
    }

}
