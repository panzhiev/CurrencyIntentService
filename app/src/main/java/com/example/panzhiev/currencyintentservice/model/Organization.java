package com.example.panzhiev.currencyintentservice.model;

/**
 * Created by Tim on 07.04.2017.
 */

public class Organization {

    private String title;
    private int logo;
    private double eurASK;
    private double eurBID;
    private double rubASK;
    private double rubBID;
    private double usdASK;
    private double usdBID;

    public Organization(String title, int logo, double eurASK, double eurBID, double rubASK, double rubBID, double usdASK, double usdBID) {
        this.title = title;
        this.logo = logo;
        this.eurASK = eurASK;
        this.eurBID = eurBID;
        this.rubASK = rubASK;
        this.rubBID = rubBID;
        this.usdASK = usdASK;
        this.usdBID = usdBID;
    }

    public int getLogo() {
        return logo;
    }

    public void setLogo(int logo) {
        this.logo = logo;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getEurASK() {
        return eurASK;
    }

    public void setEurASK(double eurASK) {
        this.eurASK = eurASK;
    }

    public double getEurBID() {
        return eurBID;
    }

    public void setEurBID(double eurBID) {
        this.eurBID = eurBID;
    }

    public double getRubASK() {
        return rubASK;
    }

    public void setRubASK(double rubASK) {
        this.rubASK = rubASK;
    }

    public double getRubBID() {
        return rubBID;
    }

    public void setRubBID(double rubBID) {
        this.rubBID = rubBID;
    }

    public double getUsdASK() {
        return usdASK;
    }

    public void setUsdASK(double usdASK) {
        this.usdASK = usdASK;
    }

    public double getUsdBID() {
        return usdBID;
    }

    public void setUsdBID(double usdBID) {
        this.usdBID = usdBID;
    }

    @Override
    public String toString() {
        return "Organization{" +
                "title='" + title + '\'' +
                ", eurASK=" + eurASK +
                ", eurBID=" + eurBID +
                ", rubASK=" + rubASK +
                ", rubBID=" + rubBID +
                ", usdASK=" + usdASK +
                ", usdBID=" + usdBID +
                '}';
    }
}
