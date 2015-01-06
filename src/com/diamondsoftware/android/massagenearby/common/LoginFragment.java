package com.diamondsoftware.android.massagenearby.common;

import com.diamondsoftware.android.masseur.MasseurMainActivity;
import com.diamondsoftware.android.masseur.R;
import com.diamondsoftware.android.masseur.SplashFragment;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class LoginFragment extends Fragment {
	private SettingsManager mSettingsManager;
    private NavigationDrawerCallbacks mCallbacks;
    Button btnCreateClientAccount;
    Button btnCreateMasseurAccount;
    EditText etUserName;
    EditText etPassword;
    CheckBox cbRememberMe;
    
	public static LoginFragment newInstance(SettingsManager setMan) {
		LoginFragment frag=new LoginFragment();
		frag.mSettingsManager=setMan;
		return frag;
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
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup= (ViewGroup) inflater.inflate(
				R.layout.fragment_login, container, false);
		btnCreateClientAccount=(Button)viewGroup.findViewById(R.id.btnLoginCreateANewClientAccount);
		btnCreateMasseurAccount=(Button)viewGroup.findViewById(R.id.btnLoginCreateANewMasseurAccount);
		etUserName=(EditText)viewGroup.findViewById(R.id.etLoginUserName);
		etPassword=(EditText)viewGroup.findViewById(R.id.etLoginPassword);
		cbRememberMe=(CheckBox)viewGroup.findViewById(R.id.cbLoginRememberMe);
		return viewGroup;
	}
	private void finishOnViewCreated() {
		if (MasseurMainActivity.mSingleton != null
				&& MasseurMainActivity.mSingleton.mItemMasseur_me != null) {
			btnCreateClientAccount.setEnabled(false);
			btnCreateMasseurAccount.setEnabled(false);
			etUserName.setText(MasseurMainActivity.mSingleton.mItemMasseur_me.getmName());
			etPassword.requestFocus();
		} else {
			etUserName.requestFocus();
		}
		if(mSettingsManager.getIsRememberMe()) {
			cbRememberMe.setChecked(true);
		}
	}
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View, android.os.Bundle)
	 */
	@Override
	public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {	
		super.onViewCreated(view, savedInstanceState);
		if(mSettingsManager.getMasseurName()!=null) {
			if (MasseurMainActivity.mSingleton != null
					&& MasseurMainActivity.mSingleton.mItemMasseur_me != null) {
				finishOnViewCreated();
			} else {
				new CountDownTimer(10000, 1000) {
					@Override
					public void onTick(long millisUntilFinished) {
						if (MasseurMainActivity.mSingleton != null
								&& MasseurMainActivity.mSingleton.mItemMasseur_me != null) {
							finishOnViewCreated();
							cancel();
						}
					}
					@Override
					public void onFinish() {
						if (MasseurMainActivity.mSingleton != null
								&& MasseurMainActivity.mSingleton.mItemMasseur_me != null) {
							finishOnViewCreated();
						}
					}
				}.start();
			}
		} else {
			finishOnViewCreated();
		}
	}
	
}
