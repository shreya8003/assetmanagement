package com.wip.assetmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.assetmanagementsystem.entity.Employee;
import com.wip.assetmanagementsystem.security.CustomUserDetailsService;
import com.wip.assetmanagementsystem.security.JwtUtil;
import com.wip.assetmanagementsystem.service.EmployeeService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "admin")
@ExtendWith(SpringExtension.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

	/*
	 * @Test void testSaveEmployee() throws Exception { Employee employee = new
	 * Employee(); employee.setId(1); employee.setName("John Doe");
	 * 
	 * when(employeeService.saveEmployee(any(Employee.class)))
	 * .thenReturn(employee);
	 * 
	 * mockMvc.perform(post("/api/employees") .with(csrf())
	 * .contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(employee)))
	 * .andExpect(status().isCreated()); }
	 */

    @Test
    void testGetAllEmployees() throws Exception {
        Employee e1 = new Employee();
        e1.setName("John");

        when(employeeService.getAllEmployees())
            .thenReturn(Arrays.asList(e1));

        mockMvc.perform(get("/api/employees"))
            .andExpect(status().isOk());
    }

	/*
	 * @Test void testGetEmployeeById() throws Exception { Employee employee = new
	 * Employee(); employee.setId(1); employee.setName("John Doe");
	 * 
	 * when(employeeService.getEmployeeById(1)) .thenReturn(employee);
	 * 
	 * mockMvc.perform(get("/api/employees/1")) .andExpect(status().isOk()); }
	 */
	/*
	 * @Test void testUpdateEmployee() throws Exception { Employee updated = new
	 * Employee(); updated.setId(1); updated.setName("John Updated");
	 * 
	 * when(employeeService.updateEmployee(eq(1), any(Employee.class)))
	 * .thenReturn(updated);
	 * 
	 * mockMvc.perform(put("/api/employees/1") .with(csrf())
	 * .contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(updated)))
	 * .andExpect(status().isOk()); }
	 */

    @Test
    void testDeleteEmployee() throws Exception {
        doNothing().when(employeeService).deleteEmployee(1);

        mockMvc.perform(delete("/api/employees/1")
                .with(csrf()))
            .andExpect(status().isOk());
    }
}