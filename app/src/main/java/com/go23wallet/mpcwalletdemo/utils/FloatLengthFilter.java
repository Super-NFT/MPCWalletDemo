package com.go23wallet.mpcwalletdemo.utils;

import android.text.InputFilter;
import android.text.Spanned;

public class FloatLengthFilter implements InputFilter {
 private int decimal_length = 0;

 public FloatLengthFilter(int decimal_length) {
     this.decimal_length = decimal_length;
 }

 public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
     if ("".equals(source.toString())) {
         return null;
     }
     String value = dest.toString();
     String[] splitArray = value.split("\\.");
     if (splitArray.length > 1) {
         String dotValue = splitArray[1];
         int diff = dotValue.length() + 1 - decimal_length;
         if (diff > 0) {
             return source.subSequence(start, end - diff);
         }
     }
     return null;
 }
}