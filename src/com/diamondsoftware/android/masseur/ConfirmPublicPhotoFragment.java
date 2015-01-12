package com.diamondsoftware.android.masseur;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.diamondsoftware.android.massagenearby.common.SettingsManager;

public class ConfirmPublicPhotoFragment extends Fragment_Abstract_NewMasseur {	
	Button btnTryAgain;
	Button btnContinue;
	public static ConfirmPublicPhotoFragment newInstance(SettingsManager setMan) {
		ConfirmPublicPhotoFragment frag=new ConfirmPublicPhotoFragment();
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
				R.layout.fragment_confirm_public_photo, container, false);
		ImageView image=(ImageView)viewGroup.findViewById(R.id.ivMasseurConfirmNewMasseur);
		image.setImageURI(MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto);
		btnTryAgain=(Button)viewGroup.findViewById(R.id.btnConfirmPhoto_TryAgain);
		btnTryAgain.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MasseurMainActivity.mSingleton.mSelectedImageNewMasseurPublicPhoto=null;
				getActivity().onBackPressed();
				
			}
		});
		btnContinue=(Button)viewGroup.findViewById(R.id.btnConfirmPhoto_Continue);

		btnContinue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCallbacks.onNavigationDrawerItemSelected(17);
			}
		});
		
		return viewGroup;
	}

}
