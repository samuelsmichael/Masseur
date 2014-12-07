package com.diamondsoftware.android.masseur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.diamondsoftware.android.common.DataGetter;
import com.diamondsoftware.android.common.GlobalStaticValues;
import com.diamondsoftware.android.massagenearby.model.ItemClient;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;

import android.app.Activity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MasseurMainActivity extends FragmentActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {
	
	public static MasseurMainActivity mSingleton=null;
	private SettingsManager mSettingsManager;
	private ItemMasseur mItemMasseur_me;


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
        
        mSettingsManager=new SettingsManager(this);
        
        // QUESTION: Does onCreate get called when 
        setContentView(R.layout.activity_masseur_main);
        mSingleton=this;
        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
        
        if(mSettingsManager.getMasseurName()==null) {
		    android.app.FragmentTransaction ft = getFragmentManager().beginTransaction();
		    android.app.Fragment prev = getFragmentManager().findFragmentByTag("login");
		    if (prev != null) {
		        ft.remove(prev);
		    }
		    Login selectLogin=new Login();
			selectLogin.show(ft,"login");
        } else {
            Intent intent=new Intent(MasseurMainActivity.this,MasseurSocketService.class);
            intent.setAction(ApplicationMasseur.ACTION_CLIENT_IS_NOW_AVAILABLE);
            startService(intent);
        }
        
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
		                	   MasseurMainActivity.mSingleton.mSettingsManager.setMasseurName(mEditText.getText().toString());		    
		                       
		                       Intent intent=new Intent(MasseurMainActivity.this,MasseurSocketService.class);
		                       intent.setAction(ApplicationMasseur.ACTION_CLIENT_IS_NOW_AVAILABLE);
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
    		mItemMasseur_me.setmUserId(userId);
    		mItemMasseur_me.setmName(name);
    	}
    	ItemClient ic=new ItemClient();
    	ClientThreadReceive ctr=new ClientThreadReceive(socket,ic,mItemMasseur_me.getmUserId());
    	ic.setmClientThreadReceive(ctr);

        ((ApplicationMasseur)getApplication()).mClients.add(ic);
        // Ask client to tell you his name
    	// We need to put this into a thread (ClientSocket?)
    	try {
	        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket
	                .getOutputStream())), true);
	        out.println(name+"~"+GlobalStaticValues.COMMAND_WHATS_YOUR_NAME_AND_ID+"~");
            Thread cThread = new Thread(ctr);
            cThread.start();
    	} catch (IOException e) {}
    }
    
    public class ClientThreadReceive implements Runnable {
    	Socket mSocket;
    	public boolean mClientIsAlive;
    	public ItemClient mItemClient;
    	private int mMasseurClientId; 
    	public ClientThreadReceive(Socket socket, ItemClient ic, int masseurClientId) {
    		mSocket=socket;
    		mClientIsAlive=true;
    		mItemClient=ic;
    		mMasseurClientId=masseurClientId;
    	}

		@Override
		public void run() {
			while(mClientIsAlive) {
				try {
		            BufferedReader in = new BufferedReader(new InputStreamReader(mSocket.getInputStream()));
		            String line = null;
		            while ((line = in.readLine()) != null) {
                    	String[] sa=line.split("\\~", -1);
                    	String name=sa[0];
                    	String command=sa[1];
                    	String msg=sa[2];
                    	if (command.equals(GlobalStaticValues.COMMAND_MY_NAME_AND_ID_IS)) {
                    		mItemClient.setmName(name);
                    		mItemClient.setmClientId(Integer.valueOf(msg));
                    	} else {
                    		if(command.equals(GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG)) {
                    			
	                			ContentValues values = new ContentValues(2);
	                			values.put(com.diamondsoftware.android.massagenearby.model.DataProvider.COL_MSG, msg);
	                			values.put(com.diamondsoftware.android.massagenearby.model.DataProvider.COL_FROM, String.valueOf(mItemClient.getmClientId()));
	                			values.put(com.diamondsoftware.android.massagenearby.model.DataProvider.COL_TO, String.valueOf(mMasseurClientId));
	                			MasseurMainActivity.this.getContentResolver().insert(com.diamondsoftware.android.massagenearby.model.DataProvider.CONTENT_URI_MESSAGES, values);

                    		}
                    	}
		            }

				} catch (IOException e) {
					break;
				}
			}
			try {
				mSocket.close();
			} catch (IOException e) {}
		}
    	
    }
    

    /* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		mSingleton=null;
		ArrayList<ItemClient> aL=((ApplicationMasseur)getApplication()).mClients;
		for(ItemClient ic: aL) {
			ic.close();
		}
		((ApplicationMasseur)getApplication()).mClients.clear();
		super.onDestroy();
	}

	@Override
    public void onNavigationDrawerItemSelected(int position) {
		int clientId=((ApplicationMasseur)getApplication()).mClients.get(position-1).getmClientId();
		String clientName=((ApplicationMasseur)getApplication()).mClients.get(position-1).getmName();
		Socket socket=((ApplicationMasseur)getApplication()).mClients.get(position-1).getmClientThreadReceive().mSocket;
        // update the main content by replacing fragments
        android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(clientId,clientName,mSettingsManager,socket))
                .commit();
    }

    public void onSectionAttached(int number) {
    	ArrayList<ItemClient> allMs=((ApplicationMasseur)getApplication()).mClients;
    	if(allMs!=null && allMs.size()>0) {
    		mTitle = ((ItemClient)allMs.get(number-1)).getmName();
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
    	int mClientId;
    	String mClientName;
    	private EditText msgEdit;
    	private Button btnSend;
    	private Socket mSocket;


        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int clientId, String clientName, SettingsManager sm, Socket socket) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, clientId);
            fragment.setArguments(args);
        	fragment.mClientId=clientId;
        	fragment.mClientName=clientName;
        	fragment.mSocket=socket;
        	sm.setChatId(String.valueOf(fragment.mClientId));
        	
            
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            android.support.v4.app.FragmentManager fragmentManager = getChildFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container2, com.diamondsoftware.android.massagenearby.common.MessagesFragment.newInstance(mClientId))
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
	    				
	           			String txt=mClientName+"~"+GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG+"~"+msg;
	           			
	           			try {
	                    PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket
	                            .getOutputStream())), true);
	                    out.println(txt);     	 	
	           			} catch (IOException e) {
	           				return;
	           			}
	           			
            			ContentValues values = new ContentValues(2);
            			values.put(com.diamondsoftware.android.massagenearby.model.DataProvider.COL_MSG, msg);
            			values.put(com.diamondsoftware.android.massagenearby.model.DataProvider.COL_TO, String.valueOf(mClientId));
            			getActivity().getContentResolver().insert(com.diamondsoftware.android.massagenearby.model.DataProvider.CONTENT_URI_MESSAGES, values);
            			
	           			
	    				
	    				msgEdit.setText(null);
	    				
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

}
