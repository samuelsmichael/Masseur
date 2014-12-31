package com.diamondsoftware.android.masseur;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.diamondsoftware.android.common.HttpFileUpload;
import com.diamondsoftware.android.common.HttpFileUploadParameters;
import com.diamondsoftware.android.common.ManagesFileUploads;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;

import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.Toast;

public class PrivatePicturesFragment extends Fragment implements ManagesFileUploads {
	private SettingsManager mSettingsManager;
    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	}
	public static PrivatePicturesFragment newInstance(SettingsManager setMan) {
		PrivatePicturesFragment frag=new PrivatePicturesFragment();
		frag.mSettingsManager=setMan;
		return frag;
	}
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        	getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    } 
    int d;
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ArrayList<View> cellsLeftToRightTopToBottom=new ArrayList<View>();
		ViewGroup viewGroup = (ViewGroup) inflater.inflate(
				R.layout.fragment_private_pictures, container, false);
		GridView gridView=(GridView)viewGroup.findViewById(R.id.gridviewPrivatePhotos);
		
		if(MasseurMainActivity.mSingleton!=null && MasseurMainActivity.mSingleton.mItemMasseur_me!=null) {
			ItemMasseur im=MasseurMainActivity.mSingleton.mItemMasseur_me;
			ArrayList<String> urls = new ArrayList<String>();
			if(!TextUtils.isEmpty(im.getPrivatePicture1URL())) {
				urls.add(im.getPrivatePicture1URL());
				View view=inflater.inflate(R.layout.private_picture_with_image, null, false);
				cellsLeftToRightTopToBottom.add(view);
			}
			if(!TextUtils.isEmpty(im.getPrivatePicture2URL())) {
				urls.add(im.getPrivatePicture2URL());
				cellsLeftToRightTopToBottom.add(inflater.inflate(R.layout.private_picture_with_image,null,false));
			}
			if(!TextUtils.isEmpty(im.getPrivatePicture3URL())) {
				urls.add(im.getPrivatePicture3URL());
				cellsLeftToRightTopToBottom.add(inflater.inflate(R.layout.private_picture_with_image,null,false));
			}
			if(!TextUtils.isEmpty(im.getPrivatePicture4URL())) {
				urls.add(im.getPrivatePicture4URL());
				cellsLeftToRightTopToBottom.add(inflater.inflate(R.layout.private_picture_with_image, null,false));
			}
			for(int c=urls.size();c<4;c++) {
				d=c;
				cellsLeftToRightTopToBottom.add(inflater.inflate(R.layout.private_picture_without_image, null,false));
			}
			gridView.setAdapter(
					new PrivatePhotosAdapter(cellsLeftToRightTopToBottom, 
							urls.size(), im, getActivity(), urls,
							this));
			/*
			if(TextUtils.isEmpty(im.getPrivatePicture2URL())) {
				View view=inflater.inflate(R.layout.private_picture_without_image, row1, true);
				Button btnFromGallery=(Button)view.findViewById(R.id.btnPrivateNoImageFromGallery);
				btnFromGallery.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
						photoPickerIntent.setType("image/*");
						startActivityForResult(
								photoPickerIntent,
								com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_2);					
						}
				});
			} else {
				View view=inflater.inflate(R.layout.private_picture_with_image, row1, true);
				ImageView iv=(ImageView)view.findViewById(R.id.ivPrivatePicture);
				com.diamondsoftware.android.common.ImageLoaderRemote ilm = new com.diamondsoftware.android.common.ImageLoaderRemote(
						getActivity(), true, .80f);
				String url = "http://"
						+ com.diamondsoftware.android.massagenearby.common.CommonMethods
								.getBaseURL(getActivity())
						+ "/MassageNearby/files/"
						+ MasseurMainActivity.mSingleton.mItemMasseur_me
								.getPrivatePicture2URL();

				ilm.displayImage(url, iv);
			}
			if(TextUtils.isEmpty(im.getPrivatePicture1URL())) {
				View view=inflater.inflate(R.layout.private_picture_without_image, row1, true);
				Button btnFromGallery=(Button)view.findViewById(R.id.btnPrivateNoImageFromGallery);
				btnFromGallery.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
						photoPickerIntent.setType("image/*");
						startActivityForResult(
								photoPickerIntent,
								com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_1);					
						}
				});
			} else {
				View view=inflater.inflate(R.layout.private_picture_with_image, row1, true);
				ImageView iv=(ImageView)view.findViewById(R.id.ivPrivatePicture);
				com.diamondsoftware.android.common.ImageLoaderRemote ilm = new com.diamondsoftware.android.common.ImageLoaderRemote(
						getActivity(), true, .80f);
				String url = "http://"
						+ com.diamondsoftware.android.massagenearby.common.CommonMethods
								.getBaseURL(getActivity())
						+ "/MassageNearby/files/"
						+ MasseurMainActivity.mSingleton.mItemMasseur_me
								.getPrivatePicture1URL();

				ilm.displayImage(url, iv);
			}
			
			if(TextUtils.isEmpty(im.getPrivatePicture4URL())) {
				View view=inflater.inflate(R.layout.private_picture_without_image, row2, true);
				Button btnFromGallery=(Button)view.findViewById(R.id.btnPrivateNoImageFromGallery);
				btnFromGallery.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
						photoPickerIntent.setType("image/*");
						startActivityForResult(
								photoPickerIntent,
								com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_4);					
						}
				});
			} else {
				View view=inflater.inflate(R.layout.private_picture_with_image, row2, true);
				ImageView iv=(ImageView)view.findViewById(R.id.ivPrivatePicture);
				com.diamondsoftware.android.common.ImageLoaderRemote ilm = new com.diamondsoftware.android.common.ImageLoaderRemote(
						getActivity(), true, .80f);
				String url = "http://"
						+ com.diamondsoftware.android.massagenearby.common.CommonMethods
								.getBaseURL(getActivity())
						+ "/MassageNearby/files/"
						+ MasseurMainActivity.mSingleton.mItemMasseur_me
								.getPrivatePicture4URL();

				ilm.displayImage(url, iv);
			}
			
			if(TextUtils.isEmpty(im.getPrivatePicture3URL())) {
				View view=inflater.inflate(R.layout.private_picture_without_image, row2, true);
				Button btnFromGallery=(Button)view.findViewById(R.id.btnPrivateNoImageFromGallery);
				btnFromGallery.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
						photoPickerIntent.setType("image/*");
						startActivityForResult(
								photoPickerIntent,
								com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_3);					
						}
				});
			} else {
				View view=inflater.inflate(R.layout.private_picture_with_image, row2, true);
				ImageView iv=(ImageView)view.findViewById(R.id.ivPrivatePicture);
				com.diamondsoftware.android.common.ImageLoaderRemote ilm = new com.diamondsoftware.android.common.ImageLoaderRemote(
						getActivity(), true, .80f);
				String url = "http://"
						+ com.diamondsoftware.android.massagenearby.common.CommonMethods
								.getBaseURL(getActivity())
						+ "/MassageNearby/files/"
						+ MasseurMainActivity.mSingleton.mItemMasseur_me
								.getPrivatePicture3URL();

				ilm.displayImage(url, iv);
			}
*/
		}
		
		return viewGroup;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		ItemMasseur im=MasseurMainActivity.mSingleton.mItemMasseur_me;
		switch (requestCode) {
		case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_1:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				try {
					InputStream imageStream = getActivity()
							.getContentResolver()
							.openInputStream(selectedImage);
					ArrayList<NameValuePair> parms = new ArrayList<NameValuePair>();
					parms.add(new BasicNameValuePair("Transaction",
							"MasseurUploadPrivatePicture1"));
					parms.add(new BasicNameValuePair(
							"UserId",
							String.valueOf(im.getmUserId())));
					parms.add(new BasicNameValuePair(
							"pnbr",
							"1"
							));
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
			break;
		case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_2:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				try {
					InputStream imageStream = getActivity()
							.getContentResolver()
							.openInputStream(selectedImage);
					ArrayList<NameValuePair> parms = new ArrayList<NameValuePair>();
					parms.add(new BasicNameValuePair("Transaction",
							"MasseurUploadPrivatePicture2"));
					parms.add(new BasicNameValuePair(
							"UserId",
							String.valueOf(im.getmUserId())));
					parms.add(new BasicNameValuePair(
							"pnbr",
							"2"
							));
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
			break;
		case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_3:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				try {
					InputStream imageStream = getActivity()
							.getContentResolver()
							.openInputStream(selectedImage);
					ArrayList<NameValuePair> parms = new ArrayList<NameValuePair>();
					parms.add(new BasicNameValuePair("Transaction",
							"MasseurUploadPrivatePicture3"));
					parms.add(new BasicNameValuePair(
							"UserId",
							String.valueOf(im.getmUserId())));
					parms.add(new BasicNameValuePair(
							"pnbr",
							"3"
							));
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
			break;
		case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_4:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = imageReturnedIntent.getData();
				try {
					InputStream imageStream = getActivity()
							.getContentResolver()
							.openInputStream(selectedImage);
					ArrayList<NameValuePair> parms = new ArrayList<NameValuePair>();
					parms.add(new BasicNameValuePair("Transaction",
							"MasseurUploadPrivatePicture4"));
					parms.add(new BasicNameValuePair(
							"UserId",
							String.valueOf(im.getmUserId())));
					parms.add(new BasicNameValuePair(
							"pnbr",
							"4"
							));
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
			break;
			
			
		}
	}

	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
	}
	@Override
	public void alert(String message) {
		((ManagesFileUploads) getActivity()).alert(message);
	}
	@Override
	public void heresTheResponse(String response, ArrayList<NameValuePair> parms) {
		((ManagesFileUploads) getActivity()).heresTheResponse(response,parms);
	}

}
