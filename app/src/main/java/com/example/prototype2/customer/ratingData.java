package com.example.prototype2.customer;

public class ratingData {

    String barberID, bookID, comment, customerID, rateID;
    Double rate;

    public ratingData(){}

    public ratingData(String barberID, String bookID, String comment, String customerID, String rateID, Double rate) {
        this.barberID = barberID;
        this.bookID = bookID;
        this.comment = comment;
        this.customerID = customerID;
        this.rateID = rateID;
        this.rate = rate;
    }

    public String getBarberID() {
        return barberID;
    }

    public void setBarberID(String barberID) {
        this.barberID = barberID;
    }

    public String getBookID() {
        return bookID;
    }

    public void setBookID(String bookID) {
        this.bookID = bookID;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerID() {
        return customerID;
    }

    public void setCustomerID(String customerID) {
        this.customerID = customerID;
    }

    public String getRateID() {
        return rateID;
    }

    public void setRateID(String rateID) {
        this.rateID = rateID;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }
}
