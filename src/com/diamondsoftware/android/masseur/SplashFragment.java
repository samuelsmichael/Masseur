package com.diamondsoftware.android.masseur;

import java.net.URLEncoder;
import java.util.ArrayList;

import com.diamondsoftware.android.client.MasseurListActivity;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby;
import com.diamondsoftware.android.massagenearby.common.LoginFragment;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.model.ItemClient;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

public class SplashFragment extends Fragment implements 
		com.diamondsoftware.android.common.WaitingForDataAcquiredAsynchronously,
		com.diamondsoftware.android.common.DataGetter {
	private SettingsManager mSettingsManager;
	private CountDownTimer mCountdownTimer;
	private Handler mHandler;
    private NavigationDrawerCallbacks mCallbacks;
    private static int LENGTH_OF_SPLASHPAGE_MILLISECONDS=2400;

	public static SplashFragment newInstance(SettingsManager setMan) {
		SplashFragment frag=new SplashFragment();
		frag.mSettingsManager=setMan;
		frag.mHandler=new Handler();
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
            if(mSettingsManager.getIsRememberMe()) {
	            if(mSettingsManager.getMasseurName()!=null) {
	    			if (MasseurMainActivity.mSingleton != null) {
	    				MasseurMainActivity.mSingleton.mItemMasseur_me=null;
	    			}

	               	new com.diamondsoftware.android.common.AcquireDataRemotelyAsynchronously("GetGuy~"+ mSettingsManager.getMasseurName(), this, this);
	
	            	/*
	                Intent intent=new Intent(activity,MasseurSocketService.class);
	                intent.setAction(GlobalStaticValuesMassageNearby.ACTION_CLIENT_IS_NOW_AVAILABLE);
	                activity.startService(intent);
	                */
	            } else {
		            if(mSettingsManager.getCurrentClientUserName() != null) {
		    			if(ApplicationMassageNearby.mSingletonApp!=null) {
		    				ApplicationMassageNearby.mSingletonApp.mItemClientMe=null;
		    			}			

		               	new com.diamondsoftware.android.common.AcquireDataRemotelyAsynchronously("GetGuy~"+ mSettingsManager.getCurrentClientUserName(), this, this);
		            }
	            }
            }
    		mHandler.post(new Runnable() {

    			@Override
    			public void run() {
    				SplashFragment.this.mCountdownTimer=new CountDownTimer(LENGTH_OF_SPLASHPAGE_MILLISECONDS, 1000) {
    					@Override
    					public void onTick(long millisUntilFinished) {
    					}
    					@Override
    					public void onFinish() {
    						mHandler.post(new Runnable() {

    							@Override
    							public void run() {
    								mCountdownTimer=null;
    					            if(mSettingsManager.getMasseurName()!=null) {
    					            	if(mSettingsManager.getIsRememberMe()) {
    					            		mCallbacks.onNavigationDrawerItemSelected(0);
    					            	} else {
    					        	        android.support.v4.app.FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
    					        	        fragmentManager.beginTransaction()
    					        	                .replace(R.id.container, LoginFragment.newInstance(mSettingsManager))
    					        	                .commit();
//    					            		mCallbacks.onNavigationDrawerItemSelected(10); // login
    					            	}
    					            } else {
    					            	if(mSettingsManager.getCurrentClientUserName()!=null) {
        					            	if(mSettingsManager.getIsRememberMe()) {
	    					    				Intent intent = new Intent(getActivity(),MasseurListActivity.class);
	    					    				getActivity().startActivity(intent);
	    					    				getActivity().finish();
        					            	}
    					            	}
    					            	mCallbacks.onNavigationDrawerItemSelected(10); // login 
    					            }
    							}
    							
    						});
    					}
    				};
    				mCountdownTimer.start();
    			}
    			
    		});

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
				R.layout.fragment_splash, container, false);
		return viewGroup;
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
		if(key.equals("GetGuy")) {

			if(data!=null && data.size()>0) {
				try {
					masseur=(ItemMasseur)data.get(0);
					wereGood=true;
					isMasseur=true;
				} catch (Exception e) {
					client=(ItemClient)data.get(0);
					wereGood=true;
					isMasseur=false;
				}
			}
			if(wereGood) {
				if(isMasseur) {
					mSettingsManager.setMasseurName(masseur.getmName());	
					if (MasseurMainActivity.mSingleton != null) {
						MasseurMainActivity.mSingleton.mItemMasseur_me=masseur;
					}
					/*
	                Intent intent=new Intent(getActivity(),MasseurSocketService.class);
	                intent.setAction(GlobalStaticValuesMassageNearby.ACTION_CLIENT_IS_NOW_AVAILABLE);
	                getActivity().startService(intent);
	                */
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
						if(finalIsMasseur) {
							SplashFragment.this.mCallbacks.onNavigationDrawerItemSelected(0);
						} else {
		    				Intent intent = new Intent(getActivity(),MasseurListActivity.class);
		    				getActivity().startActivity(intent);
		    				getActivity().finish();
						}
					}
                	
                });
			}
		}
	}

	@Override
	public ArrayList<Object> getRemoteData(String keyname) {
		String[] array = keyname.split("\\~", -1);
		String key=array[0];
		String name=array[1];
		// 10.0.0.253 when wifi on my computer
		String url=null;
		if(key.equals("GetGuy")) {
			String ipAddress=com.diamondsoftware.android.common.CommonMethods.getLocalIpAddress(getActivity());
			url="http://"+com.diamondsoftware.android.massagenearby.common.CommonMethods.getBaseURL(getActivity())+"/MassageNearby/Masseur.aspx"+"?Name="+URLEncoder.encode(name)+"&IsDoingLogin=true&IPAddress="+URLEncoder.encode(ipAddress);
		} else {
			
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

	
}
