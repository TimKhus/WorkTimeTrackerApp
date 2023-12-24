package com.example.workTogetherTimeTracker.model;

import java.time.LocalDate;
import java.util.*;

public class Employee {

    private final Long employeeId;
    private final Map<Long, Set<ProjectDates>> allProjectDates;

    public Employee(Long employeeId, Long projectId, String dateFromString, String dateToString) {
        this.employeeId = employeeId;
        this.allProjectDates = new HashMap<>();
        addProject(projectId, dateFromString, dateToString);
    }

    public void addProject(Long projectId, String dateFromString, String dateToString) {
        if (allProjectDates.containsKey(projectId)) {
            Set<ProjectDates> existingDates = allProjectDates.get(projectId);
            ProjectDates newDates = new ProjectDates(dateFromString, dateToString);

            // Check intersection of project dates
            Set<ProjectDates> intersectingDates = new HashSet<>();
            for (ProjectDates existing : existingDates) {
                ProjectDates intersection = existing.calculateIntersection(newDates);
                if (intersection != null) {
                    intersectingDates.add(existing);
                }
            }

            // Removing overlap intervals and adding a new one
            existingDates.removeAll(intersectingDates);

            // Merge intersecting intervals and add the new one
            ProjectDates mergedDates = mergeIntervals(intersectingDates, newDates);
            existingDates.add(mergedDates);
        } else {
            HashSet<ProjectDates> dateSet = new HashSet<>();
            dateSet.add(new ProjectDates(dateFromString, dateToString));
            allProjectDates.put(projectId, dateSet);
        }
    }

    private ProjectDates mergeIntervals(Set<ProjectDates> intervals, ProjectDates newDates) {
        LocalDate mergedStart = newDates.getDateFrom();
        LocalDate mergedEnd = newDates.getDateTo();

        for (ProjectDates interval : intervals) {
            if (interval.getDateFrom().isBefore(mergedStart)) {
                mergedStart = interval.getDateFrom();
            }
            if (interval.getDateTo().isAfter(mergedEnd)) {
                mergedEnd = interval.getDateTo();
            }
        }

        return new ProjectDates(mergedStart.toString(), mergedEnd.toString());
    }

    public Long getId() {
        return employeeId;
    }

    public Map<Long, Set<ProjectDates>> getAllProjectDates() {
        return allProjectDates;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employee employee = (Employee) o;
        return Objects.equals(employeeId, employee.employeeId) && Objects.equals(allProjectDates, employee.allProjectDates);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId, allProjectDates);
    }

}
