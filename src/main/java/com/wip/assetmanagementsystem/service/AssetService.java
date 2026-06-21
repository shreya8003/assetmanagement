package com.wip.assetmanagementsystem.service;

import java.util.List;

import com.wip.assetmanagementsystem.entity.Asset;

public interface AssetService {
	Asset addAsset(Asset asset);
	List<Asset> getAllAssets();
	Asset updateAsset(Integer id, Asset asset);
	Asset getAssetById(Integer id);
	void deleteAsset(Integer id);
	List<Asset> getAssetByStatus(String status);
}
