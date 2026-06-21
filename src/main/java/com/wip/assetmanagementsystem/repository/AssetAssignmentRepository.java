package com.wip.assetmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wip.assetmanagementsystem.entity.AssetAssignment;

@Repository
public interface AssetAssignmentRepository extends JpaRepository<AssetAssignment, Integer>{
	List<AssetAssignment> findByEmployeeId(Integer employeeId);
	List<AssetAssignment> findByAssetId(Integer assetId);
}
