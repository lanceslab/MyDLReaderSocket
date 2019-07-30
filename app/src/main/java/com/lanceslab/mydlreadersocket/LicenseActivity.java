//package com.lanceslab.mydlreadersocket;
package com.lanceslab.mydlreadersocket;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.CommonStatusCodes;
//import com.google.android.gms.samples.vision.barcodereader.BarcodeCaptureActivity;
//import com.google.android.gms.samples.vision.barcodereader.R;
import com.google.android.gms.vision.barcode.Barcode;

import java.util.Calendar;

/**
 * Created by LanceTaylor on 4/3/2017.
 */

public class LicenseActivity extends Activity implements View.OnClickListener {





    // use a compound button so either checkbox or switch widgets work.
    private CompoundButton autoFocus;
    private CompoundButton useFlash;
    private TextView statusMessage;
    private TextView barcodeValue;
    //private TextView lanceAdd;
    DriversLicense capturedDL;
    MediaPlayer mPlayer;
    private static final int RC_BARCODE_CAPTURE = 9001;
    private static final String TAG = "License Scanner";

    String lastName = null, firstName = null, midName = null, birthDate, licNumber,
            addStreet, addCity, addState, addZip, gender, licIssDate, licExpDate, licCounty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.license_reader);

        capturedDL = new DriversLicense();
        statusMessage = (TextView)findViewById(R.id.status_message);
        barcodeValue = (TextView)findViewById(R.id.barcode_value);
        //lanceAdd = (TextView)findViewById(R.id.lanceadd);
        autoFocus = (CompoundButton) findViewById(R.id.auto_focus);
        useFlash = (CompoundButton) findViewById(R.id.use_flash);

        findViewById(R.id.read_barcode).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.read_barcode) {
            // WORKING
//            mPlayer = MediaPlayer.create(LicenseActivity.this, R.raw.jarvisihavebeenuploaddedandready);
//            mPlayer.start();
            // launch barcode activity.
            Intent intent = new Intent(LicenseActivity.this, BarcodeCaptureActivity.class);
            intent.putExtra(BarcodeCaptureActivity.AutoFocus, autoFocus.isChecked());
            intent.putExtra(BarcodeCaptureActivity.UseFlash, useFlash.isChecked());

            startActivityForResult(intent, RC_BARCODE_CAPTURE);
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
        if (requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //statusMessage.setText(R.string.barcode_success);
                    if(barcode.displayValue.toString().trim() == "DL"){
                        statusMessage.setText(R.string.barcode_success);
                    }
//                    mPlayer = MediaPlayer.create(LicenseActivity.this, R.raw.jarvistherenderiscomplete2);
//                    mPlayer.start();
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
                                            capturedDL.setLName(lastName);
                                        }
                                    }
                                }
                            }
                        }
                    }
                    firstName = items[1].trim();
                    capturedDL.setFName(firstName);
                    String[] midNameItems = items[2].split("\n");
                    midName = midNameItems[0].trim();
                    capturedDL.setMName(midName);

                    String bDate = barcode.driverLicense.birthDate;
                    //birthDate = barcode.driverLicense.birthDate;
                    String[] bDateParts = bDate.split("");
                    String dayP, monthP, yearP, ageP;
                    monthP = bDateParts[5] + bDateParts[6];
                    dayP = bDateParts[7] + bDateParts[8];
                    yearP = bDateParts[1] + bDateParts[2] + bDateParts[3] + bDateParts[4];
                    birthDate = monthP + "-" + dayP + "-" + yearP;
                    capturedDL.setDOB(birthDate);
                    // Calculate age !!!!!!!!!!!!!!!
                    Integer yearInt, monthInt, dayInt;
                    yearInt = Integer.parseInt(yearP);
                    monthInt = Integer.parseInt(monthP);
                    dayInt = Integer.parseInt(dayP);
                    ageP = getAgeString(yearInt, monthInt, dayInt);
                    capturedDL.setAge(getAgeInt(yearInt, monthInt, dayInt));

                    licNumber = barcode.driverLicense.licenseNumber;
                    capturedDL.setLicNum(licNumber);
                    addStreet = barcode.driverLicense.addressStreet;
                    capturedDL.setStreet(addStreet);
                    addCity = barcode.driverLicense.addressCity;
                    capturedDL.setCity(addCity);
                    addState = barcode.driverLicense.addressState;
                    capturedDL.setState(addState);
                    addZip = barcode.driverLicense.addressZip;//== "1"
                    capturedDL.setZip(addZip);
                    if(barcode.driverLicense.gender.contains("1")) {
                        gender = "Male";
                    }else {
                        gender = "Female";
                    }
                    capturedDL.setGender(gender);

                    //licIssDate, licExpDate, licCounty;
                    licIssDate = barcode.driverLicense.issueDate;
                    capturedDL.setlicIssDate(licIssDate);
                    licExpDate = barcode.driverLicense.expiryDate;
                    capturedDL.setlicExpDate(licExpDate);
                    licCounty = barcode.driverLicense.issuingCountry;
                    capturedDL.setlicCounty(barcode.driverLicense.issuingCountry);

                    barcodeValue.setText("First name= " + firstName + "\n" + "Middle name= " + midName + "\n" +
                            "Last name= " + lastName + "\n" + "DOB= " + birthDate + "   Age: " + ageP + "\n" +
                            "License#=" + licNumber + "   Gender: " + gender + "\n" + "Address= " +
                            addStreet + " " + addCity + " " + addState + " " + addZip);
// INTENT
                    try{
                        Intent intent = new Intent(LicenseActivity.this, LicenseResultsActivity.class);
                        //Intent intent = new Intent(LicenseActivity.this, ClientSocketAsyncYask.class);
                        //Intent intent = new Intent(LicenseActivity.this, SendfileActivity.class);
                        //  put Captured Drivers License in intent data
//                        mPlayer = MediaPlayer.create(null, R.raw.jarvistherenderiscomplete2);
//                        mPlayer.start();
                        intent.putExtra("license", capturedDL);
                        startActivity(intent);
                    } catch (Exception ex){
                        Toast radioToast = Toast.makeText(LicenseActivity.this,
                                "Error starting LicenseResultsActivity", Toast.LENGTH_LONG);
                        radioToast.show();
                        barcodeValue.setText(ex.getMessage());
                    }


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
        }
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
