package com.wip.assetmanagementsystem.service;

import java.util.List;

import com.wip.assetmanagementsystem.entity.Employee;

public interface EmployeeService {
	Employee saveEmployee(Employee emp);
	List<Employee> getAllEmployees();
	Employee getEmployeeById(Integer id);
	Employee updateEmployee(Integer id, Employee emp);
	void deleteEmployee(Integer id);
	List<Employee> getEmployeeByDept(String department);
}
