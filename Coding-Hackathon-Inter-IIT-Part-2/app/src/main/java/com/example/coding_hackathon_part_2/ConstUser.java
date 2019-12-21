package com.example.coding_hackathon_part_2;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;


public class ConstUser {
    private DocumentReference userid;
    private String remarks;
    private Timestamp time;
    private String media_path;
    private Integer user_status;

    public ConstUser(Object userid, Object remarks, Object time, Object media_path, Object user_status) {

        this.userid = (DocumentReference) userid;
        this.remarks = remarks.toString();
        this.media_path = media_path.toString();
        this.user_status = Integer.valueOf(user_status.toString());
        this.time = (Timestamp) time;
    }


    public DocumentReference getUserid()
    {
        return userid;
    }

    public void setUserid(DocumentReference userid)
    {
        this.userid = userid;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remaeks) {
        this.remarks = remaeks;
    }

    public Timestamp getTime()
    {
        return time;
    }

    public void setTime(Timestamp time)
    {
        this.time = time;
    }

    public String getMedia_path() {
        return media_path;
    }

    public void setMedia_path(String media_path) {
        this.media_path = media_path;
    }

    public Integer getUser_status() {
        return user_status;
    }

    public void setUser_status(Integer user_status) {
        this.user_status = user_status;
    }
}
