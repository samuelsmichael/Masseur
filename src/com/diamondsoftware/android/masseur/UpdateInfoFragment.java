package com.diamondsoftware.android.masseur;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.diamondsoftware.android.common.*;

import android.app.Activity;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class UpdateInfoFragment extends Fragment_Abstract_NewMasseur implements
	WaitingForDataAcquiredAsynchronously,
	DataGetter {
	private SettingsManager mSettingsManager;
	private ItemMasseur mItemMasseur;
	private EditText mBirthdate;
	private EditText mHeight;
	private EditText mEthnicity;
	private EditText mService1;
	private EditText mService2;
	private EditText mService3;
	private EditText mBio;
	private TextView mHeading;
	private TextView mSubheading;
    private NavigationDrawerCallbacks mCallbacks;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup= (ViewGroup) inflater.inflate(
				R.layout.fragment_updateinfo, container, false);
		mBirthdate=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Birthdate);
		mHeight=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Height);
		mEthnicity=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Ethnicity);
		mService1=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Service1);
		mService2=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Service2);
		mService3=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Service3);
		mBio=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Bio);
		mHeading=(TextView)viewGroup.findViewById(R.id.tvUpdateInfoHeading);
		mSubheading=(TextView)viewGroup.findViewById(R.id.tvNewMasseurSubHeading);
		if(!isNewMasseur()) {
			if(!Utils.isNullDate(mItemMasseur.getBirthdate())) {
				mBirthdate.setText(Utils.mDateFormatYYYYMMDD.format(mItemMasseur.getBirthdate().getTime()));
			}
			mHeight.setText(mItemMasseur.getHeight());
			mEthnicity.setText(mItemMasseur.getEthnicity());
			mBio.setText(mItemMasseur.getBio());
			if(!TextUtils.isEmpty(mItemMasseur.getServices())) {
				for(int c=0;c<mItemMasseur.getServicesAsArrayList().size();c++) {
					switch (c) {
					case 0:
						mService1.setText(mItemMasseur.getServicesAsArrayList().get(c));
						break;
					case 1:
						mService2.setText(mItemMasseur.getServicesAsArrayList().get(c));
						break;
					case 2:
						mService3.setText(mItemMasseur.getServicesAsArrayList().get(c));
						break;
					default:
						break;
					}
				}
			}
		} else {
			mHeading.setText("New Masseur");
			mSubheading.setVisibility(View.VISIBLE);
		}
		Button jdContinue=(Button)viewGroup.findViewById(R.id.btnUpdateInfoContinue);
		jdContinue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ItemMasseur zMasseur=isNewMasseur()?MasseurMainActivity.mSingleton.mItemMasseur_beingCreated:mItemMasseur;
				Date bDate=null;
				try {
					String bDateStr=mBirthdate.getText().toString();
					bDate=Utils.mDateFormatYYYYMMDD.parse(bDateStr);
				} catch (java.text.ParseException e) {

				}
				if(bDate!=null) {
					GregorianCalendar gc=new GregorianCalendar();
					gc.setTime(bDate);
					zMasseur.setBirthdate(gc);
				}
				zMasseur.setHeight(mHeight.getText().toString());
				zMasseur.setEthnicity(mEthnicity.getText().toString());
				zMasseur.setBio(mBio.getText().toString());
				int c=0;
				ArrayList<String>services=new ArrayList<String>();
				if(!TextUtils.isEmpty(mService1.getText().toString())) {
					services.add(mService1.getText().toString());
				}
				if(!TextUtils.isEmpty(mService2.getText().toString())) {
					services.add(mService2.getText().toString());
				}				
				if(!TextUtils.isEmpty(mService3.getText().toString())) {
					services.add(mService3.getText().toString());
				}
				zMasseur.setServicesAsArrayList(services);
				if(!isNewMasseur()) {
					flushMasseurToDB(UpdateInfoFragment.this.mItemMasseur);
					getActivity().onBackPressed();
				} else {
					UpdateInfoFragment.this.mCallbacks.onNavigationDrawerItemSelected(14);
				}
				
			}
		});
		return viewGroup;
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
				mItemMasseur=(ItemMasseur)data.get(0);
			}
			getActivity().onBackPressed();
		}
	}
	private boolean isNewMasseur() {
		return
				MasseurMainActivity.mSingleton!=null &&
				MasseurMainActivity.mSingleton.mItemMasseur_beingCreated!=null;
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

	
	public static UpdateInfoFragment newInstance(SettingsManager setMan, ItemMasseur im) {
		UpdateInfoFragment frag=new UpdateInfoFragment();
		frag.mSettingsManager=setMan;
		frag.mItemMasseur=im;
		return frag;
	}
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        	getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    } 
}
