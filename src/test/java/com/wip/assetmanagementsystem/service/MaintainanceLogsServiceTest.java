package com.wip.assetmanagementsystem.service;

import com.wip.assetmanagementsystem.entity.MaintainanceLogs;
import com.wip.assetmanagementsystem.repository.MaintainanceLogRepository;
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

public class MaintainanceLogsServiceTest {

    @Mock
    private MaintainanceLogRepository
            maintenanceLogRepository;

    @InjectMocks
    private MaintainanceLogsServiceImpl
            maintainanceLogsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveLog() {
        MaintainanceLogs log = new MaintainanceLogs();
        log.setDescription("First service");

        when(maintenanceLogRepository.save(log))
            .thenReturn(log);

        MaintainanceLogs saved =
            maintainanceLogsService.saveLog(log);

        assertNotNull(saved);
        assertEquals("First service",
            saved.getDescription());
    }

    @Test
    void testGetAllLogs() {
        MaintainanceLogs l1 = new MaintainanceLogs();
        l1.setDescription("Service 1");
        MaintainanceLogs l2 = new MaintainanceLogs();
        l2.setDescription("Service 2");

        when(maintenanceLogRepository.findAll())
            .thenReturn(Arrays.asList(l1, l2));

        List<MaintainanceLogs> list =
            maintainanceLogsService.getAllLogs();

        assertEquals(2, list.size());
    }

    @Test
    void testGetLogById() {
        MaintainanceLogs log = new MaintainanceLogs();
        log.setId(1);
        log.setDescription("First service");

        when(maintenanceLogRepository.findById(1))
            .thenReturn(Optional.of(log));

        MaintainanceLogs found =
            maintainanceLogsService.getLogById(1);

        assertNotNull(found);
        assertEquals("First service",
            found.getDescription());
    }

    @Test
    void testDeleteLog() {
        MaintainanceLogs log = new MaintainanceLogs();
        log.setId(1);

        when(maintenanceLogRepository.findById(1))
            .thenReturn(Optional.of(log));
        doNothing().when(maintenanceLogRepository)
            .deleteById(1);

        maintainanceLogsService.deleteLog(1);

        verify(maintenanceLogRepository, times(1))
            .deleteById(1);
    }
}