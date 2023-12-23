package com.example.workTogetherTimeTracker.parser;

import com.example.workTogetherTimeTracker.model.Employee;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class CsvParser implements Parser {
    private Set<Employee> employees = new HashSet<>();

    @Override
    public Set<Employee> parse(InputStream inputStream) {

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            employees.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length == 4) {
                    Long employeeId = Long.parseLong(values[0].trim());
                    Long projectId = Long.parseLong(values[1].trim());
                    String dateFromString = values[2].trim();
                    String dateToString = values[3].trim();

                    Employee existingEmployee = getEmployee(employeeId);
                    if (existingEmployee == null) {
                        Employee employee = new Employee(employeeId, projectId, dateFromString, dateToString);
                        employees.add(employee);
                    } else {
                        existingEmployee.addProject(projectId, dateFromString, dateToString);
                    }
                }
            }
        } catch (IOException exception) {
            //
            throw new RuntimeException("Failed to read CSV file", exception);
        }

        return employees;
    }

    private Employee getEmployee(Long employeeId) {
        for (Employee employee : employees) {
            if (employee.getId().equals(employeeId)) {
                return employee;
            }
        }
        return null;
    }
}
