package com.diamondsoftware.android.masseur;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.diamondsoftware.android.common.ManagesFileUploads;
import com.diamondsoftware.android.common.HttpFileUpload;
import com.diamondsoftware.android.common.HttpFileUploadParameters;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

public class HomePageFragment extends Fragment
		implements ManagesFileUploads{
	SettingsManager mSettingsManager;
	ImageView mImageMasseur;

	public static HomePageFragment newInstance(SettingsManager sm) {
		HomePageFragment fragment=new HomePageFragment();
		fragment.mSettingsManager=sm;
		return fragment;
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void displayMasseurImage(boolean clearCache) {
		if(MasseurMainActivity.mSingleton!=null && MasseurMainActivity.mSingleton.mItemMasseur_me!=null && MasseurMainActivity.mSingleton.mItemMasseur_me.getMainPictureURL()!=null) {
			String url="http://"+com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(getActivity())+"/MassageNearby/files/"+MasseurMainActivity.mSingleton.mItemMasseur_me.getMainPictureURL();
			com.diamondsoftware.android.common.ImageLoaderRemote ilm=new com.diamondsoftware.android.common.ImageLoaderRemote(getActivity(),true,.80f);
			if(clearCache) {
				ilm.clearCache();
			}
			ilm.displayImage(url,mImageMasseur);
		}

	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup=(ViewGroup) inflater.inflate(R.layout.fragment_homepage, container,false);
		Button upload=(Button)viewGroup.findViewById(R.id.btnUploadNewPhoto);
		mImageMasseur=(ImageView)viewGroup.findViewById(R.id.imageMasseur);
		upload.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(photoPickerIntent, com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO); 
			}
		});
		return viewGroup;
	}
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
	
	    switch(requestCode) { 
	    case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO:
	        if(resultCode == Activity.RESULT_OK){  
	            Uri selectedImage = imageReturnedIntent.getData();
	            try {
					InputStream imageStream = getActivity().getContentResolver().openInputStream(selectedImage);
					ArrayList<NameValuePair> parms=new ArrayList<NameValuePair>();
					parms.add(new BasicNameValuePair("Transaction", "MasseurUploadMainPicture"));
					parms.add(new BasicNameValuePair("UserId", String.valueOf(MasseurMainActivity.mSingleton.mItemMasseur_me.getmUserId())));
					
					HttpFileUploadParameters params=new HttpFileUploadParameters(
								"http://"+com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(getActivity())+"/MassageNearby/FileUpload.aspx"
								,parms 
								,ProgressDialog.show(getActivity(),"Working ...","Uploading "+selectedImage.getPath(),true,false,null), imageStream, this
								,MasseurMainActivity.mSingleton.mItemMasseur_me.getmUserId());
					new HttpFileUpload().execute(params);
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
//	            Bitmap yourSelectedImage = BitmapFactory.decodeStream(imageStream);
	        }
	    }
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		displayMasseurImage(false);
	}
	@Override
	public void alert(String message) {
		((ManagesFileUploads)getActivity()).alert(message);
		
	}
	@Override
	public void heresTheResponse(String response) {
		((ManagesFileUploads)getActivity()).heresTheResponse(response);
		displayMasseurImage(true);

	}

}
