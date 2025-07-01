package org.example.Controllers;

import org.example.Models.PetLog;
import org.example.Services.PetLogService;

import java.util.List;

public class
PetLogController {
    private PetLogService petLogService;
    public PetLogController() {
        petLogService = new PetLogService();
    }
    public List<PetLog> filterLogs(Integer petId, Integer userId, String statusFilter) {
        return petLogService.filterLogs(petId, userId, statusFilter);
    }

    public boolean addLog(PetLog log) {
        return petLogService.addLog(log);
    }
    public boolean updateLog(PetLog log) {
        return petLogService.updateLog(log);
    }

    public boolean deleteLog(int logId) {
        return petLogService.deleteLog(logId);
    }
}
