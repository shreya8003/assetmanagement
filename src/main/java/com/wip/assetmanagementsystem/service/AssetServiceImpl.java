package com.wip.assetmanagementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wip.assetmanagementsystem.entity.Asset;
import com.wip.assetmanagementsystem.repository.AssetRepository;

@Service
public class AssetServiceImpl implements AssetService {

    private final AssetRepository assetRepository;

    public AssetServiceImpl(AssetRepository assetRepository) {
        this.assetRepository = assetRepository;
    }

    @Override
    public Asset addAsset(Asset asset) {
        return assetRepository.save(asset);
    }

    @Override
    public List<Asset> getAllAssets() {
        return assetRepository.findAll();
    }

    @Override
    public Asset getAssetById(Integer id) {
        return assetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asset Not Found"));
    }

    @Override
    public Asset updateAsset(Integer id, Asset asset) {

        Asset existing = getAssetById(id);

        existing.setName(asset.getName());
        existing.setSerialno(asset.getSerialno());
        existing.setStatus(asset.getStatus());
        existing.setPurchasePrice(asset.getPurchasePrice());
        existing.setPurchaseDate(asset.getPurchaseDate());
        existing.setCategory(asset.getCategory());

        return assetRepository.save(existing);
    }

    @Override
    public void deleteAsset(Integer id) {
        assetRepository.deleteById(id);
    }

    @Override
    public List<Asset> getAssetByStatus(String status) {
        return assetRepository.findByStatus(status);
    }
}