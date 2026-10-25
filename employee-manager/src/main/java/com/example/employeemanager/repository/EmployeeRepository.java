package com.example.employeemanager.repository;

import com.example.employeemanager.model.Employee;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Optional<Employee> findByEmail(String email);

    List<Employee> findByFullNameContainingIgnoreCase(String keyword);
}
