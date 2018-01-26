package com.example.michaelmsimon.ihelpseven;

/**
 * Created by Michael M. Simon on 14.10.2017.
 */

public class TaskBlogModelInt {
    private String task_id, task_title,task_category,task_description,task_posted_date,city,country,task_lat,task_lng,task_taken,task_posted_by_fName,user_id;
    String singleMessge;
    public TaskBlogModelInt(String task_id, String task_Title, String task_Category, String task_Description, String posted_Date,String city,String country,String task_lat,String task_lng,String task_taken, String task_posted_by_fName,String user_id) {
        this.task_id=task_id;
        this.task_title = task_Title;
        this.task_category = task_Category;
        this.task_description = task_Description;
        this.task_posted_date = posted_Date;
        this.city= city;
        this.country= country;
        this.task_lat= task_lat;
        this.task_lng= task_lng;
        this.task_taken = task_taken;
        this.task_posted_by_fName=task_posted_by_fName;
        this.user_id=user_id;



    }


    public TaskBlogModelInt() {
    }

    public String getTask_id(String tid) {
        task_id = tid;
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    public String getTask_title() {
        return task_title;
    }

    public void setTask_title(String task_title) {
        this.task_title = task_title;
    }

    public String getTask_category() {
        return task_category;
    }

    public void setTask_category(String task_category) {
        this.task_category = task_category;
    }

    public String getTask_description() {
        return task_description;
    }

    public void setTask_description(String task_description) {
        this.task_description = task_description;
    }

    public String getTask_posted_date() {
        return task_posted_date;
    }

    public void setTask_posted_date(String task_posted_date) {
        this.task_posted_date = task_posted_date;
    }

    public String getCity() {
        return city;
    }

    public String getUserID() {
        return user_id;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTask_lat() {
        return task_lat;
    }

    public void setTask_lat(String task_lat) {
        this.task_lat = task_lat;
    }

    public String getTask_lng() {
        return task_lng;
    }

    public void setTask_lng(String task_lng) {
        this.task_lng = task_lng;
    }

    public String getTask_taken() {
        return task_taken;
    }

    public void setTask_posted_by_fName(String task_posted_by_fName) {
        this.task_posted_by_fName = "Posted By: " + task_posted_by_fName;
    }

    public String getTask_posted_by_fName() {
        return task_posted_by_fName;
    }

}
