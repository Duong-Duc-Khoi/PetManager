package org.example.Models;

import java.util.Date;

public class PetLog {
    private int logId;
    private int petId;
    private String status;
    private String note;
    private Date createdAt;
    private Integer userId;

    public PetLog(int logId, int petId, String status, String note, Date createdAt, Integer userId) {
        this.logId = logId;
        this.petId = petId;
        this.status = status;
        this.note = note;
        this.createdAt = createdAt;
        this.userId = userId;
    }

    public PetLog() {
    }

    public int getLogId() {
        return logId;
    }

    public void setLogId(int logId) {
        this.logId = logId;
    }

    public int getPetId() {
        return petId;
    }

    public void setPetId(int petId) {
        this.petId = petId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
