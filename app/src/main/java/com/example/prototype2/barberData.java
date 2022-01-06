package com.example.prototype2;

public class barberData {

    String barberID, barberEmail, barberSalary, barberName;

    public barberData(){

    }

    public barberData(String barberID, String barberEmail, String barberSalary, String barberName) {
        this.barberID = barberID;
        this.barberEmail = barberEmail;
        this.barberSalary = barberSalary;
        this.barberName = barberName;
    }

    public String getBarberID() {
        return barberID;
    }

    public void setBarberID(String barberID) {
        this.barberID = barberID;
    }

    public String getBarberEmail() {
        return barberEmail;
    }

    public void setBarberEmail(String barberEmail) {
        this.barberEmail = barberEmail;
    }

    public String getBarberSalary() {
        return barberSalary;
    }

    public void setBarberSalary(String barberSalary) {
        this.barberSalary = barberSalary;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }
}
