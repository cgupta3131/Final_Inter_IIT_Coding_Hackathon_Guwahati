package com.example.coding_hackaton_guwahati;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class Projects
{
    private String id;
    private String name;
    private String report;
    private String contractor_remarks;
    private String description;
    private Double latitude;
    private Double longitude;
    private Integer num_users;
    private Integer user_status;
    private Integer contractor_status;
    private Timestamp time;
    private DocumentReference contract_ref;

    public Projects(String id, Object name, Object report, Object contractor_remarks, Object description, Object latitude, Object longitude, Object num_users, Object user_status, Object contractor_status, Object time, Object contract_ref)
    {
        this.id = id;
        this.name = name.toString();
        this.report = report.toString();
        this.contractor_remarks = contractor_remarks.toString();
        this.description = description.toString();
        this.latitude = Double.valueOf(latitude.toString());
        this.longitude = Double.valueOf(longitude.toString());
        this.num_users = Integer.valueOf(num_users.toString());
        this.user_status = Integer.valueOf(user_status.toString());
        this.contractor_status = Integer.valueOf(contractor_status.toString());
        this.time = (Timestamp) time;
        this.contract_ref = (DocumentReference) contract_ref;
    }

    public Projects() {
    }

    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getReport()
    {
        return report;
    }

    public void setReport(String report)
    {
        this.report = report;
    }

    public String getContractor_remarks()
    {
        return contractor_remarks;
    }

    public void setContractor_remarks(String contractor_remarks)
    {
        this.contractor_remarks = contractor_remarks;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Integer getNum_users()
    {
        return num_users;
    }

    public void setNum_users(Integer num_users)
    {
        this.num_users = num_users;
    }

    public Integer getUser_status()
    {
        return user_status;
    }

    public void setUser_status(Integer user_status)
    {
        this.user_status = user_status;
    }

    public Integer getContractor_status()
    {
        return contractor_status;
    }

    public void setContractor_status(Integer contractor_status)
    {
        this.contractor_status = contractor_status;
    }

    public Timestamp getTime()
    {
        return time;
    }

    public void setTime(Timestamp time)
    {
        this.time = time;
    }

    public DocumentReference getContract_ref()
    {
        return contract_ref;
    }

    public void setContract_ref(DocumentReference contract_ref)
    {
        this.contract_ref = contract_ref;
    }
}
