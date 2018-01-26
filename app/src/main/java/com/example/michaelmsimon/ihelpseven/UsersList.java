package com.example.michaelmsimon.ihelpseven;

import android.util.Log;

import com.example.michaelmsimon.ihelpseven.user.SettingActivity;

import java.util.ArrayList;

/**
 * Created by Michael M. Simon on 29.11.2017.
 */

public class UsersList {
    private String uid;
    private String uName;
    private String uidApplicants;

    ArrayList<String> usersList;


    public UsersList(String uid, String uidApplicants) {
        this.uid = uid;
        this.uidApplicants = uidApplicants;

    }

    public UsersList(ArrayList<String> usersList) {
        this.usersList = usersList;

    }

    public String getUidApplicants() {
        return uidApplicants;
    }

    public UsersList(String uidApplicants) {
        this.uidApplicants = uidApplicants;

    }

    public UsersList() {


    }

    public ArrayList<String> getUsersList() {
        return usersList;
    }

    public void setUsersList(ArrayList<String> usersList) {
        this.usersList = usersList;
    }


    public String getUid() {
        return uid;
    }

    public void setUid(String s) {
        this.uid = uid;
    }

    public String getuName() {
        return uName;
    }

    public String getUidApplicants(String s) {
        return uidApplicants;
    }

    public void setuName(String uName) {
        this.uName = uName;
    }

    public void setUidApplicants(String uidApplicants) {
        this.uidApplicants = uidApplicants;
    }

    public ArrayList<String> getValueHolder() {
        return valueHolder;
    }

    private ArrayList<String> valueHolder;

    public String compareApplicants(ArrayList<String> usersListApplicant, ArrayList<String> usersListFromDB) {

        valueHolder = new ArrayList<String>();
        for (int i = 0; i < usersListApplicant.size(); i++) {
            if (usersListFromDB.contains(usersListApplicant.get(i))) {
                valueHolder.add(String.valueOf(usersListApplicant.get(i)));
                Log.d("COMPARE METHODS ", String.valueOf(usersListApplicant.get(i)));

            }


        }
        return String.valueOf(valueHolder);

    }

}
