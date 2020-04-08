package edu.monash.fit2081.calculatorapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;


public class MainActivity extends AppCompatActivity {
    private double valueOne = Double.NaN;
    private double valueTwo;
    private static final char ADDITION = '+';
    private static final char SUBTRACTION = '-';
    private static final char MULTIPLICATION = '*';
    private static final char DIVISION = '/';
    private static final char NO_OPERATION = '?';

    private char CURRENT_ACTION;
    private DecimalFormat decimalFormat;
    public TextView interScreen;  // Intermediate result Screen
    private TextView resultScreen; // Result Screen


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Reference both TextViews
        interScreen =  findViewById(R.id.InterScreen);
        resultScreen =  findViewById(R.id.resultScreen);
        decimalFormat = new DecimalFormat("#.##########");
    }

    public void numpadClick(View v) {
        interScreen.setText(interScreen.getText() + ((Button) v).getText().toString() );
    }

    public void operatorClick(View v){
        computeCalculation();
        char operatingChar = ((Button) v).getText().toString().charAt(0);
        if (Double.isNaN(valueOne)) {
            showToast("Invalid Key");
        }else if(operatingChar == '=') {
            resultScreen.setText("="+ valueOne);
            valueOne = Double.NaN;
            CURRENT_ACTION = NO_OPERATION;
        }else{
            CURRENT_ACTION = operatingChar;
            resultScreen.setText(decimalFormat.format(valueOne) + CURRENT_ACTION);
            interScreen.setText("");
        }
    }

    public void buttonClearClick(View v) {
        /*
        * if the intermediate TextView has text then
        *       delete the last character
        * else
              * reset valueOne, valueTwo, the content of result TextView,
              * and the content of intermediate TextView
        * */
        String intermediate = (String) interScreen.getText();

        if(intermediate.length() != 0){
            intermediate = intermediate.substring(0,intermediate.length()-1);
            interScreen.setText(intermediate);
        }else{
            valueOne = Double.NaN;
            valueTwo = 0;
            resultScreen.setText("");
            interScreen.setText("");


        }
    }


    private void computeCalculation() {

        if (!Double.isNaN(valueOne)) {
            String valueTwoString = interScreen.getText().toString();
            if (!valueTwoString.equals("")) {
                valueTwo = Double.parseDouble(valueTwoString);
                interScreen.setText(null);
                if (CURRENT_ACTION == ADDITION)
                    valueOne = this.valueOne + valueTwo;
                else if (CURRENT_ACTION == SUBTRACTION)
                    valueOne = this.valueOne - valueTwo;
                else if (CURRENT_ACTION == MULTIPLICATION)
                    valueOne = this.valueOne * valueTwo;
                else if (CURRENT_ACTION == DIVISION)
                    valueOne = this.valueOne / valueTwo;
            }
        } else {
            try {
                valueOne = Double.parseDouble(interScreen.getText().toString());
            } catch (Exception e) {
            }

        }
    }

    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_LONG).show();
    }
}
