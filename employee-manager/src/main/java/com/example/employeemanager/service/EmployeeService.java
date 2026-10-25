package com.example.employeemanager.service;

import com.example.employeemanager.exception.DuplicateEmailException;
import com.example.employeemanager.exception.EmployeeNotFoundException;
import com.example.employeemanager.model.Employee;
import com.example.employeemanager.repository.EmployeeRepository;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    public List<Employee> getAllEmployees(String keyword) {
        if (keyword != null && !keyword.isBlank()) {
            return employeeRepository.findByFullNameContainingIgnoreCase(keyword.trim());
        }
        return employeeRepository.findAll(Sort.by(Sort.Direction.ASC, "id"));
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    public Employee createEmployee(Employee employee) {
        if (employeeRepository.findByEmail(employee.getEmail()).isPresent()) {
            throw new DuplicateEmailException();
        }
        return employeeRepository.save(employee);
    }

    public Employee updateEmployee(Long id, Employee updatedEmployee) {
        Employee existingEmployee = getEmployeeById(id);
        employeeRepository.findByEmail(updatedEmployee.getEmail())
                .filter(employee -> !employee.getId().equals(id))
                .ifPresent(employee -> {
                    throw new DuplicateEmailException();
                });

        existingEmployee.setFullName(updatedEmployee.getFullName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setPhone(updatedEmployee.getPhone());
        existingEmployee.setDepartment(updatedEmployee.getDepartment());
        existingEmployee.setSalary(updatedEmployee.getSalary());
        return employeeRepository.save(existingEmployee);
    }

    public void deleteEmployee(Long id) {
        employeeRepository.delete(getEmployeeById(id));
    }

    public boolean emailExistsForAnotherEmployee(String email, Long id) {
        return employeeRepository.findByEmail(email)
                .map(employee -> id == null || !employee.getId().equals(id))
                .orElse(false);
    }
}
