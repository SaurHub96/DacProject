package com.management.hotel.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.management.hotel.controllers.model.MessageConstant;

public class Validations {

	public static void isValid(String type,String value, Map<String, Object> result) {
		isValid(type,null,value, result);
	}
	public static void isValid(String type,String name,String value, Map<String, Object> result) {
		if(!isValid(type, value)) {
			List<String> list=null;
			if(result.containsKey(MessageConstant.validation)) {
				list=(List<String>) result.get(MessageConstant.validation);
				list.add(name!=null?name:type.toString());
			}else {
				list=new ArrayList<>();
				list.add(name!=null?name:type.toString());
				result.put(MessageConstant.validation, list);
			}
			
		}
	}
	public static boolean isValid(String type,String value) {
		boolean res=false;
		switch (type) {
		case "email":
			res=value!=null && !value.trim().isEmpty() && value.matches("^[A-Za-z0-9+_.-]+@(.+)$");
			break;
			
		case "password":
			res=value!=null && !value.trim().isEmpty() && value.length()>3;
			break;
			
		case "phone":
			res=value!=null && !value.trim().isEmpty() && !value.equalsIgnoreCase("null") && value.length()>5 && value.length()<=10;
			break;
			
	   case "idNumber":
		     res=value!=null && !value.trim().isEmpty() && !value.equalsIgnoreCase("null") && value.length()>5 && value.length()<13;
		    break;
		    
		case "name":
			if(value!=null && value.length()>2 && value.length()<30) {
				res=value.matches("^[\\p{L} .'-]+$");
			}
			break;
			
		case "long":
			try {
				Long.parseLong(value);
				res=true;
			}catch (Exception e) {
				
			}
			break;
			
		case "double":
			try {
				Double.parseDouble(value);
				res=true;
			}catch (Exception e) {
				
			}
			break;
			
		case "country":
			res=countries.contains(value);
			break;
			
		default:{
			res=value!=null && !value.trim().isEmpty() && !value.equalsIgnoreCase("null");
			break;
		}
		}
		return res;
	}
	public static List<String> countries=Arrays.asList(
			"Afghanistan",
			"Albania",
			"Algeria",
			"Andorra",
			"Angola",
			"Antigua and Barbuda",
			"Argentina",
			"Armenia",
			"Australia",
			"Austria",
			"Azerbaijan",
			"Bahamas",
			"Bahrain",
			"Bangladesh",
			"Barbados",
			"Belarus",
			"Belgium",
			"Belize",
			"Benin",
			"Bhutan",
			"Bolivia",
			"Bosnia and Herzegovina",
			"Botswana",
			"Brazil",
			"Brunei",
			"Bulgaria",
			"Burkina Faso",
			"Burundi",
			"Côte d'Ivoire",
			"Cabo Verde",
			"Cambodia",
			"Cameroon",
			"Canada",
			"Central African Republic",
			"Chad",
			"Chile",
			"China",
			"Colombia",
			"Comoros",
			"Congo (Congo-Brazzaville)",
			"Costa Rica",
			"Croatia",
			"Cuba",
			"Cyprus",
			"Czechia (Czech Republic)",
			"Democratic Republic of the Congo",
			"Denmark",
			"Djibouti",
			"Dominica",
			"Dominican Republic",
			"Ecuador",
			"Egypt",
			"El Salvador",
			"Equatorial Guinea",
			"Eritrea",
			"Estonia",
			"Eswatini (fmr. Swaziland)",
			"Ethiopia",
			"Fiji",
			"Finland",
			"France",
			"Gabon",
			"Gambia",
			"Georgia",
			"Germany",
			"Ghana",
			"Greece",
			"Grenada",
			"Guatemala",
			"Guinea",
			"Guinea-Bissau",
			"Guyana",
			"Haiti",
			"Holy See",
			"Honduras",
			"Hungary",
			"Iceland",
			"India",
			"Indonesia",
			"Iran",
			"Iraq",
			"Ireland",
			"Israel",
			"Italy",
			"Jamaica",
			"Japan",
			"Jordan",
			"Kazakhstan",
			"Kenya",
			"Kiribati",
			"Kuwait",
			"Kyrgyzstan",
			"Laos",
			"Latvia",
			"Lebanon",
			"Lesotho",
			"Liberia",
			"Libya",
			"Liechtenstein",
			"Lithuania",
			"Luxembourg",
			"Madagascar",
			"Malawi",
			"Malaysia",
			"Maldives",
			"Mali",
			"Malta",
			"Marshall Islands",
			"Mauritania",
			"Mauritius",
			"Mexico",
			"Micronesia",
			"Moldova",
			"Monaco",
			"Mongolia",
			"Montenegro",
			"Morocco",
			"Mozambique",
			"Myanmar (formerly Burma)",
			"Namibia",
			"Nauru",
			"Nepal",
			"Netherlands",
			"New Zealand",
			"Nicaragua",
			"Niger",
			"Nigeria",
			"North Korea",
			"North Macedonia",
			"Norway",
			"Oman",
			"Pakistan",
			"Palau",
			"Palestine State",
			"Panama",
			"Papua New Guinea",
			"Paraguay",
			"Peru",
			"Philippines",
			"Poland",
			"Portugal",
			"Qatar",
			"Romania",
			"Russia",
			"Rwanda",
			"Saint Kitts and Nevis",
			"Saint Lucia",
			"Saint Vincent and the Grenadines",
			"Samoa",
			"San Marino",
			"Sao Tome and Principe",
			"Saudi Arabia",
			"Senegal",
			"Serbia",
			"Seychelles",
			"Sierra Leone",
			"Singapore",
			"Slovakia",
			"Slovenia",
			"Solomon Islands",
			"Somalia",
			"South Africa",
			"South Korea",
			"South Sudan",
			"Spain",
			"Sri Lanka",
			"Sudan",
			"Suriname",
			"Sweden",
			"Switzerland",
			"Syria",
			"Tajikistan",
			"Tanzania",
			"Thailand",
			"Timor-Leste",
			"Togo",
			"Tonga",
			"Trinidad and Tobago",
			"Tunisia",
			"Turkey",
			"Turkmenistan",
			"Tuvalu",
			"Uganda",
			"Ukraine",
			"United Arab Emirates",
			"United Kingdom",
			"United States of America",
			"Uruguay",
			"Uzbekistan",
			"Vanuatu",
			"Venezuela",
			"Vietnam",
			"Yemen",
			"Zambia",
			"Zimbabwe"
			);
	
}
