package com.diamondsoftware.android.masseur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.diamondsoftware.android.common.ConfirmerClient;
import com.diamondsoftware.android.common.HttpFileUpload;
import com.diamondsoftware.android.common.HttpFileUploadParameters;
import com.diamondsoftware.android.common.ManagesFileUploads;
import com.diamondsoftware.android.common.Utils;
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

public class CertifyFragment extends Fragment implements ManagesFileUploads, ConfirmerClient {
	Button btnContinue;
	Button btnGallery;
	Button btnPhoto;
	TextView tvCode;
	private SettingsManager mSettingsManager;
	private ItemMasseur mItemMasseur;
	Uri mSelectedImage;
	private static final int TAKE_PICTURE = 10101;    
	private Uri imageUri;


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
				    File photo = new File(Environment.getExternalStorageDirectory(),  "CertifyPic_"+new Date().getTime()+".jpg");
				    intent.putExtra(MediaStore.EXTRA_OUTPUT,
				            Uri.fromFile(photo));
				    imageUri = Uri.fromFile(photo);
				    startActivityForResult(intent, TAKE_PICTURE);
			}
		});
		btnContinue=(Button)viewGroup.findViewById(R.id.btnCertifyContinue);
		btnContinue.setOnClickListener(new View.OnClickListener() {			
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();			
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
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        	getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
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

			}
	    case TAKE_PICTURE:
	        if (resultCode == Activity.RESULT_OK) {
	        	mSelectedImage=imageUri;
				ArrayList<Object> al=new ArrayList<Object>();
				al.add("1");
				al.add(mItemMasseur);
				new Utils.Confirmer2("Confirm Certification Picture", "Do you wish to use this picture?",
						getActivity(), this, al,mSelectedImage,"Yes","No").show(getActivity().getFragmentManager(), "Confirm2_1");

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
				getActivity().onBackPressed();
			}
		} catch (Exception e) {}
	}
	@Override
	public void heresYourAnswer(boolean saidYes, ArrayList<Object> otherData) {
		if(saidYes) {
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
		} else {
			
		}
	}

}
