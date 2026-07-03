package com.i2iacademy.ignite;

public class Subscriber {

    private String customerId;
    private double dataUsage;
    private int smsUsage;
    private int callUsage;

    public Subscriber() {
    }

    public Subscriber(String customerId, double dataUsage, int smsUsage, int callUsage) {
        this.customerId = customerId;
        this.dataUsage = dataUsage;
        this.smsUsage = smsUsage;
        this.callUsage = callUsage;
    }

    public String getCustomerId() {
        return customerId;
    }

    public double getDataUsage() {
        return dataUsage;
    }

    public void setDataUsage(double dataUsage) {
        this.dataUsage = dataUsage;
    }

    public int getSmsUsage() {
        return smsUsage;
    }

    public void setSmsUsage(int smsUsage) {
        this.smsUsage = smsUsage;
    }

    public int getCallUsage() {
        return callUsage;
    }

    public void setCallUsage(int callUsage) {
        this.callUsage = callUsage;
    }

    @Override
    public String toString() {
        return "CustomerId: " + customerId
                + " | Data: " + String.format("%.2f", dataUsage) + " MB"
                + " | SMS: " + smsUsage
                + " | Call: " + callUsage + " min";
    }
}