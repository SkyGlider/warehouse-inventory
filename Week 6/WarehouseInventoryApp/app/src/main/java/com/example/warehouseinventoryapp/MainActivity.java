package com.example.warehouseinventoryapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.ToggleButton;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class MainActivity extends AppCompatActivity {

    //ArrayList for item objects
    private ArrayList<Item> itemList = new ArrayList<Item>(100);

    //vars for elements
    private EditText nameIn, qtyIn, costIn, descIn;
    private ToggleButton frozenIn;
    private Button addItemBtn,clearItemBtn;


    //layouts
    private DrawerLayout drawerlayout;
    private NavigationView navigationView;
    Toolbar toolbar;

    private SharedPreferences appPreferences;
    private SharedPreferences.Editor appEditor;

    private Gson gson = new Gson();

    public static boolean isConfirmed;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);

        //drawer layouts and toolbar
        drawerlayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerlayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerlayout.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(new MyNavigationListener());

        //assign respective editTexts to their vars
        nameIn = (EditText) findViewById(R.id.editText);
        qtyIn = (EditText) findViewById(R.id.editText5);
        costIn = (EditText) findViewById(R.id.editText6);
        descIn = (EditText) findViewById(R.id.editText4);
        frozenIn = (ToggleButton) findViewById(R.id.toggleButton);
        addItemBtn = findViewById(R.id.add_item_btn);
        clearItemBtn = findViewById(R.id.clear_item_btn);

        //retrieve previous itemList
        appPreferences = getSharedPreferences("lastItemList",0);
        String json = appPreferences.getString("itemList","");
        if (!json.equals("")) {
            Type type = new TypeToken<ArrayList<Item>>() {}.getType();
            itemList = gson.fromJson(json, type);
        }

        //set up shared pref variables
        appPreferences = getSharedPreferences("lastEnteredItem",0);
        appEditor = appPreferences.edit();
        //retrieve shared pref and set textboxes
        restorePreferences();

        //CODES FOR SMS RECEIVER -WEEK 4 ONLY
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.SEND_SMS, Manifest.permission.RECEIVE_SMS, Manifest.permission.READ_SMS}, 0);
        MyBroadcastReceiver myBroadcastReceiver =  new MyBroadcastReceiver();
        registerReceiver(myBroadcastReceiver, new IntentFilter(SMSReceiver.SMS_FILTER));

        //Floating Action Button
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                addNewItem(view);
                isConfirmed = true;

            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();  // Always call the superclass method first
        appPreferences = getSharedPreferences("lastItemList",0);
        appEditor = appPreferences.edit();
        appEditor.putString("itemList",gson.toJson(itemList));
        appEditor.commit();
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.nav_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id == R.id.add_item_menu) {
            addItemBtn.performClick();
        } else if (id == R.id.clear_item_menu) {
            clearItemBtn.performClick();
        } else if (id == R.id.view_items_menu){
            Intent intent = new Intent(getBaseContext(), InventoryList.class);
            intent.putExtra("itemList",gson.toJson(itemList));
            startActivity(intent);
        }
        return true;
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

    class MyNavigationListener implements NavigationView.OnNavigationItemSelectedListener {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            // get the id of the selected item
            onOptionsItemSelected(item);
            // close the drawer
            drawerlayout.closeDrawers();
            // tell the OS
            return true;
        }
    }


}
