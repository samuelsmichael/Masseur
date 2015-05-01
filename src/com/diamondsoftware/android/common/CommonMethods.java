package com.diamondsoftware.android.common;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Pattern;

import com.diamondsoftware.android.massagenearby.common.SettingsManager;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

public class CommonMethods {

	   public static String getLocalIpAddress(Context context) {
	        try {
	        	String zHostAddressIPV4=null;
	        	String zHostAddressIPV6=null;
	        	String zHostAddress=null;
	            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
	                NetworkInterface intf = en.nextElement();
	                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
	                    InetAddress inetAddress = enumIpAddr.nextElement();	                    
                    	String hostAddress=inetAddress.getHostAddress().toString();
	            //		new Logger(new SettingsManager(context).getLoggingLevel(),"MasseurSocketService",context).log("Got inetaddress: " + hostAddress, com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
	                    if (!inetAddress.isLoopbackAddress() && !inetAddress.isLinkLocalAddress()) {
	    		    
	                    	if(Pattern.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", hostAddress)) {
		                    	if(zHostAddressIPV4==null) {
		                    		zHostAddressIPV4=hostAddress;
		                    	}
	                    	} else {
		                    	if(zHostAddressIPV6==null) {
		                    		zHostAddressIPV6=hostAddress;
		                    	}
	                    	}
	                    }
	                }
	            }
	            zHostAddress=
	            		zHostAddressIPV6!=null? zHostAddressIPV6:zHostAddressIPV4;
	            if(Pattern.matches("^(?:[0-9]{1,3}\\.){3}[0-9]{1,3}$", zHostAddress)) {
	            	zHostAddress="::ffff:"+zHostAddress;
	            }
	            return zHostAddress;
	        } catch (SocketException ex) {
	            Log.e("tag", ex.toString());
	        }
	        return null;
	    }
    
    public static String getCurrentSsid(Context context) {

    	  String ssid = null;
    	  ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    	  NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
    	  if (networkInfo.isConnected()) {
    	    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    	    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
    	    if (connectionInfo != null && !(connectionInfo.getSSID().equals(""))) {
    	        //if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getSSID())) {
    	      ssid = connectionInfo.getSSID();
    	    }
    	  }
    	  return ssid;
    	}
}
