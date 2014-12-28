package com.diamondsoftware.android.common;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;

public class LocationHelper implements
	GooglePlayServicesClient.ConnectionCallbacks,
	GooglePlayServicesClient.OnConnectionFailedListener, LocationListener  {
	Location mCurrentLocation;
	Context mContext;
	LocationClient mLocationClient;
	LocationWanter mLocationWanter;
	LocationRequest mLocationRequest;
	private long mLocationRequestInterval;
	boolean mJustOneLook;
	
	
	public LocationHelper(Context context) {
		mContext=context;
	}

	private void getNextLocationReadingImplementer(LocationWanter locationWanter, long locReqInterval) {
		mLocationWanter=locationWanter;
		mLocationRequestInterval=locReqInterval;
		start();
	}
	public void getNextLocationReadingAndKeepUpdatingMe(LocationWanter locationWanter, long locReqInterval) {
		mJustOneLook=false;
		getNextLocationReadingImplementer(locationWanter, locReqInterval);
	}
	public void getNextLocationReadingJustOnce(LocationWanter locationWanter) {
		mJustOneLook=true;
		getNextLocationReadingImplementer(locationWanter, 2500);
	}
	
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onConnected(Bundle arg0) {
		mLocationClient.requestLocationUpdates(getLocationRequest(), this); 
	}

	@Override
	public void onDisconnected() {
		// TODO Auto-generated method stub
		
	}	
    private LocationRequest getLocationRequest() {
    	if(mLocationRequest==null) {
            mLocationRequest = LocationRequest.create();
            // Use high accuracy
            mLocationRequest.setPriority(
                    LocationRequest.PRIORITY_HIGH_ACCURACY);
            // Set the update interval
            mLocationRequest.setInterval(mLocationRequestInterval);
            // Set the fastest update interval to 1 second
            mLocationRequest.setFastestInterval((int)(mLocationRequestInterval/2));
    	}
    	return mLocationRequest;
    }
    
	private int mDontReenter=0;
	public void start() {
		if(mDontReenter==0) {
			mDontReenter=1;
			
			if(this.mLocationClient==null || !(mLocationClient.isConnected() || mLocationClient.isConnecting())) {
		        mLocationClient = new LocationClient(mContext, this, this);
		        mLocationClient.connect();			}
			mDontReenter=0;
		}
	}
    int mDontReenter3=0;
	public void close() {
		if(mDontReenter3==0) {
			mDontReenter3=1;
		
			if (mLocationClient!=null) {

		        if (mLocationClient.isConnected()) {
		            /*
		             * Remove location updates for a listener.
		             * The current Activity is the listener, so
		             * the argument is "this".
		             */
		        	mLocationClient.removeLocationUpdates(this);
		            mLocationClient.disconnect();
		        }
			}
			mDontReenter3=0;
		}
	}


	@Override
	public void onLocationChanged(Location location) {
		if(mJustOneLook) {
			close();
		}
		this.mLocationWanter.heresYourLocation(location);
	}
}
