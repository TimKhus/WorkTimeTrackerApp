package com.example.workTogetherTimeTracker.repository;

import com.example.workTogetherTimeTracker.model.EmployeeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeEntityRepository extends JpaRepository<EmployeeEntity, Long> {

}