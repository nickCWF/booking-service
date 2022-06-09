package com.example.prototype2.barber;

import java.util.ArrayList;

public class barberWorkingData {

    String barberID, barberName,
            barberServiceId, barberServiceName,
            bookDate, bookID, bookTime, customerID,
            status, customerName, bookTotalPrice, bookTotalCommission;

    Double schedule, bookTotalDuration;

    private ArrayList<String> barberServiceNameList;
    private ArrayList bookPriceList;

    public barberWorkingData() {

    }

    public barberWorkingData(String barberName, String barberServiceName, String bookTime, ArrayList barberServiceNameList, String status, String customerName, String bookID) {
        this.barberName = barberName;
        this.barberServiceName = barberServiceName;
        this.bookTime = bookTime;
        this.barberServiceNameList = barberServiceNameList;
        this.status = status;
        this.bookID = bookID;
        this.customerName = customerName;
    }

    public barberWorkingData(String barberID, String barberName, String barberServiceId, String barberServiceName, String bookDate, String bookID, String bookTime, String customerID, String status, String customerName, ArrayList<String> barberServiceNameList, ArrayList bookPriceList) {
        this.barberID = barberID;
        this.barberName = barberName;
        this.barberServiceId = barberServiceId;
        this.barberServiceName = barberServiceName;
        this.bookDate = bookDate;
        this.bookID = bookID;
        this.bookTime = bookTime;
        this.customerID = customerID;
        this.status = status;
        this.customerName = customerName;
        this.barberServiceNameList = barberServiceNameList;
        this.bookPriceList = bookPriceList;
    }

    public ArrayList getBookPriceList() {
        return bookPriceList;
    }

    public void setBookPriceList(ArrayList bookPriceList) {
        this.bookPriceList = bookPriceList;
    }

    public ArrayList<String> getBarberServiceNameList() {
        return barberServiceNameList;
    }

    public void setBarberServiceNameList(ArrayList<String> barberServiceNameList) {
        this.barberServiceNameList = barberServiceNameList;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
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

    public String getBarberServiceId() {
        return barberServiceId;
    }

    public void setBarberServiceId(String barberServiceId) {
        this.barberServiceId = barberServiceId;
    }

    public String getBarberServiceName() {
        return barberServiceName;
    }

    public void setBarberServiceName(String barberServiceName) {
        this.barberServiceName = barberServiceName;
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

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
