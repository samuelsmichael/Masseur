package com.diamondsoftware.android.masseur;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.Socket;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.diamondsoftware.android.common.ManagesFileUploads;
import com.diamondsoftware.android.common.GlobalStaticValues;
import com.diamondsoftware.android.common.HttpFileUpload;
import com.diamondsoftware.android.common.HttpFileUploadParameters;
import com.diamondsoftware.android.common.Logger;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.common.ChatPageManager;
import com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby;
import com.diamondsoftware.android.massagenearby.common.LoginFragment;
import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.massagenearby.common.SocketCommunicationsManager;
import com.diamondsoftware.android.massagenearby.model.ItemClient;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.massagenearby.model.ItemUser;
import com.diamondsoftware.android.massagenearby.model.ParsesJsonMasseur;


public class MasseurMainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks,
        com.diamondsoftware.android.massagenearby.common.MessagesFragment.OnFragmentInteractionListener,
        ChatPageManager, ManagesFileUploads {
	
	public static MasseurMainActivity mSingleton=null;
	private SettingsManager mSettingsManager;
	public ItemMasseur mItemMasseur_me;
	public ItemMasseur mItemMasseur_beingCreated=null;
	public ItemClient mItemClient_beingCreated=null;
	private String profileClientId;
	private Handler mHandler;
	public static final String ACTION_STARTING_FROM_BOOTUP_MASSEUR="StartingFromBootupMasseur";
	public static final String ACTION_STARTING_FROM_ACTIVITY_MASSEUR="StartingFromActivityMasseur";
	public static final String ACTION_NEW_CLIENT_CONNECTION="actionnewclientconnection";
	public static boolean IS_ALREADY_IN_LOGIN=false;
    Uri mSelectedImageNewMasseurPublicPhoto;
    Uri mCertifiedImage;
    public static final boolean IS_FORCE_NEWMASSEUR_VALUES=false;


    public static final String TAG="MasseurMain";


    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSingleton=this;
        mHandler=new Handler();
        
        mSettingsManager=new SettingsManager(this);
        
        // QUESTION: Does onCreate get called when 
        setContentView(R.layout.activity_masseur_main);
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        
        
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, HomePageFragment.newInstance(mSettingsManager))
                .commit();

