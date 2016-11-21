package com.parisk85;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Locator {
	private static String URL = "http://www.mapquestapi.com/geocoding/v1/address?key=SaY9oeWzmRRRcQQ5U4TY33JgHrF8QUsi&location=";

	private static String prepareAddress(String address) {
		StringBuilder stringBuilder = new StringBuilder();
		address = address.replace(" ", ",");
		String[] strings = address.split("[$&+,:;=?@#|'<>.^*()%!-]");
		for (String s : strings) {
			stringBuilder.append(s.trim());
			stringBuilder.append("+");
		}
		return stringBuilder.substring(0, stringBuilder.length() - 1);
	}

	public static Location geocode(String address) {
		try {
			URL url = new URL(URL + prepareAddress(address));
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");

			if (conn.getResponseCode() != 200) {
				throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
			}

			BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

			String output;
			StringBuilder str = new StringBuilder();
			while ((output = br.readLine()) != null) {
				str.append(output);
			}

			try {
				JSONObject obj = new JSONObject(str.toString());
				JSONArray arr = obj.getJSONArray("results");
				obj = arr.getJSONObject(0);
				arr = obj.getJSONArray("locations");
				obj = arr.getJSONObject(0);
				//System.out.println(obj);
				JSONObject latlng = obj.getJSONObject("latLng");
				double lat = latlng.getDouble("lat");
				double lng = latlng.getDouble("lng");
				String city = obj.getString("adminArea5");
				String postalCode = obj.getString("postalCode");
				String state = obj.getString("adminArea3");
				String street = obj.getString("street");
				String country = obj.getString("adminArea1");
				
				Location location = new Location();
				location.setState(state);
				location.setCity(city);
				location.setLat(lat);
				location.setLng(lng);
				location.setPostalCode(postalCode);
				location.setStreet(street);
				location.setCountry(country);
				
				return location;
			} catch (JSONException e) {
				e.printStackTrace();
			}
			conn.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}

	public static void reverse() {

	}
}
