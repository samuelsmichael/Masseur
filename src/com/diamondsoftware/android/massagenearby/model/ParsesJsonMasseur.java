package com.diamondsoftware.android.massagenearby.model;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.diamondsoftware.android.common.ParsesJson;
import com.diamondsoftware.android.common.Utils;

public class ParsesJsonMasseur extends ParsesJson {
	String mName;
	public ParsesJsonMasseur(String name) {
		mName=name;
	}

	@Override
	protected ArrayList<Object> parse(String jsonString) throws Exception {
		ArrayList<Object> items = new ArrayList<Object>();
		JSONArray jsonArray = new JSONArray(jsonString);
		int c=jsonArray.length();
		for(int i=0;i<c;i++) {
			ItemMasseur item=new ItemMasseur();
			JSONObject jsonObject = jsonArray.getJSONObject(i);
			item.setmIsOnline(jsonObject.getBoolean("IsOnline"));
			item.setmMasserId(jsonObject.getInt("MasseurId"));
			item.setmName(jsonObject.getString("Name"));
			item.setmURL(jsonObject.getString("URL"));
			item.setmUserId(jsonObject.getInt("UserId"));
			item.setMainPictureURL(jsonObject.getString("MainPictureURL"));
			item.setCertifiedPictureURL(jsonObject.getString("CertifiedPictureURL"));
			item.setLongitude(jsonObject.getDouble("Longitude"));
			item.setLatitude(jsonObject.getDouble("Latitude"));
			item.setBirthdate(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("Birthdate")));
			item.setHeight(jsonObject.getString("Height"));
			item.setEthnicity(jsonObject.getString("Ethnicity"));
			item.setServices(jsonObject.getString("Services"));
			item.setBio(jsonObject.getString("Bio"));
			item.setSubscriptionEndDate(Utils.toDateFromYYYYdashMMdashDDTHHcolonMMcolonSS(jsonObject.getString("SubscriptionEndDate")));
			item.setPrivatePicture1URL(jsonObject.getString("PrivatePicture1URL"));
			item.setPrivatePicture2URL(jsonObject.getString("PrivatePicture2URL"));
			item.setPrivatePicture3URL(jsonObject.getString("PrivatePicture3URL"));
			item.setPrivatePicture4URL(jsonObject.getString("PrivatePicture4URL"));
			items.add(item);
		}
		return items;
	}

}
