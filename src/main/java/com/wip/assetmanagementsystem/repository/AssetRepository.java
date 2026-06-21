package com.wip.assetmanagementsystem.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.wip.assetmanagementsystem.entity.Asset;

@Repository
public interface AssetRepository extends JpaRepository<Asset, Integer>{
	List<Asset> findByStatus(String status);
//	Asset findBySerialNo(String serialno);
}
