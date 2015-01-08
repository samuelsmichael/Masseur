package com.diamondsoftware.android.masseur;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.zip.Inflater;
import com.diamondsoftware.android.common.Utils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import com.diamondsoftware.android.common.ConfirmerClient;
import com.diamondsoftware.android.common.HttpFileUpload;
import com.diamondsoftware.android.common.HttpFileUploadParameters;
import com.diamondsoftware.android.common.ManagesFileUploads;
import com.diamondsoftware.android.common.OnClickListenerWithData;
import com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby;
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
import android.os.Environment;
import android.provider.MediaStore;
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

public class PrivatePicturesFragment extends Fragment implements ManagesFileUploads,
com.diamondsoftware.android.common.WaitingForDataAcquiredAsynchronously,
com.diamondsoftware.android.common.DataGetter, ConfirmerClient {
	private SettingsManager mSettingsManager;
	private Uri imageUri;
	private static final int TAKE_PICTURE = 10102;    

    /**
     * A pointer to the current callbacks instance (the Activity).
     */
    private NavigationDrawerCallbacks mCallbacks;
    
    ViewGroup mViewGroup; 
    GridView mGridView;
    LayoutInflater mInflater;
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
    
    private void showThePictures(final ItemMasseur im) {
    	GlobalStaticValuesMassageNearby.PHOTO_RESULT_IDS.clear();
    	ArrayList<Integer> leftOvers=new ArrayList<Integer>();
		final ArrayList<View> cellsLeftToRightTopToBottom=new ArrayList<View>();
		final ArrayList<String> urls = new ArrayList<String>();
		if(!TextUtils.isEmpty(im.getPrivatePicture1URL())) {
			urls.add(im.getPrivatePicture1URL());
			View view=mInflater.inflate(R.layout.private_picture_with_image, null, false);
			Button remove=(Button)view.findViewById(R.id.btnPrivatePictureRemove);
			remove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<Object> al=new ArrayList<Object>();
					al.add("1");
					al.add(im);
					new Utils.Confirmer("Confirm remove picture", "Are you sure that you wish do remove this picture?",
							getActivity(), PrivatePicturesFragment.this, al,null).show(getActivity().getFragmentManager(), "Remove1");
				}
			});
			GlobalStaticValuesMassageNearby.PHOTO_RESULT_IDS.add(GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_1);
			cellsLeftToRightTopToBottom.add(view);
		} else {
			leftOvers.add(GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_1);
		}
		if(!TextUtils.isEmpty(im.getPrivatePicture2URL())) {
			urls.add(im.getPrivatePicture2URL());
			View view=mInflater.inflate(R.layout.private_picture_with_image, null, false);
			Button remove=(Button)view.findViewById(R.id.btnPrivatePictureRemove);
			remove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<Object> al=new ArrayList<Object>();
					al.add("2");
					al.add(im);
					new Utils.Confirmer("Confirm remove picture", "Are you sure that you wish do remove this picture?",
							getActivity(), PrivatePicturesFragment.this, al,null).show(getActivity().getFragmentManager(), "Remove2");
				}
			});
			cellsLeftToRightTopToBottom.add(view);
			GlobalStaticValuesMassageNearby.PHOTO_RESULT_IDS.add(GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_2);
		} else {
			leftOvers.add(GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_2);
		}
		if(!TextUtils.isEmpty(im.getPrivatePicture3URL())) {
			urls.add(im.getPrivatePicture3URL());
			View view=mInflater.inflate(R.layout.private_picture_with_image, null, false);
			Button remove=(Button)view.findViewById(R.id.btnPrivatePictureRemove);
			remove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<Object> al=new ArrayList<Object>();
					al.add("3");
					al.add(im);
					new Utils.Confirmer("Confirm remove picture", "Are you sure that you wish do remove this picture?",
							getActivity(), PrivatePicturesFragment.this, al,null).show(getActivity().getFragmentManager(), "Remove3");

				}
			});
			cellsLeftToRightTopToBottom.add(view);
			GlobalStaticValuesMassageNearby.PHOTO_RESULT_IDS.add(GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_3);
		} else {
			leftOvers.add(GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_3);
		}
		if(!TextUtils.isEmpty(im.getPrivatePicture4URL())) {
			urls.add(im.getPrivatePicture4URL());
			View view=mInflater.inflate(R.layout.private_picture_with_image, null, false);
			Button remove=(Button)view.findViewById(R.id.btnPrivatePictureRemove);
			remove.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ArrayList<Object> al=new ArrayList<Object>();
					al.add("4");
					al.add(im);
					new Utils.Confirmer("Confirm remove picture", "Are you sure that you wish do remove this picture?",
							getActivity(), PrivatePicturesFragment.this, al,null).show(getActivity().getFragmentManager(), "Remove4");

				}
			});
			cellsLeftToRightTopToBottom.add(view);
			GlobalStaticValuesMassageNearby.PHOTO_RESULT_IDS.add(GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_4);
		} else {
			leftOvers.add(GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_4);
		}
		int i=0;
		for(int c=urls.size();c<4;c++) {
			View zView=mInflater.inflate(R.layout.private_picture_without_image, null,false);
			
			Button btnFromGallery=(Button)zView.findViewById((R.id.btnPrivateNoImageFromGallery));
			Button btnFromPhoto=(Button)zView.findViewById(R.id.btnPrivateNoImageFromCamera);
			
			GlobalStaticValuesMassageNearby.PHOTO_RESULT_IDS.add(leftOvers.get(i++));

			final Integer theId=com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.PHOTO_RESULT_IDS.get(c);
			ArrayList<Object> aa=new ArrayList<Object>();
			aa.add(theId);
			btnFromPhoto.setOnClickListener(new OnClickListenerWithData(aa) {				
				@Override
				public void onClick(View v) {
				    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
				    File photo = new File(Environment.getExternalStorageDirectory(),  "PrivatePic_"+theId+"_"+new Date().getTime()+".jpg");
				    intent.putExtra(MediaStore.EXTRA_OUTPUT,
				            Uri.fromFile(photo));
				    imageUri = Uri.fromFile(photo);
				    startActivityForResult(intent, ((Integer)mData.get(0)).intValue());
				}
			});
			btnFromGallery.setOnClickListener(new OnClickListenerWithData(aa) {
			
				@Override
				public void onClick(View v) {
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					startActivityForResult(
							photoPickerIntent,
							((Integer)mData.get(0)).intValue());					
					}
			});
			
			
			cellsLeftToRightTopToBottom.add(zView);
		}
		getActivity().runOnUiThread(new Runnable() {

			@Override
			public void run() {
				mGridView.setAdapter(
						new PrivatePhotosAdapter(cellsLeftToRightTopToBottom, 
								urls.size(), im, getActivity(), urls,
								PrivatePicturesFragment.this));
				}
			
		});

    	
    }
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		mInflater=inflater;
		mViewGroup= (ViewGroup) inflater.inflate(
				R.layout.fragment_private_pictures, container, false);
		Button jdContinue=(Button)mViewGroup.findViewById(R.id.btnManagePrivatePhotosContinue);
		jdContinue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
				
			}
		});
		mGridView=(GridView)mViewGroup.findViewById(R.id.gridviewPrivatePhotos);
		
		if(MasseurMainActivity.mSingleton!=null && MasseurMainActivity.mSingleton.mItemMasseur_me!=null) {
			ItemMasseur im=MasseurMainActivity.mSingleton.mItemMasseur_me;
			showThePictures(im);
		}
		
		return mViewGroup;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
		Uri resultant;
		if(imageReturnedIntent==null) {
			resultant=imageUri; // when taken from camera
		} else {
			resultant=imageReturnedIntent.getData();;
		}
		ItemMasseur im=MasseurMainActivity.mSingleton.mItemMasseur_me;
		switch (requestCode) {
		case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.ACTIVITY_RESULT_SELECT_PHOTO_PRIVATE_PHOTO_1:
			if (resultCode == Activity.RESULT_OK) {
				Uri selectedImage = resultant;
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
				Uri selectedImage = resultant;
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
				Uri selectedImage = resultant;
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
				Uri selectedImage = resultant;
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
	private boolean isNewMasseur() {
		return
				MasseurMainActivity.mSingleton!=null &&
				MasseurMainActivity.mSingleton.mItemMasseur_beingCreated!=null;
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
		if(MasseurMainActivity.mSingleton!=null && MasseurMainActivity.mSingleton.mItemMasseur_me!=null) {
			ItemMasseur im=MasseurMainActivity.mSingleton.mItemMasseur_me;
			showThePictures(im);
		}
	}

	@Override
	public ArrayList<Object> getRemoteData(String keyname) {
		ArrayList<Object> data=null;
		String[] array = keyname.split("\\~", -1);
		String key=array[0];
		if(key.equals("UpdateMasseur")) {
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


	private void flushMasseurToDB(ItemMasseur masseur) {
       	new com.diamondsoftware.android.common.AcquireDataRemotelyAsynchronously("UpdateMasseur~"+masseur.getmName()+"~"+ masseur.getDBQueryStringEncoded(),this, this);
	}


	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		String[] array = name.split("\\~", -1);
		String key=array[0];
		if(key.equals("UpdateMasseur")) {
			if(data!=null && data.size()>0) {				
				((MasseurMainActivity)getActivity()).mItemMasseur_me=(ItemMasseur)data.get(0);
				showThePictures((ItemMasseur)data.get(0));
			}
		}
	}
	@Override
	public void heresYourAnswer(boolean saidYes, ArrayList<Object> otherData) {
		if(saidYes) {
			ItemMasseur im=(ItemMasseur)otherData.get(1);
			String itemNo=(String)otherData.get(0);
			if(itemNo.equals("1")) {
				im.setPrivatePicture1URL(null);
				flushMasseurToDB(im);
			} else {
				if(itemNo.equals("2")) {
					im.setPrivatePicture2URL(null);
					flushMasseurToDB(im);
				} else {
					if(itemNo.equals("3")) {
						im.setPrivatePicture3URL(null);
						flushMasseurToDB(im);
					} else {
						if(itemNo.equals("4")) {
							im.setPrivatePicture4URL(null);
							flushMasseurToDB(im);
						} 
					}
				}
			}
		}
	}
}
