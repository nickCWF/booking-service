package com.example.prototype2.customer;

import java.util.ArrayList;

public class Booking {

    private String bookID;
    private String barberID;
    private String barberServiceID;
    private String bookTime;
    private String bookDate;
    private String status;
    private String customerID;
    private String barberName;
    private String barberServiceName;
    private Double calculateBarberServiceCommission;
//    private ArrayList<String> barberServiceNameList;
//
//    public ArrayList<String> getBarberServiceNameList() {
//        return barberServiceNameList;
//    }
//
//    public void setBarberServiceNameList(ArrayList barberServiceNameList) {
//        this.barberServiceNameList = barberServiceNameList;
//    }

    public Double getCalculateBarberServiceCommission() {
        return calculateBarberServiceCommission;
    }

    public void setCalculateBarberServiceCommisson(Double calculateBarberServiceCommission) {
        this.calculateBarberServiceCommission = calculateBarberServiceCommission;
    }


    public Booking(){

    }

    public Booking(String bookID, String barberID, String barberServiceID, String bookTime, String bookDate, String status, String customerID, String barberName, String barberServiceName) {
        this.bookID = bookID;
        this.barberID = barberID;
        this.barberServiceID = barberServiceID;
        this.bookTime = bookTime;
        this.bookDate = bookDate;
        this.status = status;
        this.customerID = customerID;
        this.barberName = barberName;
//        this.barberServiceName = barberServiceName;
    }

    public String getBarberName() {
        return barberName;
    }

    public void setBarberName(String barberName) {
        this.barberName = barberName;
    }

    public String getBarberServiceName() {
        return barberServiceName;
    }

    public void setBarberServiceName(String barberServiceName) {
        this.barberServiceName = barberServiceName;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getBookDate() {
        return bookDate;
    }

    public void setBookDate(String bookDate) {
        this.bookDate = bookDate;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getBarberID() {
        return barberID;
    }

    public void setBarberID(String barberID) {
        this.barberID = barberID;
    }

    public String getBarberServiceID() {
        return barberServiceID;
    }

    public void setBarberServiceID(String barberServiceID) {
        this.barberServiceID = barberServiceID;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
