package com.diamondsoftware.android.masseur;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import com.diamondsoftware.android.common.ConfirmerClient;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.MasseurMainActivity;
import com.diamondsoftware.android.masseur.MasseurSocketService;
import com.diamondsoftware.android.masseur.R;
import com.diamondsoftware.android.masseur.SplashFragment;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class NewMasseurScreen3Fragment extends Fragment_Abstract_NewMasseur  {
	private SettingsManager mSettingsManager;
    private NavigationDrawerCallbacks mCallbacks;
    Button btnLater;
    Button btnCamera;
    Button btnGallery;
    
	public static NewMasseurScreen3Fragment newInstance(SettingsManager setMan) {
		NewMasseurScreen3Fragment frag=new NewMasseurScreen3Fragment();
		frag.mSettingsManager=setMan;
		return frag;
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup= (ViewGroup) inflater.inflate(
				R.layout.fragment_newmasseur_screen3, container, false);
		btnCamera=(Button)viewGroup.findViewById(R.id.btnNewMasseur3Camera);
		btnCamera.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
			    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
			    File photo = new File(Environment.getExternalStorageDirectory(),  "Masseur_XMainPicture_"+new Date().getTime()+".jpg");
			    intent.putExtra(MediaStore.EXTRA_OUTPUT,
			            Uri.fromFile(photo));
			    MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto = Uri.fromFile(photo);
			    startActivityForResult(intent, GlobalStaticValuesMassageNearby.TAKE_FIRST_PUBLIC_PICTURE_CAMERA);
				
				
			
			}
		});
		btnGallery=(Button)viewGroup.findViewById(R.id.btnNewMasseur3Gallery);
		btnGallery.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
				photoPickerIntent.setType("image/*");
				startActivityForResult(
						photoPickerIntent,
						GlobalStaticValuesMassageNearby.PICK_FIRST_PUBLIC_PICTURE_GALLERY);	
				
			}
		});
		btnLater=(Button)viewGroup.findViewById(R.id.btnNewMasseur3Later);
		btnLater.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				getActivity().onBackPressed();
				
			}
		});
		return viewGroup;
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode,
			Intent imageReturnedIntent) {
		super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

		switch (requestCode) {
		case com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.PICK_FIRST_PUBLIC_PICTURE_GALLERY:
			if (resultCode == Activity.RESULT_OK) {
				MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto = imageReturnedIntent.getData();
				this.mCallbacks.onNavigationDrawerItemSelected(16);
				
				//TODO: 1. all these guys in Create Masseur need to have 1. be added, not replaced. 2. implement onGoBack 3. In the onViewCreated, if mMasseur_forNew_masseur(?) has the data, then put it there.
			}
	    case GlobalStaticValuesMassageNearby.TAKE_FIRST_PUBLIC_PICTURE_CAMERA:
	        if (resultCode == Activity.RESULT_OK) {
	        	this.mCallbacks.onNavigationDrawerItemSelected(16);
	        }			
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {	
		super.onViewCreated(view, savedInstanceState);
	}

}
