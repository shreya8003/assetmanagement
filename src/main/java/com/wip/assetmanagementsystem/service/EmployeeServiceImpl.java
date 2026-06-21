package com.wip.assetmanagementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wip.assetmanagementsystem.entity.Employee;
import com.wip.assetmanagementsystem.exception.AlreadyExistsException;
import com.wip.assetmanagementsystem.repository.EmployeeRepository;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeServiceImpl(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public Employee saveEmployee(Employee employee) {
        if (employeeRepository.findByEmail(employee.getEmail()) != null) {
            throw new AlreadyExistsException("Employee with this email already exists.");
        }
        return employeeRepository.save(employee);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Employee getEmployeeById(Integer id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee Not Found"));
    }

    @Override
    public Employee updateEmployee(Integer id, Employee employee) {

        Employee existing = getEmployeeById(id);

        Employee byEmail = employeeRepository.findByEmail(employee.getEmail());
        if (byEmail != null && !byEmail.getId().equals(id)) {
            throw new AlreadyExistsException("Employee with this email already exists.");
        }

        existing.setName(employee.getName());
        existing.setEmail(employee.getEmail());
        existing.setCompany(employee.getCompany());

        return employeeRepository.save(existing);
    }

    @Override
    public void deleteEmployee(Integer id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> getEmployeeByDept(String department) {
        return employeeRepository.findByCompany(department);
    }
}