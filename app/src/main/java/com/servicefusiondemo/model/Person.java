package com.servicefusiondemo.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Avdhesh AKhani on 8/16/2016.
 */
public class Person {


    private String fName;
    private String lName;
    private String dob;
    private String zip;


    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }



    public Person() {
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("fName", fName);
        result.put("lName", lName);
        result.put("dob", dob);
        result.put("zip", zip);
        return result;
    }
}
