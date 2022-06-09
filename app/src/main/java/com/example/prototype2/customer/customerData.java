package com.example.prototype2.customer;

public class customerData {

    static String Username;
    String Email;
    String ContactNumber;
    String isClient;
    static String barberName;

    public customerData(){}

    public customerData(String username, String email, String contactNumber, String isClient) {
        Username = username;
        Email = email;
        ContactNumber = contactNumber;
        this.isClient = isClient;
    }

    public static String getBarberName() {
        return barberName;
    }

    public static void setBarberName(String barberName) {
        customerData.barberName = barberName;
    }

    public static String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getIsClient() {
        return isClient;
    }

    public void setIsClient(String isClient) {
        this.isClient = isClient;
    }
}
