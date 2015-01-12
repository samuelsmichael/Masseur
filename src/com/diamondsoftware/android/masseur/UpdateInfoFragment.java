package com.diamondsoftware.android.masseur;

import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.common.Utils.Complainer;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;
import com.diamondsoftware.android.common.*;

import android.app.Activity;
import android.content.Context;
import android.net.ParseException;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

public class UpdateInfoFragment extends Fragment_Abstract_NewMasseur implements
	WaitingForDataAcquiredAsynchronously,
	DataGetter {
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
	private android.widget.ScrollView mScrollView;

	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(savedInstanceState!=null) {
			GregorianCalendar birthDate=new GregorianCalendar();
			birthDate.setTimeInMillis(savedInstanceState.getLong("Birthdate"));
			MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setBirthdate(birthDate);
			MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setHeight(savedInstanceState.getString("Height"));
			MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setEthnicity(savedInstanceState.getString("Ethnicity"));
			MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setServices(savedInstanceState.getString("Services"));
			MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setBio(savedInstanceState.getString("Bio"));
		} else {
			
			if(((MasseurMainActivity)getActivity()).IS_FORCE_NEWMASSEUR_VALUES && this.isNewMasseur()) {
				if(Utils.isNullDate(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getBirthdate())) {
					String bDateStr="04.19.1990";
					Date date=null;
					try {
						date=Utils.mDateFormatMMDDYYYY.parse(bDateStr);
					} catch (Exception e) {}
					GregorianCalendar gc=new GregorianCalendar();
					gc.setTime(date);
					MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setBirthdate(gc);
				}
				if(TextUtils.isEmpty(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getHeight())) {
					MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setHeight("6'2\"");
				}
				if(TextUtils.isEmpty(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getEthnicity())) {
					MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setEthnicity("Afro-American");
				}
				if(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getServicesAsArrayList()==null) {
					MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setServices("Deep massage^Oil rub^Foot massage");
				}
				if(TextUtils.isEmpty(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getBio())) {
					MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.setBio("I have been a massage therapist for over 8 years.  Your first visit is free!");
				}
			}
		}
		super.onCreate(savedInstanceState);
	}
    
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		ViewGroup viewGroup= (ViewGroup) inflater.inflate(
				R.layout.fragment_updateinfo, container, false);
		mScrollView=(android.widget.ScrollView)viewGroup.findViewById(R.id.svUpdateInfo);
		mBirthdate=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Birthdate);
		mBirthdate.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					mHeight.requestFocus();
					return true;
				}
				return false;
			} 

		}); 
		mHeight=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Height);
		mHeight.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					mEthnicity.requestFocus();
					return true;
				}
				return false;
			} 

		}); 
		mEthnicity=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Ethnicity);
		mEthnicity.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					mService1.requestFocus();
					return true;
				}
				return false;
			} 

		}); 
		mService1=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Service1);
		mService1.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					mService2.requestFocus();
					
					return true;
				}
				return false;
			} 

		}); 
		mService2=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Service2);
		mService2.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					mService3.requestFocus();
					return true;
				}
				return false;
			} 

		}); 
		mService3=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Service3);
		mService3.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_NEXT) {
					mBio.requestFocus();
					return true;
				}
				return false;
			} 

		}); 
		if(savedInstanceState!=null) {
			for(int c=0;c<MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getServicesAsArrayList().size();c++) {
				if(c==0) {
					mService1.setText(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getServicesAsArrayList().get(c));
				}
				if(c==1) {
					mService2.setText(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getServicesAsArrayList().get(c));
				}
				if(c==2) {
					mService2.setText(MasseurMainActivity.mSingleton.mItemMasseur_beingCreated.getServicesAsArrayList().get(c));
				}
			}
		}
		mBio=(EditText)viewGroup.findViewById(R.id.etUpdateInfo_Bio);
		mBio.setOnEditorActionListener(new EditText.OnEditorActionListener(){  

			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if(actionId==EditorInfo.IME_ACTION_GO) {
					doGo();
					return true;
				}
				return false;
			} 

		}); 
		mHeading=(TextView)viewGroup.findViewById(R.id.tvUpdateInfoHeading);
		mSubheading=(TextView)viewGroup.findViewById(R.id.tvNewMasseurSubHeading);
		ItemMasseur theItemMasseur=
				isNewMasseur()?MasseurMainActivity.mSingleton.mItemMasseur_beingCreated:mItemMasseur;
		if(!Utils.isNullDate(theItemMasseur.getBirthdate())) {
			mBirthdate.setText(Utils.mDateFormatMMDDYYYY.format(theItemMasseur.getBirthdate().getTime()));
		}
		mHeight.setText(theItemMasseur.getHeight());
		mEthnicity.setText(theItemMasseur.getEthnicity());
		mBio.setText(theItemMasseur.getBio());
		if(!TextUtils.isEmpty(theItemMasseur.getServices())) {
			for(int c=0;c<theItemMasseur.getServicesAsArrayList().size();c++) {
				switch (c) {
				case 0:
					mService1.setText(theItemMasseur.getServicesAsArrayList().get(c));
					break;
				case 1:
					mService2.setText(theItemMasseur.getServicesAsArrayList().get(c));
					break;
				case 2:
					mService3.setText(theItemMasseur.getServicesAsArrayList().get(c));
					break;
				default:
					break;
				}
			}
		}

		if(!isNewMasseur()) {
			
		} else {
			
			mHeading.setText("New Masseur");
			mSubheading.setVisibility(View.VISIBLE);
			
		}
		Button jdContinue=(Button)viewGroup.findViewById(R.id.btnUpdateInfoContinue);
		jdContinue.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				doGo();
			}
		});
		return viewGroup;
	}
	

	private void flushMasseurToDB(ItemMasseur masseur) {
       	new com.diamondsoftware.android.common.AcquireDataRemotelyAsynchronously("UpdateMasseur~"+masseur.getmName()+"~"+ masseur.getDBQueryStringEncoded(),this, this);
	}
	private void doGo() {
		InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
		      Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(mBio.getWindowToken(), 0);
		ItemMasseur zMasseur=isNewMasseur()?MasseurMainActivity.mSingleton.mItemMasseur_beingCreated:mItemMasseur;
		Date bDate=null;
		try {
			String bDateStr=mBirthdate.getText().toString();
			bDate=Utils.mDateFormatMMDDYYYY.parse(bDateStr);
		} catch (java.text.ParseException e) {

		}
		if(bDate!=null) {
			GregorianCalendar gc=new GregorianCalendar();
			gc.setTime(bDate);
			zMasseur.setBirthdate(gc);
		} else {
			Complainer complainer=new Complainer("Invalid date format", "Please input date in this format: mm.dd.yyyy",getActivity());
			complainer.show(getActivity().getFragmentManager(), "complaindate");
			return;
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
		} else {
			UpdateInfoFragment.this.mCallbacks.onNavigationDrawerItemSelected(14);
		}		
	}
	@Override
	public void gotMyData(String name, ArrayList<Object> data) {
		String[] array = name.split("\\~", -1);
		String key=array[0];
		if(key.equals("UpdateMasseur")) {
			if(data!=null && data.size()>0) {				
				MasseurMainActivity.mSingleton.mItemMasseur_me=(ItemMasseur)data.get(0);
				mItemMasseur=(ItemMasseur)data.get(0);
			}
			this.mCallbacks.onNavigationDrawerItemSelected(0);
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
