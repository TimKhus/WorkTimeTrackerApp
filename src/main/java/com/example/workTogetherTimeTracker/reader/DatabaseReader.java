package com.example.workTogetherTimeTracker.reader;

import com.example.workTogetherTimeTracker.model.Employee;
import com.example.workTogetherTimeTracker.model.EmployeeEntity;
import com.example.workTogetherTimeTracker.repository.EmployeeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class DatabaseReader {
    private final EmployeeEntityRepository employeeRepository;

    @Autowired
    public DatabaseReader(EmployeeEntityRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public Set<Employee> readEmployeesFromDatabase() {
        List<EmployeeEntity> employeeEntities = employeeRepository.findAll();
        Set<Employee> employees = new HashSet<>();

        for (EmployeeEntity employeeEntity : employeeEntities) {
            Long employeeId = employeeEntity.getEmployeeId();
            Long projectId = employeeEntity.getProjectId();
            String startDate = employeeEntity.getStartDate();
            String endDate = employeeEntity.getEndDate();

            Employee existingEmployee = getEmployee(employees, employeeId);
            if (existingEmployee == null) {
                Employee employee = new Employee(employeeId, projectId, startDate, endDate);
                employees.add(employee);
            } else {
                existingEmployee.addProject(projectId, startDate, endDate);
            }
        }

        return employees;
    }

    private Employee getEmployee(Set<Employee> employees, Long employeeId) {
        for (Employee employee : employees) {
            if (employee.getId().equals(employeeId)) {
                return employee;
            }
        }
        return null;
    }
}
