package com.diamondsoftware.android.masseur;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SettingsManager {
	private SharedPreferences mSharedPreferences;
	private Context mContext;
	
	public SettingsManager(Context context) {
		mSharedPreferences=context.getSharedPreferences(context.getPackageName() + "_preferences", Activity.MODE_PRIVATE);
		mContext=context;
	}
	private String getValue(String key, String defValue) {
		return mSharedPreferences.getString(key, defValue);
	}
	private void setValue(String key, String value) {
		Editor editor=mSharedPreferences.edit();
		editor.putString(key,value);
		editor.commit();				
	}
	public boolean getIsEnabledMasseur() {
		String value= getValue(ApplicationMasseur.KEY_IS_ENABLED_MASSEUR,"true");
		return value.equals("true")?true:false;
	}
	public void setIsEnabledMasseur(boolean value) {
		Editor editor=mSharedPreferences.edit();
		editor.putString(ApplicationMasseur.KEY_IS_ENABLED_MASSEUR, value?"true":"false");
		editor.commit();
	}
	public String getMasseurName() {
		return getValue(ApplicationMasseur.KEY_MASSEUR_NAME,null);
	}
	public String setMasseurName(String value) {
		return getValue(ApplicationMasseur.KEY_MASSEUR_NAME,value);
	}
	public int getLoggingLevel() {
		String value=getValue(com.diamondsoftware.android.common.GlobalStaticValues.KEY_LOGGINGLEVEL,String.valueOf(com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL));
		return Integer.valueOf(value);
	}
	public void setLoggingLevel(int value) {
		setValue(com.diamondsoftware.android.common.GlobalStaticValues.KEY_LOGGINGLEVEL, String.valueOf(value));
	}
}
