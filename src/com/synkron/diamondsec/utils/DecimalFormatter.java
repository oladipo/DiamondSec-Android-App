package com.synkron.diamondsec.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class DecimalFormatter {
	
	public static String Format(Double numberToFormat){
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		
		return formatter.format(numberToFormat);
	}
	
	public static String Format(String numberToFormat){
		DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.getDefault());
		formatter.setMaximumFractionDigits(2);
		formatter.setMinimumFractionDigits(2);
		
		return formatter.format(Double.parseDouble(numberToFormat));
	}
}
