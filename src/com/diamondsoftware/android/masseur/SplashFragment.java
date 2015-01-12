package com.diamondsoftware.android.masseur;

import com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby;
import com.diamondsoftware.android.massagenearby.common.LoginFragment;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
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

public class SplashFragment extends Fragment {
	private SettingsManager mSettingsManager;
	private CountDownTimer mCountdownTimer;
	private Handler mHandler;
    private NavigationDrawerCallbacks mCallbacks;
    private static int LENGTH_OF_SPLASHPAGE_MILLISECONDS=1000;

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
            if(mSettingsManager.getMasseurName()!=null) {
                Intent intent=new Intent(activity,MasseurSocketService.class);
                intent.setAction(GlobalStaticValuesMassageNearby.ACTION_CLIENT_IS_NOW_AVAILABLE);
                activity.startService(intent);
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

	
}
