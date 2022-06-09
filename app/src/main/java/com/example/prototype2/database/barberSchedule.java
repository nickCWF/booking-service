package com.example.prototype2.database;

public class barberSchedule {

    String checkTime, barberID;
    public barberSchedule(){}

    public barberSchedule(String checkTime, String barberID) {
        this.checkTime = checkTime;
        this.barberID = barberID;
    }

    public String getCheckTime() {
        return checkTime;
    }

    public void setCheckTime(String checkTime) {
        this.checkTime = checkTime;
    }

    public String getBarberID() {
        return barberID;
    }

    public void setBarberID(String barberID) {
        this.barberID = barberID;
    }
}
