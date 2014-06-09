package com.codepath.example.tipcalculator;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TipCalculatorActivity extends Activity {

	// constants
	public static final double PERCENT = 0.01;
	public static final String NOT_A_NUMBER = "Is not a number!";
	
	// values used for tip calculation
	public static final double[] TIP_PRESET = { 10, 15, 20 };
	
	private Double tipPercentage = Double.NaN;
	private Double tipSplit = Double.NaN;
	
	// local view references
	EditText etAmount;
	EditText etAmountCustom;
	EditText etSplit;
	TextView tvTipAmount;
	TextView tvTipSplit;
	Button btnTip0;
	Button btnTip1;
	Button btnTip2;
	Button btnTipCustom;
	Button btnSplit;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator);
		
		etAmount = (EditText) findViewById(R.id.etAmount);
		etAmountCustom = (EditText) findViewById(R.id.etAmountCustom);
		etSplit = (EditText) findViewById(R.id.etSplit);
		tvTipAmount = (TextView) findViewById(R.id.tvTipAmount);
		tvTipSplit = (TextView) findViewById(R.id.tvTipSplit);
		btnTip0 = (Button) findViewById(R.id.btnTip0);
		btnTip1 = (Button) findViewById(R.id.btnTip1);
		btnTip2 = (Button) findViewById(R.id.btnTip2);
		btnTipCustom = (Button) findViewById(R.id.btnTipCustom);
		btnSplit = (Button) findViewById(R.id.btnSplit);
		
		// set the text for the 3 tip buttons
		btnTip0.setText(String.format("%d", (int)TIP_PRESET[0]) + "%" );
		btnTip1.setText(String.format("%d", (int)TIP_PRESET[1]) + "%" );
		btnTip2.setText(String.format("%d", (int)TIP_PRESET[2]) + "%" );
		
		// respond immediately after user types
		// OPTIONAL
		etAmount.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				calculateTip();
			}
		});
		
		
		etAmountCustom.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});

	}
	
	/**
	 * Method to handle the clicks of all the buttons
	 * 
	 * @param v The Button triggering this method
	 */
	public void onClick( View v ) {
		String tipStr;
		String splitStr;
		boolean usingPreSet = true;
		boolean usingSplit = false;
		
		switch( v.getId() ) {
		case R.id.btnTip0:
			tipPercentage = TIP_PRESET[0];
			break;
		case R.id.btnTip1:
			tipPercentage = TIP_PRESET[1];
			break;
		case R.id.btnTip2:
			tipPercentage = TIP_PRESET[2];
			break;
		case R.id.btnTipCustom:
			// OPTIONAL
			// custom tip
			usingPreSet = false;
			tipStr = etAmountCustom.getText().toString();
			if ( tipStr == null || tipStr.isEmpty() ) {
				tipStr = "0";
			}
			tipPercentage = isNumeric(tipStr);
			break;
		default:
			break;
		}
		
		if ( usingPreSet ) {
			tipStr = tipPercentage.toString();
			etAmountCustom.setText(tipStr);
		}
		
		calculateTip();
	}

	/**
	 * Method to calculate split
	 * 
	 * @param v View that triggered the split
	 */
	public void onSplit( View v ) {
		calculateSplit();
	}

	/**
	 * Retrieve the value from the edit text.
	 * Perform any necessary checks.
	 * Multiply the tip values to get tip value
	 * 
	 */
	protected void calculateTip() {
		String amountStr = etAmount.getText().toString();
		
		if ( amountStr.isEmpty() || amountStr == null ) {
			tvTipAmount.setText("");
			tvTipSplit.setText("");
			return;
		}
		
		Double amount = isNumeric(amountStr);
		if ( amount.isNaN() ) {
			tvTipAmount.setText(NOT_A_NUMBER);
			return;
		}
		
		// validate input
		
		
		// convert to percentage
		Double tipAmount = amount * tipPercentage * PERCENT;
		String result = String.format(Locale.getDefault(), "Tip is:  $%.2f", tipAmount);
		tvTipAmount.setText( result );
		
		// call for split
		calculateSplit();
	}

	
	/**
	 * Method to calculate the split for the user
	 */
	protected void calculateSplit() {
		// Retrieve current tip
		String tipStr = tvTipAmount.getText().toString();
		String[] tokens = tipStr.split("\\$");
		if ( tokens.length < 2 ) {
			return;
		}
		// tip value is after the dollar sign
		Double tip = isNumeric(tokens[1]);
		if ( tip.isNaN() ) {
			return;
		}
		
		// Retrieve split
		String splitStr = etSplit.getText().toString();
		if ( splitStr == null || splitStr.isEmpty() ) {
			tvTipSplit.setText("");
			return;
		}
		tipSplit = isNumeric(splitStr);
		
		// Retrieve split
		if ( tipSplit.isNaN() ) {
			Log.d("INFO", "Tip split is NaN");
			tvTipSplit.setText(NOT_A_NUMBER);
			return;
		}
		
		if ( tipSplit <= 0 ) {
			return;
		}
		
		// both values are valid
		Double tipAfterSplit = tip / tipSplit;
		String result = String.format(Locale.getDefault(),  "Split is:  $%.2f", tipAfterSplit );
		tvTipSplit.setText(result);
	}
	
	/**
	 * Checks if the string is a number.
	 * 
	 * @param str
	 * @return
	 */
	public static Double isNumeric(String str )  
	{  
		boolean isNumber = str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
		if ( !isNumber ) {
			return Double.NaN;
		}
		
		Double result;
		try  
		{  
			result = Double.parseDouble(str);
		}  
		catch(NumberFormatException nfe)  
		{  
			result = Double.NaN;  
		}  
		return result;  
	}
	
	
}
