package com.example.foovery;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Order implements Serializable {
    String id;
    String uid;
    String did;
    double cost;
    ArrayList<HashMap<String, Object>> ord;
    double Lat;
    double Lon;
    String userPhone;
    String riderPhone;
    String otp;
    String otp2;
    double locDist;

    public double getLocDist() {
        return locDist;
    }

    public void setLocDist(double locDist) {
        this.locDist = locDist;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public Order(){

    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public double getCost() {
        return cost;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }

    public ArrayList<HashMap<String, Object>> getOrd() {
        return ord;
    }

    public void setOrd(ArrayList<HashMap<String, Object>> ord) {
        this.ord = ord;
    }

    public double getLat() {
        return Lat;
    }

    public void setLat(double lat) {
        Lat = lat;
    }

    public double getLon() {
        return Lon;
    }

    public void setLon(double lon) {
        Lon = lon;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) { this.riderPhone = riderPhone; }

    public String getOtp() { return otp; }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getOtp2() { return otp2; }

    public void setOtp2(String otp2) {
        this.otp2 = otp2;
    }
}
