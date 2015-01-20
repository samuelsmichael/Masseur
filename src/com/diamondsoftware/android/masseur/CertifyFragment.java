package com.diamondsoftware.android.masseur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.diamondsoftware.android.common.ConfirmerClient;
import com.diamondsoftware.android.common.DataGetter;
import com.diamondsoftware.android.common.HttpFileUpload;
import com.diamondsoftware.android.common.HttpFileUploadParameters;
import com.diamondsoftware.android.common.ManagesFileUploads;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.common.WaitingForDataAcquiredAsynchronously;
import com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.massagenearby.model.ParsesJsonMasseur;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CertifyFragment extends Fragment_Abstract_NewMasseur implements ManagesFileUploads, ConfirmerClient,
		WaitingForDataAcquiredAsynchronously,
		DataGetter {
	Button btnContinue;
	Button btnGallery;
	Button btnPhoto;
	TextView tvCode;
	private SettingsManager mSettingsManager;
	private ItemMasseur mItemMasseur;
	Uri mSelectedImage;
	private static final int TAKE_PICTURE = 10101;    
	private static final int TAKE_FIRST_PUBLIC_PICTURE = 10203;
	private Uri imageUri;
	private String imDealingWithANewMasseurHere=null;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup= (ViewGroup) inflater.inflate(
				R.layout.fragment_certify, container, false);
		btnPhoto=(Button)viewGroup.findViewById(R.id.btnCertifyCamera);
		btnPhoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View view) {
				    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				    File photo = new File(Environment.getExternalStorageDirectory(),  "Masseur_XMainPicture_"+new Date().getTime()+".jpg");
				    intent.putExtra(MediaStore.EXTRA_OUTPUT,
				            Uri.fromFile(photo));
				    imageUri = Uri.fromFile(photo);
				    startActivityForResult(intent, TAKE_PICTURE);
			}
		});
		btnContinue=(Button)viewGroup.findViewById(R.id.btnCertifyContinue);
		if(isNewMasseur()) {
			btnContinue.setText("Later");
		}
		btnContinue.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				if(isNewMasseur()) {
					writeTheNewMasseur();
				} else {
					getActivity().onBackPressed();
				}
			}
		});
		tvCode=(TextView)viewGroup.findViewById(R.id.tvCertifyNumber);
		tvCode.setText(String.valueOf(mItemMasseur.getCertificationNumber()));
		btnGallery=(Button)viewGroup.findViewById(R.id.btnCertifyGallery);
		btnGallery.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(
						photoPickerIntent,
						com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_CERTIFY);
			}
		});
		
		return viewGroup;
	}

	public static CertifyFragment newInstance(SettingsManager setMan, ItemMasseur im) {
		CertifyFragment frag=new CertifyFragment();
		frag.mSettingsManager=setMan;
		frag.mItemMasseur=im;
		return frag;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_CERTIFY:
			if (resultCode == Activity.RESULT_OK) {
				mSelectedImage = imageReturnedIntent.getData();
				ArrayList<Object> al=new ArrayList<Object>();
				al.add("1");
				al.add(mItemMasseur);
				new Utils.Confirmer2("Confirm Certification Picture", "Do you wish to use this picture?",
						getActivity(), this, al,mSelectedImage,"Yes","No").show(getActivity().getFragmentManager(), "Confirm2_1");
				break;
			}
	    case TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	        	mSelectedImage=imageUri;
				ArrayList<Object> al=new ArrayList<Object>();
				al.add("1");
				al.add(mItemMasseur);
				new Utils.Confirmer2("Confirm Certification Picture", "Do you wish to use this picture?",
						getActivity(), this, al,mSelectedImage,"Yes","No").show(getActivity().getFragmentManager(), "Confirm2_2");
				break;
	        }
		}
		
	}
	@Override
	public void alert(String message) {
		((ManagesFileUploads) getActivity()).alert(message);
	}
	@Override
	public void heresTheResponse(String response, ArrayList<NameValuePair> parms) {
		try {
			ArrayList<Object> massers=new ParsesJsonMasseur("").parsePublic(response);
			if(massers.size()>0) {
				mItemMasseur=(ItemMasseur)massers.get(0);
				((ManagesFileUploads) getActivity()).heresTheResponse(response, parms);
				if(!isNewMasseur()) {
					getActivity().onBackPressed();
				} else {
					///this.mCallbacks.onNavigationDrawerItemSelected(0); // go to HomePage
				}
			}
		} catch (Exception e) {}
	}
	private boolean isNewMasseur() {
		if (imDealingWithANewMasseurHere==null) {
			 boolean retBool=MasseurMainActivity.mSingleton!=null &&
					MasseurMainActivity.mSingleton.mItemMasseur_beingCreated!=null;
			 if(retBool){
				 imDealingWithANewMasseurHere="yes";
			 } else {
				 imDealingWithANewMasseurHere="no";
			 }
		}
		return
			imDealingWithANewMasseurHere.equals("yes");
	}

	@Override
	public void heresYourAnswer(boolean saidYes, ArrayList<Object> otherData) {
		if(saidYes) {
			if(!isNewMasseur()) {
				try {
					InputStream imageStream = getActivity()
							.getContentResolver()
							.openInputStream(mSelectedImage);
					ArrayList<NameValuePair> parms = new ArrayList<NameValuePair>();
					parms.add(new BasicNameValuePair("Transaction",
							"MasseurUploadCertifiedPicture"));
					parms.add(new BasicNameValuePair(
							"UserId",
							String.valueOf(MasseurMainActivity.mSingleton.mItemMasseur_me
									.getmUserId())));
		
					HttpFileUploadParameters params = new HttpFileUploadParameters(
							"http://"
									+ com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(getActivity())
									+ "/MassageNearby/FileUpload.aspx", parms,
							ProgressDialog.show(getActivity(), "Working ...",
									"Uploading " + mSelectedImage.getPath(),
									true, false, null), imageStream, this,
							MasseurMainActivity.mSingleton.mItemMasseur_me
									.getmUserId());
					new HttpFileUpload().execute(params);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				}
			} else { // is new masseur
				// I need to store the uri for a later upload
				MasseurMainActivity.mSingleton.mCertifiedImage=mSelectedImage;
				this.writeTheNewMasseur();
			}
		} else {
			
		}
	}

	@Override
	public ArrayList<Object> getRemoteData(String keyname) {
		ArrayList<Object> data=null;
		String[] array = keyname.split("\\~", -1);
		String key=array[0];
		if(key.equals("UpdateMasseur")||key.equals("WriteMasseur")) {
			String name=array[1];
			String masseurQueryString=array[2];
			String url="http://"+com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(getActivity())+"/MassageNearby/Masseur.aspx"+"?"+masseurQueryString;
			try {
				data = new com.diamondsoftware.android.common.JsonReaderFromRemotelyAcquiredJson(
						new com.diamondsoftware.android.massagenearby.model.ParsesJsonMasseur(name), 
						url
						).parse();
			} catch (Exception e) {
				e=e;
			}
		}
		return data;
	}

	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		String[] array = name.split("\\~", -1);
		String key=array[0];
		if(key.equals("UpdateMasseur")) {
			if(data!=null && data.size()>0) {				
				((MasseurMainActivity)getActivity()).mItemMasseur_me=(ItemMasseur)data.get(0);
				mItemMasseur=(ItemMasseur)data.get(0);
				MasseurMainActivity.mSingleton.mItemMasseur_beingCreated=null;
				// Send out the certified image
				mSelectedImage=MasseurMainActivity.mSingleton.mCertifiedImage;
				heresYourAnswer(true, null);
				// Send out main image
				Uri selectedImage = MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto;
				try {
					InputStream imageStream = getActivity()
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
									+ com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(getActivity())
									+ "/MassageNearby/FileUpload.aspx", parms,
							ProgressDialog.show(getActivity(), "Working ...",
									"Uploading " + selectedImage.getPath(),
									true, false, null), imageStream, this,
							MasseurMainActivity.mSingleton.mItemMasseur_me
									.getmUserId());
					new HttpFileUpload().execute(params);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			getActivity().onBackPressed();
		} else {
			if(key.equals("WriteMasseur")) {
				((MasseurMainActivity)getActivity()).mItemMasseur_me=(ItemMasseur)data.get(0);
				mItemMasseur=(ItemMasseur)data.get(0);
				MasseurMainActivity.mSingleton.mItemMasseur_beingCreated=null;		
				
				if(MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto!=null
						|| MasseurMainActivity.mSingleton.mCertifiedImage!=null) {
					new NewMasseurPhotoUploadManager(getActivity(),mSettingsManager);
				} else {

					mSettingsManager.setMasseurName(mItemMasseur.getmName());
					this.mCallbacks.onNavigationDrawerItemSelected(0);
				}
				
			}
		}
		
	}
	private void writeTheNewMasseur() {
       	new com.diamondsoftware.android.common.AcquireDataRemotelyAsynchronously("WriteMasseur~"+MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getmName()+"~"+ MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getDBQueryStringEncoded(),this, this);

		//TODO: 
		/*
		 * 
		 * 1. write the record
		 * 2. on return, update mMymasseur object
		 * 3. send photos
		 * 3. go Home
		 */
	}
}
