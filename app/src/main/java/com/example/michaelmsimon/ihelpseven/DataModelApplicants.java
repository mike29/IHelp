package com.example.michaelmsimon.ihelpseven;

import android.widget.Button;
import android.widget.ImageButton;

/**
 * Created by Michael M. Simon on 29.11.2017.
 */

public class DataModelApplicants {

    private String FName;
    private String LName;
    private ImageButton accept;
    private String userID;


    public DataModelApplicants(String FName, String LName, String userID, ImageButton accept) {
        this.FName = FName;
        this.LName = LName;
        this.accept = accept;
        this.userID = userID;

    }

    public String getFName() {
        return FName;
    }

    public String getLName() {
        return LName;
    }

    public ImageButton getAccept() {
        return accept;
    }

    public String getUserID() {
        return userID;
    }

}
