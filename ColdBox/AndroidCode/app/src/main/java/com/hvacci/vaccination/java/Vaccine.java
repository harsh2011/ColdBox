package com.hvacci.vaccination.java;

/**
 * Created by Harsh on 18-02-2017.
 */

public class Vaccine {
    public Vaccine(int id,String date, String vname, String vdesc,int price) {
        this.id = id;
        this.date = date;
        this.vname = vname;
        this.vdesc = vdesc;
        this.price =  price;
    }

    public String getdate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    int id;
    String date;
    String vname;
    String vdesc;
    int price;

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getVName() {
        return vname;
    }

    public void setVName(String vname) {
        this.vname = vname;
    }

    public String getVDesc() {
        return vdesc;
    }

    public void setVDesc(String vdesc) {
        this.vdesc = vdesc;
    }

}
