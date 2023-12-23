package com.example.workTogetherTimeTracker.model;

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
            // Проверяем, не существует ли уже такой проект у сотрудника
            if (!existingDates.contains(new ProjectDates(dateFromString, dateToString))) {
                ProjectDates dates = new ProjectDates(dateFromString, dateToString);
                existingDates.add(dates);
            }
        } else {
            HashSet<ProjectDates> dateSet = new HashSet<>();
            dateSet.add(new ProjectDates(dateFromString, dateToString));
            allProjectDates.put(projectId, dateSet);
        }
    }

    public Long getId() {
        return employeeId;
    }

    public Map<Long, Set<ProjectDates>> getAllProjectDates() {
        return allProjectDates;
    }
}
