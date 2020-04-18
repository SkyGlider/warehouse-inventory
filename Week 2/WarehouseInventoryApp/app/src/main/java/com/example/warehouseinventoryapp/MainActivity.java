package com.example.warehouseinventoryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //ArrayList for item objects
    private ArrayList<Item> itemList = new ArrayList<Item>(100);

    //vars for elements
    private EditText nameIn;
    private EditText qtyIn;
    private EditText costIn;
    private EditText descIn;
    private ToggleButton frozenIn;

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
            toastText = "Item (" + itemName +") successfully added" ;

        }else{
            toastText = "Cannot add empty values";
        }

        //toast
        Toast.makeText(getApplicationContext(),toastText,Toast.LENGTH_SHORT).show();
    }

    public void clearAllItems(View v){

        //clear all field values, set toggle to off state
        nameIn.setText("");
        qtyIn.setText("");
        costIn.setText("");
        descIn.setText("");
        nameIn.requestFocus();
        frozenIn.setChecked(false);

    }
}
