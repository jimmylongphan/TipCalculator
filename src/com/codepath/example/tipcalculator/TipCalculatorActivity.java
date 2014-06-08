package com.codepath.example.tipcalculator;

import java.util.Locale;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class TipCalculatorActivity extends Activity {

	// constants
	public static final double PERCENT = 0.01;
	
	// values used for tip calculation
	public static final double[] TIP_PRESET = { 10, 15, 20 };
	
	private Double tipPercentage = TIP_PRESET[0] * PERCENT;
	
	// local view references
	EditText etAmount;
	EditText etAmountCustom;
	TextView tvTipAmount;
	Button btnTip0;
	Button btnTip1;
	Button btnTip2;
	Button btnTipCustom;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_tip_calculator);
		
		// set the text for the 3 tip buttons
		etAmount = (EditText) findViewById(R.id.etAmount);
		etAmountCustom = (EditText) findViewById(R.id.etAmountCustom);
		tvTipAmount = (TextView) findViewById(R.id.tvTipAmount);
		btnTip0 = (Button) findViewById(R.id.btnTip0);
		btnTip1 = (Button) findViewById(R.id.btnTip1);
		btnTip2 = (Button) findViewById(R.id.btnTip2);
		btnTipCustom = (Button) findViewById(R.id.btnTipCustom);
		
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
		
		// OPTIONAL
		// custom tip
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
				//onClick( btnTipCustom );
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
		boolean usingPreSet = true;
		
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
	 * Retrieve the value from the edit text.
	 * Perform any necessary checks.
	 * Multiply the tip values to get tip value
	 * 
	 */
	protected void calculateTip() {
		String amountStr = etAmount.getText().toString();
		
		if ( amountStr.isEmpty() || amountStr == null ) {
			tvTipAmount.setText("");
			return;
		}
		
		Double amount = isNumeric(amountStr);
		
		// validate input
		if ( amount.isNaN() || tipPercentage.isNaN() ) {
			//Toast.makeText(this, "Is not a number!", Toast.LENGTH_SHORT).show();
			tvTipAmount.setText("Is not a number!");
			
			return;
		}
		
		// convert to percentage
		Double tipAmount = amount * tipPercentage * PERCENT;
		String tipStr = String.format(Locale.getDefault(), "%.2f", tipAmount);
		
		tvTipAmount.setText( "$" + tipStr );
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
