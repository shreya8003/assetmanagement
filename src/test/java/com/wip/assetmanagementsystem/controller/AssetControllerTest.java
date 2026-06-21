package com.wip.assetmanagementsystem.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.wip.assetmanagementsystem.entity.Asset;
import com.wip.assetmanagementsystem.security.CustomUserDetailsService;
import com.wip.assetmanagementsystem.security.JwtUtil;
import com.wip.assetmanagementsystem.service.AssetService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WithMockUser(username = "admin")
@ExtendWith(SpringExtension.class)
@WebMvcTest(AssetController.class)
public class AssetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AssetService assetService;

    @MockBean
    private JwtUtil jwtUtil;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private ObjectMapper objectMapper;

	/*
	 * @Test void testSaveAsset() throws Exception { Asset asset = new Asset();
	 * asset.setId(1); asset.setName("Dell Laptop");
	 * 
	 * when(assetService.saveAsset(any(Asset.class))) .thenReturn(asset);
	 * 
	 * mockMvc.perform(post("/api/assets") .with(csrf())
	 * .contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(asset)))
	 * .andExpect(status().isCreated()); }
	 */

    @Test
    void testGetAllAssets() throws Exception {
        Asset a1 = new Asset();
        a1.setName("Laptop");

        when(assetService.getAllAssets())
            .thenReturn(Arrays.asList(a1));

        mockMvc.perform(get("/api/assets"))
            .andExpect(status().isOk());
    }

    @Test
    void testGetAssetById() throws Exception {
        Asset asset = new Asset();
        asset.setId(1);
        asset.setName("Dell Laptop");

        when(assetService.getAssetById(1))
            .thenReturn(asset);

        mockMvc.perform(get("/api/assets/1"))
            .andExpect(status().isOk());
    }

	/*
	 * @Test void testUpdateAsset() throws Exception { Asset updated = new Asset();
	 * updated.setId(1); updated.setName("Updated Laptop");
	 * 
	 * when(assetService.updateAsset(eq(1), any(Asset.class))) .thenReturn(updated);
	 * 
	 * mockMvc.perform(put("/api/assets/1") .with(csrf())
	 * .contentType(MediaType.APPLICATION_JSON)
	 * .content(objectMapper.writeValueAsString(updated)))
	 * .andExpect(status().isOk()); }
	 */

    @Test
    void testDeleteAsset() throws Exception {
        doNothing().when(assetService).deleteAsset(1);

        mockMvc.perform(delete("/api/assets/1")
                .with(csrf()))
            .andExpect(status().isOk());
    }
}