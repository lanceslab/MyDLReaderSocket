/*
 * Copyright (C) The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.lanceslab.mydlreadersocket;
//import com.google.android.gms.samples.vision.barcodereader.
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import java.util.Calendar;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;


/**
 * Main activity demonstrating how to pass extra parameters to an activity that reads bar codes.
 */
public class MainActivityFirst extends Activity implements View.OnClickListener {

    // use a compound button so either checkbox or switch widgets work.

    private RadioButton radLicense, radVin;
    private TextView statusMessage;
    private TextView radioValue;



    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "LancesLab Main";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_first);

        statusMessage = (TextView)findViewById(R.id.status_message);
        radioValue = (TextView)findViewById(R.id.radio_value);

        findViewById(R.id.read_barcode).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        if(v.getId() == R.id.btnScan){

            if(radLicense.isChecked() && radVin.isChecked()){
                Toast radioToast = Toast.makeText(MainActivityFirst.this, "You cant have both Radios CHECKED", Toast.LENGTH_LONG);
                radioToast.show();
                return;
            } else if(radLicense.isChecked()){
                // Start Activity for Drivers License
                Intent intent = new Intent(this, LicenseActivity.class);
                startActivity(intent);
            } else if(radVin.isChecked()){
                // Start Activity for VIN Numbers
            }

        }


    }

    /**
     * Called when an activity you launched exits, giving you the requestCode it started with,
     * the resultCode it returned, and any additional data from it.
     * Called immediately before onResume() when the activity is re-starting.
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {




    }



    // Calculate Age
    private String getAge(int year, int month, int day){
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






}




/*
        if (requestCode == RC_BARCODE_CAPTURE) {
                if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                //statusMessage.setText(R.string.barcode_success);

                // Get the Name Info
                String temBCdata = "";
                temBCdata += barcode.rawValue;
                String[] items = temBCdata.split(",");
                char[] lNameSplit = items[0].toCharArray();
                for (int i = 0; i < lNameSplit.length - 1; i++){

        if(lNameSplit[i] == 'D'){
        if(lNameSplit[i+1] == 'L'){
        if(lNameSplit[i+2] == 'D'){
        if(lNameSplit[i+3] == 'A'){
        if(lNameSplit[i+4] == 'A'){
        lastName = items[0].substring(i+5).trim();
        }
        }
        }
        }
        }
        }
        firstName = items[1].trim();
        String[] midNameItems = items[2].split("\n");
        midName = midNameItems[0].trim();

        Boolean allPass = true;
        if(firstName == null){
        allPass = false;
        firstName = "Missing";
        }else if(lastName == null){
        allPass = false;
        lastName = "Missing";
        }else if(midName == null){
        allPass = false;
        midName = "Missing";
        }else if(barcode.driverLicense.birthDate == null){
        allPass = false;
        birthDate = "Missing";
        }else if(barcode.driverLicense.licenseNumber == null){
        allPass = false;
        licNumber = "Missing";
        }else if(barcode.driverLicense.addressStreet == null){
        allPass = false;
        addStreet = "Missing";
        }else if(barcode.driverLicense.addressCity == null){
        allPass = false;
        addCity = "Missing";
        }else if(barcode.driverLicense.addressState == null){
        allPass = false;
        addState = "Missing";
        }else if(barcode.driverLicense.addressZip == null){
        allPass = false;
        addZip = "Missing";
        }else if(barcode.driverLicense.gender == null){
        allPass = false;
        gender = "Missing";
        }else if(barcode.driverLicense.issueDate == null){
        //allPass = false;
        licIssDate = "Missing";
        }else if(barcode.driverLicense.expiryDate == null){
        //allPass = false;
        licExpDate = "Missing";
        }else if(barcode.driverLicense.issuingCountry == null){
        //allPass = false;
        licCounty = "Missing";
        }else {
        //allPass = true;
        }


        if(allPass == true){
        statusMessage.setText(R.string.barcode_success);
        }else {
        statusMessage.setText(R.string.barcode_missing_data);
        }


        String bDate = barcode.driverLicense.birthDate;
        //birthDate = barcode.driverLicense.birthDate;
        String[] bDateParts = bDate.split("");
        String dayP, monthP, yearP, ageP;
        monthP = bDateParts[5] + bDateParts[6];
        dayP = bDateParts[7] + bDateParts[8];
        yearP = bDateParts[1] + bDateParts[2] + bDateParts[3] + bDateParts[4];
        birthDate = monthP + "-" + dayP + "-" + yearP;
        // Calculate age !!!!!!!!!!!!!!!
        Integer yearInt, monthInt, dayInt;
        yearInt = Integer.parseInt(yearP);
        monthInt = Integer.parseInt(monthP);
        dayInt = Integer.parseInt(dayP);
        ageP = getAge(yearInt, monthInt, dayInt);


        licNumber = barcode.driverLicense.licenseNumber;
        addStreet = barcode.driverLicense.addressStreet;
        addCity = barcode.driverLicense.addressCity;
        addState = barcode.driverLicense.addressState;
        addZip = barcode.driverLicense.addressZip;//== "1"
        if(barcode.driverLicense.gender.contains("1")) {
        gender = "Male";
        }else {
        gender = "Female";
        }

        barcodeValue.setText("First name= " + firstName + "\n" + "Middle name= " + midName + "\n" +
        "Last name= " + lastName + "\n" + "DOB= " + birthDate + "   Age: " + ageP + "\n" +
        "License#=" + licNumber + "   Gender: " + gender + "\n" + "Address= " +
        addStreet + " " + addCity + " " + addState + " " + addZip);

        Log.d(TAG, "License read: " + barcode.displayValue);
        } else {
        statusMessage.setText(R.string.barcode_failure);
        Log.d(TAG, "No License captured, intent data is null");
        }
        } else {
        statusMessage.setText(String.format(getString(R.string.barcode_error),
        CommonStatusCodes.getStatusCodeString(resultCode)));
        }
        }
        else {
        super.onActivityResult(requestCode, resultCode, data);
        }*/
