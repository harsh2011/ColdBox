package com.hvacci.vaccination.java;

import com.android.volley.toolbox.StringRequest;

/**
 * Created by Harsh on 18-02-2017.
 */

public class BookVaccine {
    String vaccine_id;
    String vaccine_name;
    String due_date;
    String price;
    String username;
    String paramedics_id;
    String address;
    String status;


    public BookVaccine(){

    }

    public String getStatus() {
        return status;
    }

    public void setVaccine_id(String vaccine_id) {
        this.vaccine_id = vaccine_id;
    }

    public void setVaccine_name(String vaccine_name) {
        this.vaccine_name = vaccine_name;
    }

    public void setDue_date(String due_date) {
        this.due_date = due_date;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setParamedics_id(String paramedics_id) {
        this.paramedics_id = paramedics_id;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParamedics_id() {
        return paramedics_id;
    }

    public String getAddress() {
        return address;
    }

    public BookVaccine(String vaccine_id, String vaccine_name, String due_date, String price, String username, String status, String paramedics_id, String address) {
        this.vaccine_id = vaccine_id;
        this.vaccine_name = vaccine_name;
        this.due_date = due_date;
        this.price = price;
        this.username = username;
        this.status = status;
        this.paramedics_id = paramedics_id;
        this.address = address;
    }

    public BookVaccine(String vaccine_id, String vaccine_name, String due_date, String price, String username, String status) {
        this.vaccine_id = vaccine_id;
        this.vaccine_name = vaccine_name;
        this.due_date = due_date;
        this.price = price;
        this.username = username;
        this.status = status;
    }

    public String getVaccine_id() {
        return vaccine_id;
    }

    public String getVaccine_name() {
        return vaccine_name;
    }

    public String getDue_date() {
        return due_date;
    }

    public String getPrice() {
        return price;
    }

    public String getUsername() {
        return username;
    }

}
