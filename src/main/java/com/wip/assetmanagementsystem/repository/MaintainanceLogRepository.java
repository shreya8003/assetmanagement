package com.wip.assetmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wip.assetmanagementsystem.entity.MaintainanceLogs;

@Repository
public interface MaintainanceLogRepository extends JpaRepository<MaintainanceLogs, Integer>{
	List<MaintainanceLogs> findByAssetId(Integer assetId);
	List<MaintainanceLogs> findBySubmittedByUsername(String username);
}
