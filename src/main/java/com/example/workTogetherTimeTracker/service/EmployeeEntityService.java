package com.example.workTogetherTimeTracker.service;

import com.example.workTogetherTimeTracker.model.Employee;
import com.example.workTogetherTimeTracker.model.EmployeeEntity;
import com.example.workTogetherTimeTracker.model.ProjectDates;
import com.example.workTogetherTimeTracker.repository.EmployeeEntityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

@Service
public class EmployeeEntityService {

    private final EmployeeEntityRepository employeeRepository;

    @Autowired
    public EmployeeEntityService(EmployeeEntityRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public void saveEmployeeToDatabase(Employee employee) {
        Long employeeId = employee.getId();

        for (Map.Entry<Long, Set<ProjectDates>> entry : employee.getAllProjectDates().entrySet()) {
            Long projectId = entry.getKey();

            for (ProjectDates projectDates : entry.getValue()) {
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String dateFromString = projectDates.getDateFrom().format(formatter);
                String dateToString = projectDates.getDateTo().format(formatter);

                try {
                    employeeRepository.save(new EmployeeEntity(employeeId, projectId, dateFromString, dateToString));
                } catch (DataIntegrityViolationException e) {
                    e.getStackTrace();
                }
            }
        }
    }

    public List<EmployeeEntity> getAllEmployees() {
        return employeeRepository.findAll();
    }

    public Optional<EmployeeEntity> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    public EmployeeEntity saveEmployee(EmployeeEntity employee) {
        return employeeRepository.save(employee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    public void clearData() {
        employeeRepository.deleteAll();
    }
}
