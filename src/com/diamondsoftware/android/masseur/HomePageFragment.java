package com.diamondsoftware.android.masseur;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.diamondsoftware.android.common.ManagesFileUploads;
import com.diamondsoftware.android.common.HttpFileUpload;
import com.diamondsoftware.android.common.HttpFileUploadParameters;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;

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
import android.widget.TextView;

public class HomePageFragment extends Fragment implements ManagesFileUploads {
	SettingsManager mSettingsManager;
	ImageView mImageMasseur;
	TextView mTvAge;
	TextView tvHeight;
	TextView tvEthnicity;
	TextView tvServices;
	TextView tvBio;
	TextView tvCertified;
	TextView tvGetCertified;
	TextView tvRenewSubscription;
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;

	public static HomePageFragment newInstance(SettingsManager sm) {
		HomePageFragment fragment = new HomePageFragment();
		fragment.mSettingsManager = sm;
		return fragment;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onActivityCreated(savedInstanceState);
	}

	private void displayMasseurImage(boolean clearCache) {
		if (MasseurMainActivity.mSingleton != null
				&& MasseurMainActivity.mSingleton.mItemMasseur_me != null
				&& MasseurMainActivity.mSingleton.mItemMasseur_me
						.getMainPictureURL() != null) {
			String url = "http://"
					+ com.diamondsoftware.android.massagenearby.common.CommonMethods
							.getBaseURL(getActivity())
					+ "/MassageNearby/files/"
					+ MasseurMainActivity.mSingleton.mItemMasseur_me
							.getMainPictureURL();
			com.diamondsoftware.android.common.ImageLoaderRemote ilm = new com.diamondsoftware.android.common.ImageLoaderRemote(
					getActivity(), true, .80f);
			if (clearCache && 1==2) { // in other words ... I don't have to clear cache anymore
				ilm.clearCache();
			}
			ilm.displayImage(url, mImageMasseur);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }		
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup = (ViewGroup) inflater.inflate(
				R.layout.fragment_homepage, container, false);
		Button upload = (Button) viewGroup.findViewById(R.id.btnUploadNewPhoto);
		Button privatePictures=(Button) viewGroup.findViewById(R.id.btnManagePrivatePhotos);
		Button updateInfo=(Button) viewGroup.findViewById(R.id.btnUpdateInfo);
		tvHeight = (TextView) viewGroup.findViewById(R.id.tvHomeHeight);
		tvEthnicity = (TextView) viewGroup.findViewById(R.id.tvHomeEthnicity);
		tvServices = (TextView) viewGroup.findViewById(R.id.tvHomeServices);
		tvBio = (TextView) viewGroup.findViewById(R.id.tvHomeBio);
		tvCertified=(TextView)viewGroup.findViewById(R.id.tvHomeCertified);
		tvGetCertified=(TextView)viewGroup.findViewById(R.id.tvHomeGetCertified);
		tvRenewSubscription=(TextView)viewGroup.findViewById(R.id.tvHomeSubscriptionExpireDate);
		mImageMasseur = (ImageView) viewGroup.findViewById(R.id.imageMasseur);
		mTvAge = (TextView) viewGroup.findViewById(R.id.tvHomeAge);
		updateInfo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        if (mCallbacks != null) {
		            mCallbacks.onNavigationDrawerItemSelected(2);
		        }
			}
		});
		
		privatePictures.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
		        if (mCallbacks != null) {
		            mCallbacks.onNavigationDrawerItemSelected(1);
		        }
				
			}
		});
		upload.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(
						photoPickerIntent,
						com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO);
			}
		});
		return viewGroup;
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
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
				// Bitmap yourSelectedImage =
				// BitmapFactory.decodeStream(imageStream);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewCreated(view, savedInstanceState);
		displayMasseurImage(false);
		GregorianCalendar bdate;
		if (MasseurMainActivity.mSingleton != null
				&& MasseurMainActivity.mSingleton.mItemMasseur_me != null) {
			ItemMasseur im = MasseurMainActivity.mSingleton.mItemMasseur_me;
			tvHeight.setText(im.getHeight());
			if(!Utils.isNullDate(im.getSubscriptionEndDate())) {
				tvRenewSubscription.setText(Utils.mLocaleDateFormat.format(im.getSubscriptionEndDate().getTime()));
			}
			bdate = im.getBirthdate();
			Date now = new Date();
			long millisecondsAlive = now.getTime() - bdate.getTimeInMillis();
			int age = (int) (millisecondsAlive / 3.15569e10);
			mTvAge.setText(String.valueOf(age));
			tvEthnicity.setText(im.getEthnicity());
			StringBuilder sb = new StringBuilder();
			String newLine = "";
			if (im.getServices() != null
					&& im.getServices().trim().length() > 0) {
				String[] services = im.getServices().split("\\^", -1);
				for (String svc : services) {
					if (svc.trim().length() > 0) {
						sb.append(newLine + svc);
						newLine = "\n";
					}
				}
			}

			tvServices.setText(sb.toString());
			tvBio.setText(im.getBio());
			this.tvCertified.setText(
					im.getCertifiedPictureURL()==null || im.getCertifiedPictureURL().trim().length()==0?"No":"Yes"
			);
			if(im.getCertifiedPictureURL()==null || im.getCertifiedPictureURL().trim().length()==0) {
				tvGetCertified.setVisibility(View.VISIBLE);
			} else {
				tvGetCertified.setVisibility(View.INVISIBLE);
			}
		}
	}

	@Override
	public void alert(String message) {
		((ManagesFileUploads) getActivity()).alert(message);

	}

	@Override
	public void heresTheResponse(String response, ArrayList<NameValuePair> parms) {
		((ManagesFileUploads) getActivity()).heresTheResponse(response, parms);
		displayMasseurImage(true);

	}

}
