package com.wip.assetmanagementsystem.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.wip.assetmanagementsystem.dto.AssetDTO;
import com.wip.assetmanagementsystem.entity.Asset;
import com.wip.assetmanagementsystem.service.AssetService;

@RestController
@RequestMapping("/api/assets")
public class AssetController {

    private final AssetService assetService;

    public AssetController(AssetService assetService) {
        this.assetService = assetService;
    }

    @PostMapping
    public ResponseEntity<AssetDTO> saveAsset(
            @Valid @RequestBody AssetDTO assetDTO) {

        Asset saved = assetService.addAsset(assetDTO.toEntity());
        return ResponseEntity.ok(AssetDTO.fromEntity(saved));
    }

    @GetMapping
    public ResponseEntity<List<AssetDTO>> getAllAssets() {
        List<AssetDTO> list = assetService.getAllAssets().stream()
                .map(AssetDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AssetDTO> getAssetById(
            @PathVariable Integer id) {

        Asset asset = assetService.getAssetById(id);
        return ResponseEntity.ok(AssetDTO.fromEntity(asset));
    }

//    @GetMapping("/status/{status}")
//    public ResponseEntity<List<Asset>> getAssetsByStatus(
//            @PathVariable String status) {
//
//        return ResponseEntity.ok(
//                assetService.getAssetsByStatus(status));
//    }
//
//    @GetMapping("/serial/{serialNumber}")
//    public ResponseEntity<Asset> getAssetBySerialNumber(
//            @PathVariable String serialNumber) {
//
//        return ResponseEntity.ok(
//                assetService.getAssetBySerialNumber(serialNumber));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<AssetDTO> updateAsset(
            @PathVariable Integer id,
            @Valid @RequestBody AssetDTO assetDTO) {

        Asset updated = assetService.updateAsset(id, assetDTO.toEntity());
        return ResponseEntity.ok(AssetDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteAsset(
            @PathVariable Integer id) {

        assetService.deleteAsset(id);
        return ResponseEntity.ok("Asset Deleted Successfully");
    }
}