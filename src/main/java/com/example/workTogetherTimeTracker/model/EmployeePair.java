package com.example.workTogetherTimeTracker.model;

import java.time.temporal.ChronoUnit;
import java.util.*;

public class EmployeePair {

    private final Long employeeId1;
    private final Long employeeId2;
    private final Map<Long, Set<ProjectDates>> daysWorkedTogetherByProject;


    public EmployeePair(Employee employee1, Employee employee2) {
        this.employeeId1 = employee1.getId();
        this.employeeId2 = employee2.getId();
        this.daysWorkedTogetherByProject = addOverlapDays(employee1.getAllProjectDates(), employee2.getAllProjectDates());
    }


    public Long getEmployeeId1() {
        return employeeId1;
    }

    public Long getEmployeeId2() {
        return employeeId2;
    }

    public Map<Long, Set<ProjectDates>> getDaysWorkedTogetherByProject() {
        return daysWorkedTogetherByProject;
    }

    private Map<Long, Set<ProjectDates>> addOverlapDays(Map<Long, Set<ProjectDates>> allProjectDates1,
                                                        Map<Long, Set<ProjectDates>> allProjectDates2) {

        Map<Long, Set<ProjectDates>> overlapDays = new HashMap<>();

        for (Long projectId : allProjectDates1.keySet()) {
            if (allProjectDates2.containsKey(projectId)) {
                Set<ProjectDates> datesSet = new HashSet<>();
                for (ProjectDates dates1 : allProjectDates1.get(projectId)) {
                    for (ProjectDates dates2 : allProjectDates2.get(projectId)) {
                        ProjectDates intersectionDates = dates1.calculateIntersection(dates2);
                        if (intersectionDates != null) {
                            datesSet.add(intersectionDates);
                        }
                    }
                }
                if (datesSet.size() > 0) {
                    overlapDays.put(projectId, datesSet);
                }
            }
        }

        return overlapDays;
    }


    public Map<Long, Integer> calculateDaysWorkedTogether() {
        Map<Long, Integer> totalDaysWorkedOnProject = new HashMap<>();

        for (Map.Entry<Long, Set<ProjectDates>> entry : getDaysWorkedTogetherByProject().entrySet()) {
            Long projectId = entry.getKey();
            Set<ProjectDates> projectDatesSet = entry.getValue();

            int totalDaysWorkedOnProjectId = projectDatesSet.stream()
                    .mapToInt(dates -> (int) ChronoUnit.DAYS.between(dates.getDateFrom(), dates.getDateTo().plusDays(1)))
                    .sum();
            totalDaysWorkedOnProject.put(projectId, totalDaysWorkedOnProjectId);
        }

        return totalDaysWorkedOnProject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EmployeePair that = (EmployeePair) o;
        return (Objects.equals(employeeId1, that.employeeId1) && Objects.equals(employeeId2, that.employeeId2)) ||
                (Objects.equals(employeeId1, that.employeeId2) && Objects.equals(employeeId2, that.employeeId1));
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeId1, employeeId2);
    }

    public int calculateTotalDaysWorkedTogether() {
        int totalDaysWorkedTogether = 0;

        for (Integer days : calculateDaysWorkedTogether().values()) {
            totalDaysWorkedTogether += days;
        }

        return totalDaysWorkedTogether;
    }

}
