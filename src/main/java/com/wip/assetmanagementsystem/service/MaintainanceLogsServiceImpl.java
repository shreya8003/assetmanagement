package com.wip.assetmanagementsystem.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.wip.assetmanagementsystem.entity.MaintainanceLogs;
import com.wip.assetmanagementsystem.repository.MaintainanceLogRepository;

@Service
public class MaintainanceLogsServiceImpl implements MaintainanceLogsService {

    private final MaintainanceLogRepository maintenanceLogRepository;

    public MaintainanceLogsServiceImpl(MaintainanceLogRepository maintenanceLogRepository) {
        this.maintenanceLogRepository = maintenanceLogRepository;
    }

    @Override
    public MaintainanceLogs saveLog(MaintainanceLogs maintenanceLog) {
        if (maintenanceLog.getMaintainanceDate() == null) {
            maintenanceLog.setMaintainanceDate(new java.sql.Date(System.currentTimeMillis()));
        }
        return maintenanceLogRepository.save(maintenanceLog);
    }

    @Override
    public List<MaintainanceLogs> getAllLogs() {
        return maintenanceLogRepository.findAll();
    }

    @Override
    public List<MaintainanceLogs> getLogsByUsername(String username) {
        return maintenanceLogRepository.findBySubmittedByUsername(username);
    }

    @Override
    public MaintainanceLogs getLogById(Integer id) {
        return maintenanceLogRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Maintenance Log Not Found"));
    }

    @Override
    public MaintainanceLogs updateLog(Integer id, MaintainanceLogs maintenanceLog) {
        MaintainanceLogs existing = getLogById(id);
        existing.setAsset(maintenanceLog.getAsset());
        existing.setMaintainanceDate(maintenanceLog.getMaintainanceDate());
        existing.setDescription(maintenanceLog.getDescription());
        if (maintenanceLog.getStatus() != null) {
            existing.setStatus(maintenanceLog.getStatus());
        }
        // Clear rejection message when status is no longer Rejected
        if ("Rejected".equalsIgnoreCase(maintenanceLog.getStatus())) {
            existing.setRejectionMessage(maintenanceLog.getRejectionMessage());
        } else {
            existing.setRejectionMessage(null);
        }
        return maintenanceLogRepository.save(existing);
    }

    @Override
    public void deleteLog(Integer id) {
        maintenanceLogRepository.deleteById(id);
    }
}
