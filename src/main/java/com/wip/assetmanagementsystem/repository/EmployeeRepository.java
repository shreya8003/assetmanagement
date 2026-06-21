package com.wip.assetmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wip.assetmanagementsystem.entity.Employee;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Integer>{
	Employee findByEmail(String email);
	Employee findByUsername(String username);
	boolean existsByUsername(String username);
	List<Employee> findByCompany(String company);
}
