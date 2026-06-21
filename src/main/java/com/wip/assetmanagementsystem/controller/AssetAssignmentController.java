package com.wip.assetmanagementsystem.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.wip.assetmanagementsystem.dto.AssetAssignmentDTO;
import com.wip.assetmanagementsystem.entity.AssetAssignment;
import com.wip.assetmanagementsystem.entity.Employee;
import com.wip.assetmanagementsystem.repository.EmployeeRepository;
import com.wip.assetmanagementsystem.service.AssetAssignmentService;

@RestController
@RequestMapping("/api/assignments")
public class AssetAssignmentController {

    private final AssetAssignmentService assignmentService;
    private final EmployeeRepository employeeRepository;

    public AssetAssignmentController(AssetAssignmentService assignmentService,
                                     EmployeeRepository employeeRepository) {
        this.assignmentService = assignmentService;
        this.employeeRepository = employeeRepository;
    }

    @PostMapping
    public ResponseEntity<AssetAssignmentDTO> assignAsset(
            @Valid @RequestBody AssetAssignmentDTO assignmentDTO) {
        AssetAssignment saved = assignmentService.saveAssignment(assignmentDTO.toEntity());
        return ResponseEntity.ok(AssetAssignmentDTO.fromEntity(saved));
    }

    @GetMapping
    public ResponseEntity<List<AssetAssignmentDTO>> getAllAssignments() {
        List<AssetAssignmentDTO> list = assignmentService.getAllAssignment().stream()
                .map(AssetAssignmentDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/my")
    public ResponseEntity<List<AssetAssignmentDTO>> getMyAssignments(Authentication authentication) {
        Employee employee = employeeRepository.findByUsername(authentication.getName());
        if (employee == null) {
            return ResponseEntity.ok(List.of());
        }
        List<AssetAssignmentDTO> list = assignmentService
            .getAssignmentsByEmployeeId(employee.getId())
            .stream()
            .map(AssetAssignmentDTO::fromEntity)
            .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetAssignmentDTO> getAssignmentById(@PathVariable Integer id) {
        AssetAssignment assignment = assignmentService.getAssignmentById(id);
        return ResponseEntity.ok(AssetAssignmentDTO.fromEntity(assignment));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetAssignmentDTO> updateAssignment(
            @PathVariable Integer id,
            @Valid @RequestBody AssetAssignmentDTO assignmentDTO) {
        AssetAssignment updated = assignmentService.updateAssignment(id, assignmentDTO.toEntity());
        return ResponseEntity.ok(AssetAssignmentDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAssignment(@PathVariable Integer id) {
        assignmentService.deleteAssignment(id);
        return ResponseEntity.ok("Assignment Deleted Successfully");
    }
}