/*        
        if(mSettingsManager.getMasseurName()==null) {
		    android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
		    android.app.Fragment prev = getFragmentManager().findFragmentByTag("login");
		    if (prev != null) {
		        ft.remove(prev);
		    }
		    Login selectLogin=new Login();
			selectLogin.show(ft,"login");
        } else {
    		new Logger(mSettingsManager.getLoggingLevel(),"MasseurMainActivity",this).log("Telling socket I'm here", com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_INFORMATION);

            Intent intent=new Intent(this,MasseurSocketService.class);
            intent.setAction(GlobalStaticValuesMassageNearby.ACTION_CLIENT_IS_NOW_AVAILABLE);
            startService(intent);
        }
  */      
    }
    
    public class Login extends DialogFragment {
    	View mView;
    	EditText mEditText;
    	

		@Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
	        // Use the Builder class for convenient dialog construction
		        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		        // Get the layout inflater
		        LayoutInflater inflater = getActivity().getLayoutInflater();
		        mView = inflater.inflate(R.layout.login, null);
		        mEditText=(EditText)mView.findViewById(R.id.edittextloginid);
		        mEditText.setText(MasseurMainActivity.this.mSettingsManager.getMasseurName());
		        mEditText.selectAll();
		        builder.setView(mView);
	
		        // Inflate and set the layout for the dialog
		        // Pass null as the parent view because its going in the dialog layout
//		        builder.setView(inflater.inflate(R.layout.find_home, null));
		        builder.setTitle("Login")
		        	   .setMessage("Key in your name")
		               .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   final EditText mEditText=(EditText)Login.this.getDialog().findViewById(R.id.edittextloginid);
		                	   //TODO can't allow blanks here
		                	   MasseurMainActivity.mSingleton.mSettingsManager.setMasseurName(mEditText.getText().toString().trim());		    
		                       
		                       Intent intent=new Intent(MasseurMainActivity.this,MasseurSocketService.class);
		                       intent.setAction(GlobalStaticValuesMassageNearby.ACTION_CLIENT_IS_NOW_AVAILABLE);
		                       startService(intent);

		                  }
		               })
		               .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
		                   public void onClick(DialogInterface dialog, int id) {
		                	   finish();
		                   }
		               });
		        // Create the AlertDialog object and return it
		        return builder.create();
        }		
    }

    
    
    
    public void addNewClientSocket(Socket socket,String name, int userId) {
    	// TODO
    	/*
    	 * This is a new socket. It doesn't have a ClientMasseur associated with it yet. 
    	 * In other words, we don't know who the Client is who accessed the socket.  The service will have already sent a "Hi. I'm Masseur xxx" record, and
    	 * just needs an acknowledgement (with a name)
    	 */

    	if(this.mItemMasseur_me==null) {
    		mItemMasseur_me=new ItemMasseur();
    	}
		mItemMasseur_me.setmUserId(userId);
		mItemMasseur_me.setmName(name);
    	mSettingsManager.setChatId(String.valueOf(userId));
    	ItemClient ic=new ItemClient();
    	SocketCommunicationsManager ctr=new SocketCommunicationsManager(socket, this, ic, mItemMasseur_me,this);

    }
    
    

    /* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		mSingleton=null;
		ArrayList<SocketCommunicationsManager> aL=((ApplicationMassageNearby)getApplication()).mClients;
		for(SocketCommunicationsManager ic: aL) {
			ic.close();
		}
		((ApplicationMassageNearby)getApplication()).mClients.clear();
		super.onDestroy();
	}

	@Override
    public void onNavigationDrawerItemSelected(int position) {
		if(position>=100) {
			ArrayList<SocketCommunicationsManager> aL=((ApplicationMassageNearby)getApplication()).mClients;
			if(aL.size()>0) {
				SocketCommunicationsManager smc=aL.get(position-100);
				profileClientId=String.valueOf(smc.getmItemUserClient().getmUserId());
		        // update the main content by replacing fragments
		        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		        fragmentManager.beginTransaction()
		                .replace(R.id.container, PlaceholderFragment.newInstance(smc,mSettingsManager))
		                .commit();
			}
		} else {
			if(position==0) {
		        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
//		        for(int c=0;c<fragmentManager.getBackStackEntryCount();c++) {
	//	        	fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
		//        }
		        mNavigationDrawerFragment.setDrawerIndicatorEnabled(true);
		        fragmentManager.beginTransaction()
		                .replace(R.id.container, HomePageFragment.newInstance(mSettingsManager))
		                .addToBackStack(null)
		                .commit();
			} else {
				if(position==1) {
					//disable the toggle menu and show up carat
					mNavigationDrawerFragment.setDrawerIndicatorEnabled(false);
			        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
			        fragmentManager.beginTransaction()
			                .replace(R.id.container, PrivatePicturesFragment.newInstance(mSettingsManager))
			                .addToBackStack(null)
			                .commit();
				} else {
					if(position==2) {
						//disable the toggle menu and show up carat
						mNavigationDrawerFragment.setDrawerIndicatorEnabled(false);
				        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
				        fragmentManager.beginTransaction()
				                .replace(R.id.container, UpdateInfoFragment.newInstance(mSettingsManager,mItemMasseur_me))
				                .addToBackStack(null)
				                .commit();
					} else {
						if(position==3) {
							//disable the toggle menu and show up carat
							mNavigationDrawerFragment.setDrawerIndicatorEnabled(false);
					        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
					        fragmentManager.beginTransaction()
					                .replace(R.id.container, CertifyFragment.newInstance(mSettingsManager,mItemMasseur_me))
					                .addToBackStack(null)
					                .commit();
						} else {
							if(position==10) {
								//disable the toggle menu and show up carat
								mNavigationDrawerFragment.setDrawerIndicatorEnabled(false);
						        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
						        fragmentManager.beginTransaction()
						        		
						                .replace(R.id.container, LoginFragment.newInstance(mSettingsManager))
						                .commit();
							} else {
								if(position==11) {
									//disable the toggle menu and show up carat
									mNavigationDrawerFragment.setDrawerIndicatorEnabled(false);
							        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
							        fragmentManager.beginTransaction()
							                .replace(R.id.container, NewMasseurScreen1Fragment.newInstance(mSettingsManager))
							        		.addToBackStack(null)
							                .commit();
								} else {
									if(position==14) {
										//disable the toggle menu and show up carat
										mNavigationDrawerFragment.setDrawerIndicatorEnabled(false);
								        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
								        fragmentManager.beginTransaction()
								                .replace(R.id.container, NewMasseurScreen3Fragment.newInstance(mSettingsManager))
								        		.addToBackStack(null)
								                .commit();
									} else {

										if(position==16) { // how's the uploaded photo look?
											//disable the toggle menu and show up carat
											mNavigationDrawerFragment.setDrawerIndicatorEnabled(false);
									        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
									        fragmentManager.beginTransaction()
									                .replace(R.id.container, ConfirmPublicPhotoFragment.newInstance(mSettingsManager))
									        		.addToBackStack(null)
									                .commit();
										} else {
										
											if(position==17) { // Photo looks fine ... onto ConfirmationPhoto
												//disable the toggle menu and show up carat
												mNavigationDrawerFragment.setDrawerIndicatorEnabled(false);
										        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
										        fragmentManager.beginTransaction()
										                .replace(R.id.container, CertifyFragment.newInstance(mSettingsManager, 
										                		MasseurMainActivity.mSingleton.mItemMasseur_beingCreated!=null?MasseurMainActivity.mSingleton.mItemMasseur_beingCreated: MasseurMainActivity.mSingleton.mItemMasseur_me))
										                .commit();
											} else {
											
											}								
										}								
									
									}								
								}								
							}
						}
					}
				}
			}
		}
    }

	@Override
	public void onBackPressed() {
	    super.onBackPressed();
	    // turn on the Navigation Drawer image; 
	    // this is called in the LowerLevelFragments
	    mNavigationDrawerFragment.setDrawerIndicatorEnabled(true);
	}
	
    public void onSectionAttached(int number) {
    	ArrayList<SocketCommunicationsManager> allMs=((ApplicationMassageNearby)getApplication()).mClients;
    	if(allMs!=null && allMs.size()>0) {
    		ItemUser ic= ((ApplicationMassageNearby)getApplication()).getItemClientWhoseUserIdEquals(number).getmItemUserClient();
    		if(ic!=null) {
    			mTitle = ic.getmName();
    		} else {
    			mTitle="";
    		}
    	} else {
    		mTitle="";
    	}
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.masseur_main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends android.support.v4.app.Fragment {
    	SocketCommunicationsManager mSCM;
    	private EditText msgEdit;
    	private Button btnSend;


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(SocketCommunicationsManager scm,SettingsManager sm) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, scm.getmItemUserClient().getmUserId());
            fragment.setArguments(args);
            fragment.mSCM=scm;
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container2, com.diamondsoftware.android.massagenearby.common.MessagesFragment.newInstance(mSCM))
                    .commit();

            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            msgEdit = (EditText)rootView.findViewById(R.id.msg_edit);
            btnSend = (Button) rootView.findViewById(R.id.send_btn);
            btnSend.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
	    			String msg = msgEdit.getText().toString();
	    			if (!TextUtils.isEmpty(msg)) {
	           			btnSend.setEnabled(false);
	    				ItemMasseur im=((MasseurMainActivity)getActivity()).mItemMasseur_me;
	           			try {
	           				mSCM.doSend(GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG,msg);
	           			} catch (Exception e) {
		    				btnSend.setEnabled(true);
	           				return;
	           			}
	           			
            			ContentValues values = new ContentValues(2);
            			values.put(com.diamondsoftware.android.masseur.DataProvider.COL_MSG, msg);
            			values.put(com.diamondsoftware.android.masseur.DataProvider.COL_TO, String.valueOf(mSCM.getmItemUserClient().getmUserId()));
            			getActivity().getContentResolver().insert(com.diamondsoftware.android.masseur.DataProvider.CONTENT_URI_MESSAGES, values);
            			
	           			
	    				
	    				msgEdit.setText(null);
	    				btnSend.setEnabled(true);
	    			}
				}
			});
            return rootView;
        }
    	
        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MasseurMainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) { 
	    super.onActivityResult(requestCode, resultCode, imageReturnedIntent); 
	
	}

    public void alert(String message) {
        AlertDialog.Builder bld = new AlertDialog.Builder(this);
        bld.setMessage(message);
        bld.setNeutralButton("OK", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			}
		});
        bld.create().show();
    }


	@Override
	public String getProfileChatId() {

		return profileClientId;
	}

	@Override
	// Note: this does not occur on the UI thread
	public void lostCommunicationsWith(final SocketCommunicationsManager itemUser) {
		mHandler.post(new Runnable() {

			@Override
			public void run() {
		        AlertDialog.Builder bld = new AlertDialog.Builder(MasseurMainActivity.this);
		        bld.setTitle("Communications has been lost with "+itemUser.getmItemUserClient().getmName());
		        bld.setMessage("Do you want to close the chat window for this user?");
		        bld.setPositiveButton("Yes", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						((ApplicationMassageNearby)getApplication()).removeUserFromList(itemUser);
						if(((ApplicationMassageNearby)getApplication()).mClients.size()==0) {
							finish();
						} else {
							android.support.v4.app.Fragment frag = getSupportFragmentManager().findFragmentById(R.id.container);
							getSupportFragmentManager().beginTransaction()
					        	.remove(frag)
					            .commit();
						}
					}
				});
		        bld.setNegativeButton("No", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		        bld.create().show();
				
			}
			
		});
		
	}

	@Override
	// Note: this does not occur on the UI thread
	public void weveGotANewChat(SocketCommunicationsManager ctr) {
		if(((ApplicationMassageNearby)getApplication()).getItemClientWhoseUserIdEquals(ctr.getmItemUserClient().getmUserId())!=null) {
			((ApplicationMassageNearby)getApplication()).updateListWithNewSCM(ctr);
		} else {
			((ApplicationMassageNearby)getApplication()).mClients.add(ctr);
		}
		mHandler.post(new Runnable() {

			@Override
			public void run() {
				mNavigationDrawerFragment.openDrawer();
				
			}
			
		});

		
	}



	@Override
	public void heresTheResponse(String response, ArrayList<NameValuePair> parms) {
		try {
			ArrayList<Object> massers=new ParsesJsonMasseur("").parsePublic(response);
			if(massers.size()>0) {
				mItemMasseur_me=(ItemMasseur)massers.get(0);
			}
		} catch (Exception e) {}
		
	}
}
