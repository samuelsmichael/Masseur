package com.diamondsoftware.android.masseur;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import android.app.Activity;
import android.app.ProgressDialog;
import android.net.Uri;
import android.support.v4.app.FragmentActivity;

import com.diamondsoftware.android.common.HttpFileUpload;
import com.diamondsoftware.android.common.HttpFileUploadParameters;
import com.diamondsoftware.android.common.ManagesFileUploads;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.massagenearby.model.ParsesJsonMasseur;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;

public class NewMasseurPhotoUploadManager implements ManagesFileUploads {
	FragmentActivity mActivity;
	int nbrOfImagesToUpload=0;
	SettingsManager mSettingsManager;
	public NewMasseurPhotoUploadManager(FragmentActivity activity,SettingsManager settingsManager) {
		mActivity=activity;
		mSettingsManager=settingsManager;
		if(MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto!=null) {
			nbrOfImagesToUpload++;
		}
		if(MasseurMainActivity.mSingleton.mCertifiedImage!=null) {
			nbrOfImagesToUpload++;
		}
		if(MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto!=null) {
			Uri selectedImage = MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto;
			
			try {
				InputStream imageStream = mActivity
						.getContentResolver()
						.openInputStream(selectedImage);
				ArrayList<NameValuePair> parms = new ArrayList<NameValuePair>();
				parms.add(new BasicNameValuePair("Transaction",
						"MasseurUploadMainPicture"));
				parms.add(new BasicNameValuePair(
						"UserId",
						String.valueOf(MasseurMainActivity.mSingleton.mItemMasseur_me
								.getmUserId())));
	
				HttpFileUploadParameters params = new HttpFileUploadParameters(
						"http://"
								+ com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(mActivity)
								+ "/MassageNearby/FileUpload.aspx", parms,
						null, // no progress dialog ... all happening in the background 
						imageStream, 
						this,
						MasseurMainActivity.mSingleton.mItemMasseur_me
								.getmUserId());
				new HttpFileUpload().execute(params);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(MasseurMainActivity.mSingleton.mCertifiedImage!=null) {
			Uri selectedImage = MasseurMainActivity.mSingleton.mCertifiedImage;
			
			try {
				InputStream imageStream = mActivity
						.getContentResolver()
						.openInputStream(selectedImage);
				ArrayList<NameValuePair> parms = new ArrayList<NameValuePair>();
				parms.add(new BasicNameValuePair("Transaction",
						"MasseurUploadCertifiedPicture"));
				parms.add(new BasicNameValuePair(
						"UserId",
						String.valueOf(MasseurMainActivity.mSingleton.mItemMasseur_me
								.getmUserId())));
	
				HttpFileUploadParameters params = new HttpFileUploadParameters(
						"http://"
								+ com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(mActivity)
								+ "/MassageNearby/FileUpload.aspx", parms,
						null, // no progress dialog ... all happening in the background 
						imageStream, 
						this,
						MasseurMainActivity.mSingleton.mItemMasseur_me
								.getmUserId());
				new HttpFileUpload().execute(params);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}

	@Override
	public void alert(String message) {
		// TODO Auto-generated method stub

	}

	@Override
	public void heresTheResponse(String response, ArrayList<NameValuePair> parms) {
		try {
			this.nbrOfImagesToUpload--;
			ArrayList<Object> massers=new ParsesJsonMasseur("").parsePublic(response);
			if(massers.size()>0) {
				((ManagesFileUploads) MasseurMainActivity.mSingleton).heresTheResponse(response, parms);
			}
			if(nbrOfImagesToUpload==0) {
				mSettingsManager.setMasseurName(MasseurMainActivity.mSingleton.mItemMasseur_me.getmName());
				((NavigationDrawerCallbacks)  mActivity).onNavigationDrawerItemSelected(0);
			}
		} catch (Exception e) {}
	}

}
