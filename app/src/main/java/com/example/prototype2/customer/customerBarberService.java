package com.example.prototype2.customer;

public class customerBarberService {
    private String barberServiceName,barberServiceID;
    private String barberServicePrice,barberServiceCommission;
    private Double calculateBarberServiceCommission;
    private Double barberServiceDuration;


    public customerBarberService(){
    }

    public customerBarberService(String barberServiceName, String barberServiceID, String barberServicePrice, String barberServiceCommisson, boolean isSelected,  Double calculateBarberServiceCommission, Double barberServiceDuration) {
        this.barberServiceName = barberServiceName;
        this.barberServiceID = barberServiceID;
        this.barberServicePrice = barberServicePrice;
        this.barberServiceCommission = barberServiceCommisson;
        this.isSelected = isSelected;
        this.calculateBarberServiceCommission =  calculateBarberServiceCommission;
        this.barberServiceDuration = barberServiceDuration;
    }

    public Double getBarberServiceDuration() {
        return barberServiceDuration;
    }

    public void setBarberServiceDuration(Double barberServiceDuration) {
        this.barberServiceDuration = barberServiceDuration;
    }

    public Double getCalculateBarberServiceCommission() {
        return calculateBarberServiceCommission;
    }

    public void setCalculateBarberServiceCommission(Double calculateBarberServiceCommisson) {
        this.calculateBarberServiceCommission = calculateBarberServiceCommisson;
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

    public String getBarberServiceCommission() {
        return barberServiceCommission;
    }

    public void setBarberServiceCommission(String barberServiceCommission) {
        this.barberServiceCommission = barberServiceCommission;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected;


}
