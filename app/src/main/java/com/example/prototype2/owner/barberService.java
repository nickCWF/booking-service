package com.example.prototype2.owner;

public class barberService {

    private String barberServiceName,barberServiceID;
    private String barberServicePrice,barberServiceCommisson;
    private boolean isSelected;
    private Double calculateBarberServiceCommisson;

    barberService(){

    }

    public barberService(String barberServiceName, String barberServiceID, String barberServicePrice, String barberServiceCommisson, Double calculateBarberServiceCommisson) {
        this.barberServiceName = barberServiceName;
        this.barberServiceID = barberServiceID;
        this.barberServicePrice = barberServicePrice;
        this.barberServiceCommisson = barberServiceCommisson;
        this.calculateBarberServiceCommisson = calculateBarberServiceCommisson;

    }

    public Double getCalculateBarberServiceCommisson() {
        return calculateBarberServiceCommisson;
    }

    public void setCalculateBarberServiceCommisson(Double calculateBarberServiceCommisson) {
        this.calculateBarberServiceCommisson = calculateBarberServiceCommisson;
    }

    public String getBarberServiceName() {
        return barberServiceName;
    }

    public void setBarberServiceName(String barberServiceName) {
        this.barberServiceName = barberServiceName;
    }

    public String getBarberServiceID() {
        return barberServiceID;
    }

    public void setBarberServiceID(String barberServiceID) {
        this.barberServiceID = barberServiceID;
    }

    public String getBarberServicePrice() {
        return barberServicePrice;
    }

    public void setBarberServicePrice(String barberServicePrice) {
        this.barberServicePrice = barberServicePrice;
    }

    public String getBarberServiceCommisson() {
        return barberServiceCommisson;
    }

    public void setBarberServiceCommisson(String barberServiceCommisson) {
        this.barberServiceCommisson = barberServiceCommisson;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
