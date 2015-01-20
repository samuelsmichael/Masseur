package com.diamondsoftware.android.massagenearby.model;

import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import android.text.TextUtils;

import com.diamondsoftware.android.common.Utils;

public class ItemMasseur extends ItemUser implements ObtainDBUpdateQueryString {

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
	String PrivatePicture1URL;
	String PrivatePicture2URL;
	String PrivatePicture3URL;
	String PrivatePicture4URL;
	int Port;

	/**
	 * @return the port
	 */
	public int getPort() {
		return Port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		Port = port;
	}
	/**
	 * @return the isCertified
	 */
	public boolean isIsCertified() {
		return IsCertified;
	}
	/**
	 * @param isCertified the isCertified to set
	 */
	public void setIsCertified(boolean isCertified) {
		IsCertified = isCertified;
	}
	/**
	 * @return the certificationNumber
	 */
	public int getCertificationNumber() {
		return CertificationNumber;
	}
	/**
	 * @param certificationNumber the certificationNumber to set
	 */
	public void setCertificationNumber(int certificationNumber) {
		CertificationNumber = certificationNumber;
	}
	boolean IsCertified;
	int CertificationNumber;

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
		if(Services!=null) {
			for(String svc: Services) {
				sb.append(carrot+svc);
				carrot="^";
			}
		}
		return sb.toString();
	}
	public ArrayList<String> getServicesAsArrayList() {
		return Services;
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
	public void setServicesAsArrayList(ArrayList<String>svcs) {
		Services=svcs;
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
	@Override
	public String getDBQueryStringEncoded() {
		StringBuilder sb=new StringBuilder();
		sb.append("Action=set&UserId="+mUserId);
		
		if(!TextUtils.isEmpty(mName)) {
			sb.append("&Name="+URLEncoder.encode(mName));
		}
		if(!TextUtils.isEmpty(mURL)) {
			sb.append("&URL="+URLEncoder.encode(this.getmURL()));
		}

		if(!TextUtils.isEmpty(Password)) {
			sb.append("&Password="+URLEncoder.encode(Password));
		}		
		if(!TextUtils.isEmpty(Email)) {
			sb.append("&Email="+URLEncoder.encode(Email));
		}	
		if(!TextUtils.isEmpty(MainPictureURL)) {
			sb.append("&MainPictureURL="+URLEncoder.encode(MainPictureURL));
		} else {
			sb.append("&MainPictureURL=^");
		}
		if(!TextUtils.isEmpty(CertifiedPictureURL)) {
			sb.append("&CertifiedPictureURL="+URLEncoder.encode(CertifiedPictureURL));
		} else {
			sb.append("&CertifiedPictureURL="+URLEncoder.encode("^"));
		}
		
		if(!Utils.isNullDate(Birthdate)) {
			sb.append("&Birthdate="+URLEncoder.encode(Utils.toStringYYYYdashMMdashDDTHHcolonMMcolonSSFromDate(Birthdate)));
		}
		if(!Utils.isNullDate(SubscriptionEndDate)) {
			sb.append("&SubscriptionEndDate="+URLEncoder.encode(Utils.toStringYYYYdashMMdashDDTHHcolonMMcolonSSFromDate(SubscriptionEndDate)));
		}
		
		if(Longitude!=0) {
			sb.append("&Longitude="+URLEncoder.encode(String.valueOf(Longitude)));
		}
		if(Latitude!=0) {
			sb.append("&Latitude="+URLEncoder.encode(String.valueOf(Latitude)));
		}
		
		if(!TextUtils.isEmpty(Height)) {
			sb.append("&Height="+URLEncoder.encode(Height));
		}
				
		if(Ethnicity!=null) {
			sb.append("&Ethnicity="+URLEncoder.encode(Ethnicity));
		}
		if(Services!=null) {
			sb.append("&Services="+URLEncoder.encode(getServices())); // Note how I have to use getServices ... since this method codes the ArrayList to a String
		}
		if(!TextUtils.isEmpty(Bio)) {
			sb.append("&Bio="+URLEncoder.encode(Bio));
		}
		if(!TextUtils.isEmpty(PrivatePicture1URL)) {
			sb.append("&PrivatePicture1URL="+URLEncoder.encode(PrivatePicture1URL));
		} else {
			sb.append("&PrivatePicture1URL="+URLEncoder.encode("^")); // by convention
		}
		if(!TextUtils.isEmpty(PrivatePicture2URL)) {
			sb.append("&PrivatePicture2URL="+URLEncoder.encode(PrivatePicture2URL));
		} else {
			sb.append("&PrivatePicture2URL="+URLEncoder.encode("^")); // by convention
		}
		if(!TextUtils.isEmpty(PrivatePicture3URL)) {
			sb.append("&PrivatePicture3URL="+URLEncoder.encode(PrivatePicture3URL));
		} else {
			sb.append("&PrivatePicture3URL="+URLEncoder.encode("^")); // by convention
		}
		if(!TextUtils.isEmpty(PrivatePicture4URL)) {
			sb.append("&PrivatePicture4URL="+URLEncoder.encode(PrivatePicture4URL));
		} else {
			sb.append("&PrivatePicture4URL="+URLEncoder.encode("^")); // by convention
		}

		return sb.toString();
	}
	@Override
	public String getDBQueryString() {
		StringBuilder sb=new StringBuilder();
		sb.append("Action=set&UserId="+mUserId);
		if(!TextUtils.isEmpty(mName)) {
			sb.append("&Name="+mName);
		}
		if(MainPictureURL!=null) {
			sb.append("&MainPictureURL="+MainPictureURL);
		} else {
			sb.append("&MainPictureURL=^");
		}
		if(!TextUtils.isEmpty(CertifiedPictureURL)) {
			sb.append("&CertifiedPictureURL="+CertifiedPictureURL);
		} else {
			sb.append("&CertifiedPictureURL=^");
		}
		
		if(!Utils.isNullDate(Birthdate)) {
			sb.append("&Birthdate="+Utils.toStringYYYYdashMMdashDDTHHcolonMMcolonSSFromDate(Birthdate));
		}
		if(!Utils.isNullDate(SubscriptionEndDate)) {
			sb.append("&SubscriptionEndDate="+Utils.toStringYYYYdashMMdashDDTHHcolonMMcolonSSFromDate(SubscriptionEndDate));
		}
		
		if(Longitude!=0) {
			sb.append("&Longitude="+String.valueOf(Longitude));
		}
		if(Latitude!=0) {
			sb.append("&Latitude="+String.valueOf(Latitude));
		}
		
		if(!TextUtils.isEmpty(Height)) {
			sb.append("&Height="+Height);
		}
				
		if(!TextUtils.isEmpty(Ethnicity)) {
			sb.append("&Ethnicity="+Ethnicity);
		}
		if(Services!=null) {
			sb.append("&Services="+getServices()); // Note how I have to use getServices ... since this method codes the ArrayList to a String
		}
		if(!TextUtils.isEmpty(Bio)) {
			sb.append("&Bio="+Bio);
		}
		if(!TextUtils.isEmpty(Password)) {
			sb.append("&Password="+Password);
		}		
		if(!TextUtils.isEmpty(Email)) {
			sb.append("&Email="+Email);
		}		
		if(!TextUtils.isEmpty(PrivatePicture1URL)) {
			sb.append("&PrivatePicture1URL="+PrivatePicture1URL);
		} else {
			sb.append("&PrivatePicture1URL="+"^"); // by convention
		}
		if(!TextUtils.isEmpty(PrivatePicture2URL)) {
			sb.append("&PrivatePicture2URL="+PrivatePicture2URL);
		} else {
			sb.append("&PrivatePicture2URL"+"^"); // by convention
		}
		if(!TextUtils.isEmpty(PrivatePicture3URL)) {
			sb.append("&PrivatePicture3URL="+PrivatePicture3URL);
		} else {
			sb.append("&PrivatePicture3URL="+"^"); // by convention
		}
		if(!TextUtils.isEmpty(PrivatePicture4URL)) {
			sb.append("&PrivatePicture4URL="+PrivatePicture4URL);
		} else {
			sb.append("&PrivatePicture4URL="+"^"); // by convention
		}
		sb.append("&CertificationNumber="+this.getCertificationNumber());
		if(!TextUtils.isEmpty(mURL)) {
			sb.append("&URL="+this.getmURL());
		}
		return sb.toString();
	}
}
