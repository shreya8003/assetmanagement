package com.wip.assetmanagementsystem.controller;

import java.util.List;
import jakarta.validation.Valid;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.wip.assetmanagementsystem.dto.MaintainanceLogsDTO;
import com.wip.assetmanagementsystem.entity.MaintainanceLogs;
import com.wip.assetmanagementsystem.service.MaintainanceLogsService;


@RestController
@RequestMapping("/api/maintenance")
public class MaintenanceLogController {

    private final MaintainanceLogsService maintenanceLogService;

    public MaintenanceLogController(MaintainanceLogsService maintenanceLogService) {
        this.maintenanceLogService = maintenanceLogService;
    }

    @PostMapping
    public ResponseEntity<MaintainanceLogsDTO> saveMaintenanceLog(
            @Valid @RequestBody MaintainanceLogsDTO maintenanceLogDTO,
            Authentication authentication) {

        MaintainanceLogs log = maintenanceLogDTO.toEntity();
        if (authentication != null) {
            log.setSubmittedByUsername(authentication.getName());
        }
        MaintainanceLogs saved = maintenanceLogService.saveLog(log);
        return ResponseEntity.ok(MaintainanceLogsDTO.fromEntity(saved));
    }

    @GetMapping
    public ResponseEntity<List<MaintainanceLogsDTO>> getAllMaintenanceLogs() {
        List<MaintainanceLogsDTO> list = maintenanceLogService.getAllLogs().stream()
                .map(MaintainanceLogsDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/my")
    public ResponseEntity<List<MaintainanceLogsDTO>> getMyMaintenanceLogs(Authentication authentication) {
        List<MaintainanceLogsDTO> list = maintenanceLogService
                .getLogsByUsername(authentication.getName()).stream()
                .map(MaintainanceLogsDTO::fromEntity)
                .toList();
        return ResponseEntity.ok(list);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MaintainanceLogsDTO> getMaintenanceLogById(@PathVariable Integer id) {
        return ResponseEntity.ok(MaintainanceLogsDTO.fromEntity(maintenanceLogService.getLogById(id)));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MaintainanceLogsDTO> updateMaintenanceLog(
            @PathVariable Integer id,
            @Valid @RequestBody MaintainanceLogsDTO maintenanceLogDTO) {

        MaintainanceLogs updated = maintenanceLogService.updateLog(id, maintenanceLogDTO.toEntity());
        return ResponseEntity.ok(MaintainanceLogsDTO.fromEntity(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMaintenanceLog(@PathVariable Integer id) {
        maintenanceLogService.deleteLog(id);
        return ResponseEntity.ok("Maintenance Log Deleted Successfully");
    }
}
