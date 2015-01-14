package com.diamondsoftware.android.massagenearby.common;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.diamondsoftware.android.client.MasseurListActivity;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.model.ItemClient;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.ApplicationMassageNearby;
import com.diamondsoftware.android.masseur.MasseurMainActivity;
import com.diamondsoftware.android.masseur.MasseurSocketService;
import com.diamondsoftware.android.masseur.R;
import com.diamondsoftware.android.masseur.SplashFragment;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

public class LoginFragment extends Fragment implements
com.diamondsoftware.android.common.WaitingForDataAcquiredAsynchronously,
com.diamondsoftware.android.common.DataGetter {
	private SettingsManager mSettingsManager;
    private NavigationDrawerCallbacks mCallbacks;
    Button btnCreateClientAccount;
    Button btnCreateMasseurAccount;
    EditText etUserName;
    EditText etPassword;
    CheckBox cbRememberMe;
    Button btnSubmit;
    ProgressDialog mProgressDialog;
    
	public static LoginFragment newInstance(SettingsManager setMan) {
		LoginFragment frag=new LoginFragment();
		frag.mSettingsManager=setMan;
		return frag;
	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
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
		btnCreateMasseurAccount.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				mCallbacks.onNavigationDrawerItemSelected(11);
				
			}
		});
		etUserName=(EditText)viewGroup.findViewById(R.id.etLoginUserName);
		etUserName.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					etPassword.requestFocus();
					return true;
				}
				return false;
			} 

		}); 
		etPassword=(EditText)viewGroup.findViewById(R.id.etLoginPassword);
		etPassword.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_GO) {
					doSubmitAction();
					return true;
				}
				return false;
			} 

		}); 

		cbRememberMe=(CheckBox)viewGroup.findViewById(R.id.cbLoginRememberMe);
		btnSubmit=(Button)viewGroup.findViewById(R.id.btnLoginContinue);
		btnSubmit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doSubmitAction();
			}
		});
		return viewGroup;
	}
	private void doSubmitAction() {
		if(TextUtils.isEmpty(etUserName.getText().toString())||TextUtils.isEmpty(etPassword.getText().toString())) {
			
			new Utils.Complainer("Invalid Data","Please enter both User Name and Password",getActivity()).show(getActivity().getFragmentManager(), "unamepwdblank");
			if(TextUtils.isEmpty(etPassword.getText().toString())) {
				etPassword.requestFocus();
			}
			if(TextUtils.isEmpty(etUserName.getText().toString())) {
				etUserName.requestFocus();
			}
			return;
		}
		mProgressDialog=ProgressDialog.show(getActivity(), "Working ...",
				"Verifying credentials...",
				true, false, null);
		this.btnSubmit.setEnabled(false);
       	new com.diamondsoftware.android.common.AcquireDataRemotelyAsynchronously("login~"+ etUserName.getText().toString(), this, this);

	}
	private void finishOnViewCreated() {
		try {
			InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
			imm.showSoftInput(etUserName, InputMethodManager.SHOW_IMPLICIT);
			imm.showSoftInput(etPassword, InputMethodManager.SHOW_IMPLICIT);
			if (mSettingsManager.getMasseurName() != null) {
		//		btnCreateClientAccount.setEnabled(false);
			//	btnCreateMasseurAccount.setEnabled(false);
				etUserName.setText(mSettingsManager.getMasseurName());
				etPassword.requestFocus();
			} else {
				if(mSettingsManager.getCurrentClientUserName()!=null) {
					//		btnCreateClientAccount.setEnabled(false);
					//	btnCreateMasseurAccount.setEnabled(false);
					etUserName.setText(mSettingsManager.getCurrentClientUserName());
					etPassword.requestFocus();					 
				} else {
					etPassword.requestFocus();
					etUserName.requestFocus();
				}
			}
			if(mSettingsManager.getIsRememberMe()) {
				cbRememberMe.setChecked(true);
			}
		} catch (Exception e) {}
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
/*
 * 		                	   MasseurMainActivity.mSingleton.mSettingsManager.setMasseurName(mEditText.getText().toString().trim());		    
		                       
		                       Intent intent=new Intent(MasseurMainActivity.this,MasseurSocketService.class);
		                       intent.setAction(GlobalStaticValuesMassageNearby.ACTION_CLIENT_IS_NOW_AVAILABLE);
		                       startService(intent);

 */


	@Override
	public ArrayList<Object> getRemoteData(String keyname) {
		String[] array = keyname.split("\\~", -1);
		String key=array[0];
		String name=array[1];
		// 10.0.0.253 when wifi on my computer
		String url=null;
		if(key.equals("login")) {
			String ipAddress=com.diamondsoftware.android.common.CommonMethods.getLocalIpAddress();
			url="http://"+com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(getActivity())+"/MassageNearby/Masseur.aspx"+"?Name="+URLEncoder.encode(name)+"&IsDoingLogin=true&IPAddress="+URLEncoder.encode(ipAddress);
		}
		ArrayList<Object> data=null;
		try {
			data = new com.diamondsoftware.android.common.JsonReaderFromRemotelyAcquiredJson(
				new com.diamondsoftware.android.massagenearby.model.ParsesJsonMasseur(name), 
				url
				).parse();
		} catch (Exception e) {
			try {
				data = new com.diamondsoftware.android.common.JsonReaderFromRemotelyAcquiredJson(
					new com.diamondsoftware.android.massagenearby.model.ParsesJsonClient(name), 
					url
					).parse();
			} catch (Exception e2) {
				
			}
			
		}
		return data;
	}


	@Override
	public void gotMyData(String keyname, ArrayList<Object> data) {
		String[] array = keyname.split("\\~", -1);
		String key=array[0];
		String name=array[1];
		boolean wereGood=false;
		ItemMasseur masseur=null;
		ItemClient client=null;
		boolean isMasseur=false;
		if(key.equals("login")) {
			mSettingsManager.setCurrentClientUserName(null);
			mSettingsManager.setMasseurName(null);
			if (MasseurMainActivity.mSingleton != null) {
				MasseurMainActivity.mSingleton.mItemMasseur_me=null;
			}
			if(ApplicationMassageNearby.mSingletonApp!=null) {
				ApplicationMassageNearby.mSingletonApp.mItemClientMe=null;
			}			

			if(data!=null && data.size()>0) {
				try {
					masseur=(ItemMasseur)data.get(0);
					if(masseur.getPassword().equals(this.etPassword.getText().toString())) {
						wereGood=true;
						isMasseur=true;
					}
				} catch (Exception e) {
					client=(ItemClient)data.get(0);
					if(client.getPassword().equals(this.etPassword.getText().toString())) {
						wereGood=true;
						isMasseur=false;
					}
				}
			}
			if(wereGood) {
				if(isMasseur) {
					mSettingsManager.setMasseurName(masseur.getmName());	
					if (MasseurMainActivity.mSingleton != null) {
						MasseurMainActivity.mSingleton.mItemMasseur_me=masseur;
					}
	                Intent intent=new Intent(getActivity(),MasseurSocketService.class);
	                intent.setAction(GlobalStaticValuesMassageNearby.ACTION_CLIENT_IS_NOW_AVAILABLE);
	                getActivity().startService(intent);
				} else {
					mSettingsManager.setCurrentClientUserName(client.getmName());
					if(ApplicationMassageNearby.mSingletonApp!=null) {
						ApplicationMassageNearby.mSingletonApp.mItemClientMe=client;
					}
				}
				final boolean finalIsMasseur=isMasseur;
                getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(LoginFragment.this.etPassword.getWindowToken(), 0);
						if(mProgressDialog!=null) {
							mProgressDialog.dismiss();
							mProgressDialog=null;
						}
						mSettingsManager.setIsRememberMe(cbRememberMe.isChecked());
	        	     //   android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
	        	       // fragmentManager.beginTransaction()
	        	        //	.remove(LoginFragment.this)
	        	        	//.commit();
	        	        	
						//LoginFragment.this.getActivity().onBackPressed();
						if(finalIsMasseur) {
							LoginFragment.this.mCallbacks.onNavigationDrawerItemSelected(0);
						} else {
		    				Intent intent = new Intent(getActivity(),MasseurListActivity.class);
		    				getActivity().startActivity(intent);
		    				getActivity().finish();
						}
					}
                	
                });
			} else {
	               getActivity().runOnUiThread(new Runnable() {

						@Override
						public void run() {
							if(mProgressDialog!=null) {
								mProgressDialog.dismiss();
								mProgressDialog=null;
							}
							LoginFragment.this.btnSubmit.setEnabled(true);
							LoginFragment.this.etUserName.requestFocus();
							LoginFragment.this.etUserName.selectAll();
							new Utils.Complainer("Invalid Credentials", "Please try again", getActivity()).show(getActivity().getFragmentManager(), "failedlogin");
						}
	                	
	                });
			}
		}
	}
}
