package com.example.prototype2.barber;

public class leaveApplyData {
    String barberID, barberName, leaveID, reason, status;
    Long DateEnd, DateStart;

    public leaveApplyData(){

    }

    public leaveApplyData(String barberID, String barberName, String leaveID, String reason, String status, Long dateEnd, Long dateStart) {
        this.barberID = barberID;
        this.barberName = barberName;
        this.leaveID = leaveID;
        this.reason = reason;
        this.status = status;
        DateEnd = dateEnd;
        DateStart = dateStart;
    }

    public String getBarberID() {
        return barberID;
    }

    public void setBarberID(String barberID) {
        this.barberID = barberID;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getLeaveID() {
        return leaveID;
    }

    public void setLeaveID(String leaveID) {
        this.leaveID = leaveID;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getDateEnd() {
        return DateEnd;
    }

    public void setDateEnd(Long dateEnd) {
        DateEnd = dateEnd;
    }

    public Long getDateStart() {
        return DateStart;
    }

    public void setDateStart(Long dateStart) {
        DateStart = dateStart;
    }
}
