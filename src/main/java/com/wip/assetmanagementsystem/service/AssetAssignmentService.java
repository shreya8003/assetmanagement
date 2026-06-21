package com.wip.assetmanagementsystem.service;
import com.wip.assetmanagementsystem.entity.AssetAssignment;
import java.util.List;

public interface AssetAssignmentService {
	AssetAssignment saveAssignment(AssetAssignment assignment);
    List<AssetAssignment>getAllAssignment();
    AssetAssignment getAssignmentById(Integer id);
    AssetAssignment updateAssignment(Integer id,AssetAssignment assignment);
    void deleteAssignment(Integer id);
    List<AssetAssignment> getAssignmentsByEmployeeId(Integer employeeId);
}