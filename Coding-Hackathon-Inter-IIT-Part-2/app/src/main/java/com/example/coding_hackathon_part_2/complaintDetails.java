package com.example.coding_hackathon_part_2;

import com.google.firebase.firestore.DocumentReference;

public class complaintDetails
{
    private String remarks;
    private DocumentReference userid;

    public complaintDetails(Object userid, Object remarks)
    {
        this.remarks = remarks.toString();
        this.userid = (DocumentReference) userid;

    }

    public String getRemarks()
    {
        return remarks;
    }

    public void setRemarks(String remarks)
    {
        this.remarks = remarks;
    }

    public DocumentReference getUserid()
    {
        return userid;
    }

    public void setUserid(DocumentReference userid)
    {
        this.userid = userid;
    }

}