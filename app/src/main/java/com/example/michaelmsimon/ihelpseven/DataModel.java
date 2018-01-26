package com.example.michaelmsimon.ihelpseven;

import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

/**
 * Created by Michael M. Simon on 28.11.2017.
 */

public class DataModel {


    private String aName;
    private String date;
    private String distance;
    private ImageButton accepts;
    private ImageButton accept;
    private String taskID;
    private String userID;
    private String applicantName;

    public DataModel(String aName, String date, String distance,String taskID, ImageButton accept ) {
        this.aName=aName;
        this.date=date;
        this.distance = distance;
        this.taskID = taskID;
        this.accept=accept;

    }
    public DataModel(String applicantName, String distance,String userID, ImageButton accepts ) {
        this.applicantName=applicantName;
        this.distance = distance;
        this.userID = userID;
        this.accepts=accepts;

    }

    public DataModel(String userID){
        this.userID = userID;

    }



    public String getAName() {
        return aName;
    }

    public String getDate() {
        return date;
    }

    public String getDistance() {
        return distance;
    }
    public ImageButton getAccepts(){
        return accepts;
    }

    public ImageButton getAccept() {
        return accept;
    }
    public String getTaskID() {
        return taskID;
    }
    public String getUserID() {
        return userID;
    }

    public String getApplicantName() {
        return applicantName;
    }
}
