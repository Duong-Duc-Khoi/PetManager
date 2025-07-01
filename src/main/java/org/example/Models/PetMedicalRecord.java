package org.example.Models;

import java.util.Date;

public class PetMedicalRecord {
    private int recordId;           // Mã hồ sơ y tế (duy nhất)
    private int petId;              // Mã thú cưng (liên kết với bảng Pet)
    private String type;            // Loại hồ sơ (ví dụ: "Khám định kỳ", "Tiêm chủng", "Điều trị")
    private String description;     // Mô tả chi tiết tình trạng, chẩn đoán, phương pháp điều trị...
    private String vetName;         // Tên bác sĩ thú y phụ trách
    private Date date;              // Ngày khám hoặc điều trị
    private Date nextAppointment;   // Ngày hẹn khám tiếp theo (nếu có)

    public PetMedicalRecord(int recordId, int petId, String type, String description, String vetName, Date date, Date nextAppointment) {
        this.recordId = recordId;
        this.petId = petId;
        this.type = type;
        this.description = description;
        this.vetName = vetName;
        this.date = date;
        this.nextAppointment = nextAppointment;
    }

    public PetMedicalRecord() {
    }

    public int getRecordId() {
        return recordId;
    }

    public void setRecordId(int recordId) {
        this.recordId = recordId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getVetName() {
        return vetName;
    }

    public void setVetName(String vetName) {
        this.vetName = vetName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getNextAppointment() {
        return nextAppointment;
    }

    public void setNextAppointment(Date nextAppointment) {
        this.nextAppointment = nextAppointment;
    }
}
