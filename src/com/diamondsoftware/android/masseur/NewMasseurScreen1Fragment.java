package com.diamondsoftware.android.masseur;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;

import com.diamondsoftware.android.common.Utils;
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
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
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

public class NewMasseurScreen1Fragment extends Fragment_Abstract_NewMasseur implements
com.diamondsoftware.android.common.WaitingForDataAcquiredAsynchronously,
com.diamondsoftware.android.common.DataGetter {
    EditText etUserName;
    EditText etPassword;
    EditText etConfirmPassword;
    EditText etEmail;
    Button btnSubmit;
    ProgressDialog mProgressDialog;
    
	public static NewMasseurScreen1Fragment newInstance(SettingsManager setMan) {
		NewMasseurScreen1Fragment frag=new NewMasseurScreen1Fragment();
		frag.mSettingsManager=setMan;
		return frag;
	}
	
	
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated==null) {
			MasseurMainActivity.mSingleton.mItemMasseur_beingCreated=new ItemMasseur();
			MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setCertificationNumber((int)(Math.random()*100000));
			GregorianCalendar cal = new GregorianCalendar(); 
			cal.add(Calendar.MONTH, 3);
			MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setSubscriptionEndDate(cal);
		}

		if(((MasseurMainActivity)getActivity()).IS_FORCE_NEWMASSEUR_VALUES) {
			if(TextUtils.isEmpty(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getmName())) {
				MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setmName("zz"+new Date().getTime());
			}
			if(TextUtils.isEmpty(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getEmail())) {
				MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setEmail("samuelsmichael222@gmail.com");
			}
			if(TextUtils.isEmpty(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getPassword())) {
				MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setPassword("p");
			}
		
		}
		super.onCreate(savedInstanceState);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onAttach(android.app.Activity)
	 */

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup= (ViewGroup) inflater.inflate(
				R.layout.fragment_newmasseur_screen1, container, false);
		etUserName=(EditText)viewGroup.findViewById(R.id.etNewMasseurUserName);
		if(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getmName()!=null) {
			etUserName.setText(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getmName());
		}
		etUserName.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					etEmail.requestFocus();
					return true;
				}
				return false;
			} 

		}); 
		etPassword=(EditText)viewGroup.findViewById(R.id.etNewMasseurPassword);
		if(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getPassword()!=null) {
			etPassword.setText(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getPassword());
		}
		etPassword.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					etConfirmPassword.requestFocus();
					return true;
				}
				return false;
			} 

		}); 
		etConfirmPassword=(EditText)viewGroup.findViewById(R.id.etNewMasseurConfirmPassword);
		if(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getPassword()!=null) {
			etConfirmPassword.setText(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getPassword());
		}
		etConfirmPassword.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

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
		etEmail=(EditText)viewGroup.findViewById(R.id.etNewMasseurEmail);
		if(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getEmail()!=null) {
			etEmail.setText(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getEmail());
		}
		etEmail.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

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
		if(TextUtils.isEmpty(etUserName.getText().toString())||TextUtils.isEmpty(etPassword.getText().toString())||TextUtils.isEmpty(etConfirmPassword.getText().toString())) {			
			new Utils.Complainer("Invalid Data","Please enter User Name, Password, and Confirm Password",getActivity()).show(getActivity().getFragmentManager(), "unamepwdsblank");
			if(TextUtils.isEmpty(etPassword.getText().toString())) {
				etPassword.requestFocus();
			}
			if(TextUtils.isEmpty(etUserName.getText().toString())) {
				etUserName.requestFocus();
			}
			if(TextUtils.isEmpty(etConfirmPassword.getText().toString())) {
				etConfirmPassword.requestFocus();
			}
		} else {
			if(!etPassword.getText().toString().equals(etConfirmPassword.getText().toString())) {
				new Utils.Complainer("Invalid Data","Password is not the same as Confirm Password",getActivity()).show(getActivity().getFragmentManager(), "unamepwdsblank");
				etPassword.requestFocus();
				etPassword.selectAll();
			} else {
				if(!Utils.isEmailValid(etEmail.getText().toString())) {
					new Utils.Complainer("Invalid Data","Email is not correctly formed",getActivity()).show(getActivity().getFragmentManager(), "bademail");
					etEmail.requestFocus();
					etEmail.selectAll();
				} else {
					mProgressDialog=ProgressDialog.show(getActivity(), "Working ...",
							"Checking to be sure that this name isn't already being used ...",
							true, false, null);
					this.btnSubmit.setEnabled(false);
			       	new com.diamondsoftware.android.common.AcquireDataRemotelyAsynchronously("newmasseur~"+ etUserName.getText().toString(), this, this);
				}
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

	@Override
	public ArrayList<Object> getRemoteData(String keyname) {
		String[] array = keyname.split("\\~", -1);
		String key=array[0];
		String name=array[1];
		// 10.0.0.253 when wifi on my computer
		String url=null;
		if(key.equals("newmasseur")) {
			url="http://"+com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(getActivity())+"/MassageNearby/Masseur.aspx"+"?Name="+URLEncoder.encode(name)+"&IsDoingLogin=true";
		}
		ArrayList<Object> data=null;
		try {
			data = new com.diamondsoftware.android.common.JsonReaderFromRemotelyAcquiredJson(
				new com.diamondsoftware.android.massagenearby.model.ParsesJsonMasseur(name), 
				url
				).parse();
		} catch (Exception e) {}
		return data;
	}


	@Override
	public void gotMyData(String keyname, ArrayList<Object> data) {
		String[] array = keyname.split("\\~", -1);
		String key=array[0];
		String name=array[1];
		boolean wereGood=false;
		ItemMasseur masseur=null;
		if(key.equals("newmasseur")) {

			if(data!=null && data.size()>0) {
				wereGood=false;
			} else {
				wereGood=true;
			}
			if(wereGood) {
                getActivity().runOnUiThread(new Runnable() {

					@Override
					public void run() {
						InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
						imm.hideSoftInputFromWindow(NewMasseurScreen1Fragment.this.etConfirmPassword.getWindowToken(), 0);
						if(mProgressDialog!=null) {
							mProgressDialog.dismiss();
							mProgressDialog=null;
						}
						MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setmName(etUserName.getText().toString());
						MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setPassword(etPassword.getText().toString());
						MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setEmail(etEmail.getText().toString());
						MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setmURL(getLocalIpAddress());

		                NewMasseurScreen1Fragment.this.mCallbacks.onNavigationDrawerItemSelected(2); // go to next screen
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
							NewMasseurScreen1Fragment.this.btnSubmit.setEnabled(true);
							NewMasseurScreen1Fragment.this.etUserName.requestFocus();
							NewMasseurScreen1Fragment.this.etUserName.selectAll();
							new Utils.Complainer("Invalid Credentials", "This name is already taken", getActivity()).show(getActivity().getFragmentManager(), "useralreadyexists");
						}
	                	
	                });
			}
		}
	}

    private String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                    	String hostAddress=inetAddress.getHostAddress().toString();
                    	return hostAddress; 
                    }
                }
            }
        } catch (SocketException ex) {
            
        }
        return null;
    }
}
