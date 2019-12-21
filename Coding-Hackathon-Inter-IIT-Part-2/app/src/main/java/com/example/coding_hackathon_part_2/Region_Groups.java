package com.example.coding_hackathon_part_2;

import java.io.Serializable;

public class Region_Groups {
    private String id;
    private double latitude;
    private double longitude;
    private Integer num_complaints;

    public Region_Groups(String id,Object latitude, Object longitude, Object num_complaints) {
        this.id = id;
        this.latitude = Double.valueOf(latitude.toString());
        this.longitude = Double.valueOf(longitude.toString());
        this.num_complaints = Integer.valueOf(num_complaints.toString());
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public Integer getNum_complaints() {
        return num_complaints;
    }

    public void setNum_complaints(Integer num_complaints) {
        this.num_complaints = num_complaints;
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
