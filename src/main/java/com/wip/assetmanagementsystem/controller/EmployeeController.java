package com.wip.assetmanagementsystem.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wip.assetmanagementsystem.dto.EmployeeDTO;
import com.wip.assetmanagementsystem.entity.Employee;
import com.wip.assetmanagementsystem.service.EmployeeService;

@RestController
@RequestMapping("/api/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping
    public ResponseEntity<EmployeeDTO> saveEmployee(
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        Employee saved = employeeService.saveEmployee(employeeDTO.toEntity());
        return ResponseEntity.ok(EmployeeDTO.fromEntity(saved));
    }

    @GetMapping
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
        List<EmployeeDTO> list = employeeService.getAllEmployees().stream()
                .map(EmployeeDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(
            @PathVariable Integer id) {

        Employee employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(EmployeeDTO.fromEntity(employee));
    }

//    @GetMapping("/email/{email}")
//    public ResponseEntity<Employee> getEmployeeByEmail(
//            @PathVariable String email) {
//
//        return ResponseEntity.ok(
//                employeeService.getEmployeeByEmail(email));
//    }
//
//    @GetMapping("/department/{department}")
//    public ResponseEntity<List<Employee>> getEmployeesByDepartment(
//            @PathVariable String department) {
//
//        return ResponseEntity.ok(
//                employeeService.getEmployeesByDepartment(department));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable Integer id,
            @Valid @RequestBody EmployeeDTO employeeDTO) {

        Employee updated = employeeService.updateEmployee(id, employeeDTO.toEntity());
        return ResponseEntity.ok(EmployeeDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteEmployee(
            @PathVariable Integer id) {

        employeeService.deleteEmployee(id);
        return ResponseEntity.ok("Employee Deleted Successfully");
    }
}