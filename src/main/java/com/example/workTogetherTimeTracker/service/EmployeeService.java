package com.example.workTogetherTimeTracker.service;

import com.example.workTogetherTimeTracker.model.Employee;
import com.example.workTogetherTimeTracker.model.EmployeePair;
import com.example.workTogetherTimeTracker.model.ProjectDates;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
public class EmployeeService {

    private final Set<Employee> employees = new HashSet<>();
    private final Set<EmployeePair> employeePairs = new HashSet<>();

    public void addEmployees(Set<Employee> employeesList) {
        employees.addAll(employeesList);
    }

    public Set<EmployeePair> createEmployeePairs() {

        List<Employee> employeesList = new ArrayList<>(employees);

        for (int i = 0; i < employees.size() - 1; i++) {
            for (int j = i + 1; j < employeesList.size(); j++) {
                Employee employee1 = employeesList.get(i);
                Employee employee2 = employeesList.get(j);
                EmployeePair pair = new EmployeePair(employee1, employee2);
                if (!employeePairs.contains(pair)
                        && !employee1.getId().equals(employee2.getId())) {
                    employeePairs.add(pair);
                }
            }
        }
        return employeePairs;
    }

    public int calculateTotalDaysWorkedTogether(EmployeePair employeePair) {

        Map<Long, Set<ProjectDates>> allProjectDates = employeePair.getDaysWorkedTogetherByProject();
        int totalDaysWorkedTogether = 0;

        for (Long projectId : allProjectDates.keySet()) {
            Set<ProjectDates> projectDates = allProjectDates.get(projectId);

            for (ProjectDates dates : projectDates) {
                LocalDate start = dates.getDateFrom();
                LocalDate end = dates.getDateTo();
                totalDaysWorkedTogether += (int) ChronoUnit.DAYS.between(start, end.plusDays(1));
            }
        }

        return totalDaysWorkedTogether;
    }

    public void clearData() {
        employees.clear();
        employeePairs.clear();
    }

    public EmployeePair findPairWithMostDaysWorkedTogether() {
        EmployeePair mostDaysWorkedPair = null;
        int maxDaysWorked = 0;

        for (EmployeePair employeePair : employeePairs) {
            int daysWorked = calculateTotalDaysWorkedTogether(employeePair);
            if (daysWorked > maxDaysWorked) {
                maxDaysWorked = daysWorked;
                mostDaysWorkedPair = employeePair;
            }
        }

        return mostDaysWorkedPair;
    }
}
