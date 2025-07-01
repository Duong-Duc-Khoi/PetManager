package org.example.Controllers;

import org.example.Models.PetMedicalRecord;
import org.example.Services.RecordLogService;

import java.util.List;

public class RecordLogController {
    private RecordLogService recordLogService;

    public RecordLogController() {
        recordLogService = new RecordLogService();
    }

    public List<PetMedicalRecord> getRecordLogService() {
        return recordLogService.getRecordLogService();
    }

    public boolean addRecordLogService(PetMedicalRecord record) {
        return recordLogService.addRecordLogService(record);
    }

    public boolean updateRecordLogService(PetMedicalRecord record) {
        return recordLogService.updateRecordLogService(record);
    }

    public boolean deleteRecordLogService(PetMedicalRecord record) {
        return recordLogService.deleteRecordLogService(record);
    }
}
