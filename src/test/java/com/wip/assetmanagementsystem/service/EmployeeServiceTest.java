package com.wip.assetmanagementsystem.service;

import com.wip.assetmanagementsystem.entity.Employee;
import com.wip.assetmanagementsystem.repository.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveEmployee() {
        Employee employee = new Employee();
        employee.setName("John Doe");
        employee.setEmail("john@gmail.com");

        when(employeeRepository.save(employee))
            .thenReturn(employee);

        Employee saved =
            employeeService.saveEmployee(employee);

        assertNotNull(saved);
        assertEquals("John Doe", saved.getName());
    }

    @Test
    void testGetAllEmployees() {
        Employee e1 = new Employee();
        e1.setName("John");
        Employee e2 = new Employee();
        e2.setName("Jane");

        when(employeeRepository.findAll())
            .thenReturn(Arrays.asList(e1, e2));

        List<Employee> list =
            employeeService.getAllEmployees();

        assertEquals(2, list.size());
    }

    @Test
    void testGetEmployeeById() {
        Employee employee = new Employee();
        employee.setId(1);
        employee.setName("John Doe");

        when(employeeRepository.findById(1))
            .thenReturn(Optional.of(employee));

        Employee found =
            employeeService.getEmployeeById(1);

        assertNotNull(found);
        assertEquals("John Doe", found.getName());
    }

    @Test
    void testDeleteEmployee() {
        doNothing().when(employeeRepository)
            .deleteById(1);

        employeeService.deleteEmployee(1);

        verify(employeeRepository, times(1))
            .deleteById(1);
    }
}