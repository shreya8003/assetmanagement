package com.wip.assetmanagementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wip.assetmanagementsystem.entity.AssetAssignment;
import com.wip.assetmanagementsystem.repository.AssetAssignmentRepository;

@Service
public class AssetAssignmentServiceImpl implements AssetAssignmentService {

    private final AssetAssignmentRepository assignmentRepository;

    public AssetAssignmentServiceImpl(
            AssetAssignmentRepository assignmentRepository) {
        this.assignmentRepository = assignmentRepository;
    }

    @Override
    public AssetAssignment saveAssignment(AssetAssignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Override
    public List<AssetAssignment> getAllAssignment() {
        return assignmentRepository.findAll();
    }

    @Override
    public AssetAssignment getAssignmentById(Integer id) {
        return assignmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Assignment Not Found"));
    }

    @Override
    public AssetAssignment updateAssignment(Integer id,
                                            AssetAssignment assignment) {

        AssetAssignment existing = getAssignmentById(id);

        existing.setAsset(assignment.getAsset());
        existing.setEmployee(assignment.getEmployee());
        existing.setAssignedDate(assignment.getAssignedDate());
        existing.setReturnDate(assignment.getReturnDate());

        return assignmentRepository.save(existing);
    }

    @Override
    public void deleteAssignment(Integer id) {
        assignmentRepository.deleteById(id);
    }

    @Override
    public List<AssetAssignment> getAssignmentsByEmployeeId(Integer employeeId) {
        return assignmentRepository.findByEmployeeId(employeeId);
    }
}