package com.diamondsoftware.android.massagenearby.model;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

public class ItemMasseur extends ItemUser {

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		if(mIsOnline) {
			return mName + " (online)";
		} else {
			return mName;
		}
	}
	public int getmMasserId() {
		return mMasserId;
	}
	public void setmMasserId(int mMasserId) {
		this.mMasserId = mMasserId;
	}
	public boolean ismIsOnline() {
		return mIsOnline;
	}
	public void setmIsOnline(boolean mIsOnline) {
		this.mIsOnline = mIsOnline;
	}
	public Socket getmSocket() {
		return mSocket;
	}
	public void setmSocket(Socket mSocket) {
		this.mSocket = mSocket;
	}
	int mMasserId;
	boolean mIsOnline;
	Socket mSocket;
	boolean mConnected=false;
	String MainPictureURL;
	String CertifiedPictureURL;
	GregorianCalendar	Birthdate;
	String Height;
	String Ethnicity;
	ArrayList<String> Services;
	String Bio;
	GregorianCalendar SubscriptionEndDate;
	/**
	 * @return the privatePicture1URL
	 */
	public String getPrivatePicture1URL() {
		return PrivatePicture1URL;
	}
	/**
	 * @param privatePicture1URL the privatePicture1URL to set
	 */
	public void setPrivatePicture1URL(String privatePicture1URL) {
		PrivatePicture1URL = privatePicture1URL;
	}
	/**
	 * @return the privatePicture2URL
	 */
	public String getPrivatePicture2URL() {
		return PrivatePicture2URL;
	}
	/**
	 * @param privatePicture2URL the privatePicture2URL to set
	 */
	public void setPrivatePicture2URL(String privatePicture2URL) {
		PrivatePicture2URL = privatePicture2URL;
	}
	/**
	 * @return the privatePicture3URL
	 */
	public String getPrivatePicture3URL() {
		return PrivatePicture3URL;
	}
	/**
	 * @param privatePicture3URL the privatePicture3URL to set
	 */
	public void setPrivatePicture3URL(String privatePicture3URL) {
		PrivatePicture3URL = privatePicture3URL;
	}
	/**
	 * @return the privatePicture4URL
	 */
	public String getPrivatePicture4URL() {
		return PrivatePicture4URL;
	}
	/**
	 * @param privatePicture4URL the privatePicture4URL to set
	 */
	public void setPrivatePicture4URL(String privatePicture4URL) {
		PrivatePicture4URL = privatePicture4URL;
	}
	String PrivatePicture1URL;
	String PrivatePicture2URL;
	String PrivatePicture3URL;
	String PrivatePicture4URL;

	/**
	 * @return the birthdate
	 */
	public GregorianCalendar getBirthdate() {
		return Birthdate;
	}
	/**
	 * @param gregorianCalendar the birthdate to set
	 */
	public void setBirthdate(GregorianCalendar gregorianCalendar) {
		Birthdate = gregorianCalendar;
	}
	/**
	 * @return the height
	 */
	public String getHeight() {
		return Height;
	}
	/**
	 * @param height the height to set
	 */
	public void setHeight(String height) {
		Height = height;
	}
	/**
	 * @return the ethnicity
	 */
	public String getEthnicity() {
		return Ethnicity;
	}
	/**
	 * @param ethnicity the ethnicity to set
	 */
	public void setEthnicity(String ethnicity) {
		Ethnicity = ethnicity;
	}
	/**
	 * @return the service - formatted as ^-delmited string
	 */
	public String getServices() {
		StringBuilder sb=new StringBuilder();
		String carrot="";
		for(String svc: Services) {
			sb.append(carrot+svc);
			carrot="^";
		}
		return sb.toString();
	}
	/**
	 * @param service the service to set n -- requires a ^-delimited string
	 */
	public void setServices(String service) {
		ArrayList<String> aL=new ArrayList<String>();
		String[] sa=service.split("\\^",-1);
		for(int c=0;c<sa.length;c++) {
			String str=sa[c].trim();
			if(str.length()>0) {
				aL.add(str);
			}
		}
		Services = aL;
	}
	/**
	 * @return the bio
	 */
	public String getBio() {
		return Bio;
	}
	/**
	 * @param bio the bio to set
	 */
	public void setBio(String bio) {
		Bio = bio;
	}
	/**
	 * @return the subscriptionEndDate
	 */
	public GregorianCalendar getSubscriptionEndDate() {
		return SubscriptionEndDate;
	}
	/**
	 * @param subscriptionEndDate the subscriptionEndDate to set
	 */
	public void setSubscriptionEndDate(GregorianCalendar subscriptionEndDate) {
		SubscriptionEndDate = subscriptionEndDate;
	}
	/**
	 * @return the longitude
	 */
	public double getLongitude() {
		return Longitude;
	}
	/**
	 * @param longitude the longitude to set
	 */
	public void setLongitude(double longitude) {
		Longitude = longitude;
	}
	/**
	 * @return the latitude
	 */
	public double getLatitude() {
		return Latitude;
	}
	/**
	 * @param latitude the latitude to set
	 */
	public void setLatitude(double latitude) {
		Latitude = latitude;
	}
	double Longitude;
	double Latitude;
	/**
	 * @return the mainPictureURL
	 */
	public String getMainPictureURL() {
		return MainPictureURL;
	}
	/**
	 * @param mainPictureURL the mainPictureURL to set
	 */
	public void setMainPictureURL(String mainPictureURL) {
		MainPictureURL = mainPictureURL;
	}
	/**
	 * @return the certifiedPictureURL
	 */
	public String getCertifiedPictureURL() {
		return CertifiedPictureURL;
	}
	/**
	 * @param certifiedPictureURL the certifiedPictureURL to set
	 */
	public void setCertifiedPictureURL(String certifiedPictureURL) {
		CertifiedPictureURL = certifiedPictureURL;
	}
	/**
	 * @return the mConnected
	 */
	public boolean ismConnected() {
		return mConnected;
	}
	/**
	 * @param mConnected the mConnected to set
	 */
	public void setmConnected(boolean mConnected) {
		this.mConnected = mConnected;
	}
}
