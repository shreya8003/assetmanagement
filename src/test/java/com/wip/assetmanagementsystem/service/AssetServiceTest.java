package com.wip.assetmanagementsystem.service;

import com.wip.assetmanagementsystem.entity.Asset;
import com.wip.assetmanagementsystem.repository.AssetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class AssetServiceTest {

    @Mock
    private AssetRepository assetRepository;

    @InjectMocks
    private AssetServiceImpl assetService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveAsset() {
        Asset asset = new Asset();
        asset.setName("Dell Laptop");
        asset.setStatus("active");

        when(assetRepository.save(asset))
            .thenReturn(asset);

        Asset saved = assetService.addAsset(asset);

        assertNotNull(saved);
        assertEquals("Dell Laptop", saved.getName());
    }

    @Test
    void testGetAllAssets() {
        Asset a1 = new Asset();
        a1.setName("Laptop");
        Asset a2 = new Asset();
        a2.setName("Printer");

        when(assetRepository.findAll())
            .thenReturn(Arrays.asList(a1, a2));

        List<Asset> list = assetService.getAllAssets();

        assertEquals(2, list.size());
    }

    @Test
    void testGetAssetById() {
        Asset asset = new Asset();
        asset.setId(1);
        asset.setName("Dell Laptop");

        when(assetRepository.findById(1))
            .thenReturn(Optional.of(asset));

        Asset found = assetService.getAssetById(1);

        assertNotNull(found);
        assertEquals("Dell Laptop", found.getName());
    }

    @Test
    void testDeleteAsset() {
        Asset asset = new Asset();
        asset.setId(1);

        when(assetRepository.findById(1))
            .thenReturn(Optional.of(asset));
        doNothing().when(assetRepository)
            .deleteById(1);

        assetService.deleteAsset(1);

        verify(assetRepository, times(1))
            .deleteById(1);
    }
}