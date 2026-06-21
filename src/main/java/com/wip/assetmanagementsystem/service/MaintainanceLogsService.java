package com.wip.assetmanagementsystem.service;
import com.wip.assetmanagementsystem.entity.MaintainanceLogs;
import java.util.List;

public interface MaintainanceLogsService {
	MaintainanceLogs saveLog(MaintainanceLogs logs);
    List<MaintainanceLogs> getAllLogs();
    List<MaintainanceLogs> getLogsByUsername(String username);
    MaintainanceLogs getLogById(Integer id);
    MaintainanceLogs updateLog(Integer id, MaintainanceLogs logs);
    void deleteLog(Integer id);
}