package com.management.hotel.services;

import java.util.Arrays;
import java.util.List;

public class NumberToWords {
	public static void main(String[] args) {
		System.out.println(getWord(10300));
	}

	private static List<String> NUMBERS = Arrays.asList("~,One,Two,Three,Four,Five,Six,Seven,Eight,Nine".split(","));
	private static List<String> NUM1XGRT = Arrays
			.asList("Ten,Eleven,Twelve,Thirteen,Fourteen,Fifteen,Sixteen,Seventeen,Eighteen,Nineteen".split(","));
	private static List<String> NUM2XGRT = Arrays
			.asList("~,~,Twenty,Thirty,Forty,Fifty,Sixty,Seventy,Eighty,Ninety".split(","));
	private static List<String> NUM3XGRT = Arrays
			.asList("~,~,~, Hundreds, Thousands, Thousands, Lakhs, Lakhs".split(","));
	public static String getWord(int num) {
		if (num == 0) {
			return "Zero";
		}
		char[] numChar = (num + "").toCharArray();
		String stringNum = "";
		for (int k = numChar.length - 1; k >= 0; k--) {
			stringNum += numChar[k];
		}
		int i = stringNum.length() - 1;
		String word = "";
		while (i >= 0) {
			int currentNum = Integer.parseInt("" + stringNum.charAt(i));
			if (currentNum == 0) {
				i--;
				continue;
			}
			switch (i + 1) {
			case 1:
				if(!word.trim().isEmpty() && stringNum.charAt(1)=='0') {
					word+=" And";
				}
				word += " " + NUMBERS.get(currentNum);
				break;
			case 2:
				if(!word.trim().isEmpty()) {
					word+=" And";
				}
				if (currentNum == 1) {
					word += " " + NUM1XGRT.get(Integer.parseInt("" + stringNum.charAt(i - 1)));
					i = -1;
				} else {
					word += " " + NUM2XGRT.get(currentNum);
				}

				break;
			case 3:
				if(!word.trim().isEmpty()) {
					word+=" And";
				}
				word += " " + NUMBERS.get(currentNum) + NUM3XGRT.get(3);
				break;
			case 4:
				if(!word.trim().isEmpty() && stringNum.charAt(4)=='0') {
					word+=" And";
				}
				word += " " + NUMBERS.get(currentNum) + NUM3XGRT.get(4);
				break;
			case 5:
				if(!word.trim().isEmpty() && stringNum.charAt(4)=='0') {
					word+=" And";
				}
				if (currentNum == 1) {
					word += " " + NUM1XGRT.get(Integer.parseInt("" + stringNum.charAt(i - 1))) ;
					i--;
				} else {
					word += " " + NUM2XGRT.get(currentNum);
				}
				if(stringNum.charAt(3)=='0') {
					word +=  NUM3XGRT.get(5);
				}
				break;
			case 6:
				if(!word.trim().isEmpty() && stringNum.charAt(4)=='0') {
					word+=" And";
				}
				word += " " + NUMBERS.get(currentNum) + NUM3XGRT.get(6);
				break;
			case 7:
				if(!word.trim().isEmpty() && stringNum.charAt(4)=='0') {
					word+=" And";
				}
				if (currentNum == 1) {
					word += " " + NUM1XGRT.get(Integer.parseInt("" + stringNum.charAt(i - 1))) ;
					i--;
				} else {
					word += " " + NUM2XGRT.get(currentNum);
				}
				if(stringNum.charAt(3)=='0') {
					word += NUM3XGRT.get(7);
				}
				break;
				
			}
			
			
			i--;
		}

		return word.trim();
	}
}
