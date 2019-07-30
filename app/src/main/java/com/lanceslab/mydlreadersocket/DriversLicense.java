package com.lanceslab.mydlreadersocket;//package com.google.android.gms.samples.vision.barcodereader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by LanceTaylor on 4/3/2017.
 */

public class DriversLicense implements Serializable {

    private static final long serialVersionUID = 1L;

    //private String name;
    private String lastName = null, firstName = null, midName = null, birthDate, licNumber,
            addStreet, addCity, addState, addZip, gender, licIssDate = "none",
            licExpDate = "none", licCounty = "none";

    private int age = 100;


    // Default Constructor
    public DriversLicense(){

    }

    public DriversLicense(String _lastName,String  _firstName,String  _midName,String  _birthDate,
                          String _licNumber, String _addStreet, String _addCity, String _addState,
                          String  _addZip, String  _gender, String _licIssDate, String _licExpDate,
                          String _licCounty){
        lastName = _lastName; firstName = _firstName; midName = _midName; birthDate = _birthDate;
        licNumber = _licNumber; addStreet = _addStreet; addCity = _addCity; addState = _addState;
        addZip = _addZip; gender = _gender; licIssDate = _licIssDate; licExpDate = _licExpDate;
                licCounty = _licCounty;
    }

    public DriversLicense(String _lastName,String  _firstName,String  _midName,String  _birthDate,String _licNumber,
           String _addStreet, String _addCity, String _addState,String  _addZip, String  _gender){
        lastName = _lastName; firstName = _firstName; midName = _midName; birthDate = _birthDate;
        licNumber = _licNumber; addStreet = _addStreet; addCity = _addCity; addState = _addState;
                addZip = _addZip; gender = _gender;
    }

    public String getFName() {
        return firstName;
    }

    public void setFName(String fName) {
        this.firstName = fName;
    }

    public String getLName() {
        return lastName;
    }

    public void setLName(String lName) {
        this.lastName = lName;
    }

    public String getMName() {
        return midName;
    }

    public void setMName(String mName) {
        this.midName = mName;
    }

    public String getDOB() {
        return birthDate;
    }

    public void setDOB(String dob) {
        this.birthDate = dob;
    }

    public int getAge() {
        if (age == 100){
            if(birthDate != null){
                // Calculate age !!!!!!!!!!!!!!!
                //birthDate = monthP + "-" + dayP + "-" + yearP;
                String[] bDateParts = birthDate.split("-");
                String dayP, monthP, yearP, ageString;
                int ageInt;
                monthP = bDateParts[0] + bDateParts[1];
                dayP = bDateParts[2] + bDateParts[3];
                yearP = bDateParts[4] + bDateParts[5] + bDateParts[6] + bDateParts[7];

                Integer yearInt, monthInt, dayInt;
                yearInt = Integer.parseInt(yearP);
                monthInt = Integer.parseInt(monthP);
                dayInt = Integer.parseInt(dayP);
                //ageString = getAgeString(yearInt, monthInt, dayInt);
                ageInt = getAgeInt(yearInt,monthInt, dayInt);
                return ageInt;
            }else{
                return age;
            }
        }else {
            return age;
        }
    }

    public void setAge(int a) {
        this.age = a;
    }

    public String getLicNum() {
        return licNumber;
    }

    public void setLicNum(String licNum) {
        this.licNumber = licNum;
    }

    public String getStreet() {
        return addStreet;
    }

    public void setStreet(String street) {
        this.addStreet = street;
    }

    public String getCity() {
        return addCity;
    }

    public void setCity(String city) {
        this.addCity = city;
    }

    public String getState() {
        return addState;
    }

    public void setState(String state) {
        this.addState = state;
    }

    public String getZip() {
        return addZip;
    }

    public void setZip(String zip) {
        this.addZip = zip;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gen) {
        this.gender = gen;
    }

    public String getlicIssDate() {
        return licIssDate;
    }

    public void setlicIssDate(String issDate) {
        this.licIssDate = issDate;
    }

    public String getlicExpDate() {
        return licExpDate;
    }

    public void setlicExpDate(String expDate) {
        this.licExpDate = expDate;
    }

    public String getlicCounty() {
        return licCounty;
    }

    public void setlicCounty(String issCounty) {
        this.licCounty = issCounty;
    }

    @Override
    public String toString() {
//        return "com.google.android.gms.samples.vision.barcodereader.DriversLicense [First Name=" + firstName + ", Last Name=" + lastName +
//                ", Middle Name=" + midName + ", DOB" + birthDate + ", Age=" + age +
//                ", License #=" + licNumber + ", Street=" + addStreet + ", City" + addCity +
//                ", State" + addState + ", Zip" + addZip + ", Gender=" + gender + ", Issue Date=" +
//                licIssDate + ", Exp Date=" + licExpDate + ", Lic County=" + licCounty + "]";
        return "DriversLicense [First Name= " +
                firstName + ", Last Name= " + lastName + ", Middle Name= " + midName + ", DOB= " +
                 birthDate + ", License #= " + licNumber + ", Street= " + addStreet + ", City= " + addCity +
                ", State= " + addState + ", Zip= " + addZip + ", Gender= " + gender + ", Issue Date= " +
                licIssDate + ", Exp Date= " + licExpDate + ", Lic County =" + licCounty + "]";


        //return "com.google.android.gms.samples.vision.barcodereader.DriversLicense [First Name=" + firstName + ", age=" + age + "]";
    }
/*    @Override
    public String toString() {
        return new GsonBuilder().create().toJson(this, Producto.class);
    }*/
    public String toJSON(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("First Name", firstName);
            jsonObject.put("Middle Name", midName);
            jsonObject.put("Last Name", lastName);
            jsonObject.put("DOB", birthDate);
            jsonObject.put("Gender", gender);
            jsonObject.put("License #", licNumber);
            jsonObject.put("Street", addStreet);
            jsonObject.put("City", addCity);
            jsonObject.put("State", addState);
            jsonObject.put("Zip", addZip);
            jsonObject.put("Exp Date", licExpDate);
            jsonObject.put("Iss Date", licIssDate);
            jsonObject.put("County", licCounty);

            return jsonObject.toString();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return "";
        }

    }

    public JSONObject toJSONObject(){

        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("First Name", firstName);
            jsonObject.put("Middle Name", midName);
            jsonObject.put("Last Name", lastName);
            jsonObject.put("DOB", birthDate);
            jsonObject.put("Gender", gender);
            jsonObject.put("License #", licNumber);
            jsonObject.put("Street", addStreet);
            jsonObject.put("City", addCity);
            jsonObject.put("State", addState);
            jsonObject.put("Zip", addZip);
            jsonObject.put("Exp Date", licExpDate);
            jsonObject.put("Iss Date", licIssDate);
            jsonObject.put("County", licCounty);

            return jsonObject;
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            return  null;
        }
        //return jsonObject;
    }



    // Calculate Age
    private String getAgeString(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        Integer ageInt = new Integer(age);
        String ageS = ageInt.toString();
        return ageS;
    }
    // Calculate Age
    private int getAgeInt(int year, int month, int day){
        Calendar dob = Calendar.getInstance();
        Calendar today = Calendar.getInstance();
        dob.set(year, month, day);
        int age = today.get(Calendar.YEAR) - dob.get(Calendar.YEAR);
        if (today.get(Calendar.DAY_OF_YEAR) < dob.get(Calendar.DAY_OF_YEAR)){
            age--;
        }
        Integer ageInt = new Integer(age);
        return ageInt;
    }



}
