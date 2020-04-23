package com.example.foovery;

import java.util.ArrayList;
import java.util.HashMap;

public class Order {
    String id;
    String uid;
    double cost;
    ArrayList<HashMap<String, Object>> ord;
    double Lat;
    double Lon;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    String phone;

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
