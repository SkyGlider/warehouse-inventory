package com.example.warehouseinventoryapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.ToggleButton;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    //ArrayList for item objects
    private ArrayList<Item> itemList = new ArrayList<Item>(100);

    //vars for elements
    private EditText nameIn, qtyIn, costIn, descIn;
    private ToggleButton frozenIn;

    private SharedPreferences appPreferences;
    private SharedPreferences.Editor appEditor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //assign respective editTexts to their vars
        nameIn = (EditText) findViewById(R.id.editText);
        qtyIn = (EditText) findViewById(R.id.editText5);
        costIn = (EditText) findViewById(R.id.editText6);
        descIn = (EditText) findViewById(R.id.editText4);
        frozenIn = (ToggleButton) findViewById(R.id.toggleButton);

        //set up shared pref variables
        appPreferences = getSharedPreferences("lastEnteredItem",0);
        appEditor = appPreferences.edit();
        //retrieve shared pref and set textboxes
        restorePreferences();

        //CODES FOR SMS RECEIVER -WEEK 4 ONLY
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        MyBroadcastReceiver myBroadcastReceiver =  new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));


    }

    public void addNewItem(View v){

        //check entry values of each element
        String itemName = nameIn.getText().toString().trim();
        String itemQuantity = qtyIn.getText().toString().trim();
        String itemCost = costIn.getText().toString().trim();
        String itemDesc = descIn.getText().toString().trim();
        boolean itemFrozen = frozenIn.isChecked();

        //string for toast
        String toastText;

        //check if field values empty or not
        //item description not compulsory
        if (itemName.length() != 0 && itemQuantity.length() != 0 && itemCost.length() != 0){

            //create new Item object and add to itemList and set toast text
            Item newItem = new Item(itemName,Integer.parseInt(itemQuantity),Double.parseDouble(itemCost),itemDesc,itemFrozen);
            itemList.add(newItem);

            //commit to save preferences
            appEditor.putString(getString(R.string.itemName),itemName);
            appEditor.putString(getString(R.string.itemQty),itemQuantity);
            appEditor.putString(getString(R.string.itemCost),itemCost);
            appEditor.putString(getString(R.string.itemDesc),itemDesc);
            appEditor.putBoolean(getString(R.string.itemFrozen),itemFrozen);
            appEditor.commit();

            toastText = "Item (" + itemName +") successfully added" ;

        }else{
            toastText = "Cannot add empty values";
        }

        //toast
        Toast.makeText(getApplicationContext(),toastText,Toast.LENGTH_SHORT).show();
    }

    public void clearAllItems(View v){

        //clear all field values, set toggle to off state
        appEditor.clear();
        appEditor.commit();
        restorePreferences();
        nameIn.requestFocus();

    }

    private void restorePreferences(){
        nameIn.setText(appPreferences.getString(getString(R.string.itemName),""));
        qtyIn.setText(appPreferences.getString(getString(R.string.itemQty),""));
        costIn.setText(appPreferences.getString(getString(R.string.itemCost),""));
        descIn.setText(appPreferences.getString(getString(R.string.itemDesc),""));
        if (appPreferences.getBoolean(getString(R.string.itemFrozen),false)) {
            frozenIn.setChecked(true);
        }else{
            frozenIn.setChecked(false);
        }
    }

    //restore view data when orientation changed
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    //only gets executed if inState != null so no need to check for null Bundle
    protected void onRestoreInstanceState(Bundle inState) {
        super.onRestoreInstanceState(inState);
    }

    //Broadcast receiver - WEEK 4 Feature
    class MyBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {

            //get string from intent
            String msg = intent.getStringExtra(SMSReceiver.SMS_MSG_KEY);
            //use string tokenizer to seperate the ';' symbols
            StringTokenizer sT = new StringTokenizer(msg,";");

            //set editText and check boxes to their respective string
            nameIn.setText(sT.nextToken());
            qtyIn.setText(sT.nextToken());
            costIn.setText(sT.nextToken());
            descIn.setText(sT.nextToken());
            if (sT.nextToken().toLowerCase().equals("true")) {
                frozenIn.setChecked(true);
            }else{
                frozenIn.setChecked(false);
            }

        }
    }


}
