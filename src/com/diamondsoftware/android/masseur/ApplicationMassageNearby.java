package com.diamondsoftware.android.masseur;

import java.net.Socket;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Random;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import org.acra.ReportingInteractionMode;

import com.diamondsoftware.android.massagenearby.common.SocketCommunicationsManager;
import com.diamondsoftware.android.massagenearby.model.ItemClient;


import android.app.Application;

@ReportsCrashes(
		formKey="dGVacG0ydVHnaNHjRjVTUTEtb3FPWGc6MQ",
        mode = ReportingInteractionMode.DIALOG,
        resToastText = R.string.crash_toast_text, // optional, displayed as soon as the crash occurs, before collecting data which can take a few seconds
        resDialogText = R.string.crash_dialog_text,
        resDialogIcon = android.R.drawable.ic_dialog_info, //optional. default is a warning sign
        resDialogTitle = R.string.crash_dialog_title, // optional. default is your application name
        resDialogCommentPrompt = R.string.crash_dialog_comment_prompt, // optional. when defined, adds a user text field input with this text resource as a label
        resDialogOkToast = R.string.crash_dialog_ok_toast2,  // optional. displays a Toast message when the user accepts to send a report.
        mailTo = "diamondsoftware222@gmail.com"	
)

public class ApplicationMassageNearby extends Application {
	
	/* (non-Javadoc)
	 * @see android.app.Application#onCreate()
	 */
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mSingletonApp=this;

        // The following line triggers the initialization of ACRA
       /////////////////////////////////////////////////////////////////////////////// ACRA.init(this);
	}
	//public static final int SERVERPORT = 8080;
	public static final int NETWORK_STATUS_POLLING_INTERVAL_IN_MILLISECONDS=5000;
	
    public static final int MAX_ATTEMPTS = 5;
    public static final int BACKOFF_MILLI_SECONDS = 2000;
    public static final Random random = new Random();

	public ItemClient mItemClientMe;
	public boolean isSettingUpMasseur;

	public static ApplicationMassageNearby mSingletonApp=null;
	public ArrayList<SocketCommunicationsManager> mClients=new ArrayList<SocketCommunicationsManager>();
	public Hashtable<Integer,Socket> mPendingSockets=new Hashtable<Integer,Socket>();
	String[] getAllClientsAsStringArray() {
		if(mClients!=null) {
			String[] allClientNames=new String[mClients.size()];
			int c=0;
			for(SocketCommunicationsManager ic: mClients) {
				allClientNames[c++]=ic.getmItemUserClient().getmName();
			}		
			return allClientNames;
		} else {
			return null;
		}
	}
	
	SocketCommunicationsManager getItemClientWhoseUserIdEquals(int userId) {
		SocketCommunicationsManager retValue=null;
		for(SocketCommunicationsManager ic: mClients) {
			if(ic.getmItemUserClient().getmUserId()==userId) {
				retValue=ic;
				break;
			}
		}
		return retValue;
	}
	
	void removeUserFromList(SocketCommunicationsManager scm) {
		ArrayList<SocketCommunicationsManager> newClients=new ArrayList<SocketCommunicationsManager>();
		for(SocketCommunicationsManager smcexisting: mClients) {
			if(smcexisting.getmItemUserClient().getmUserId()!=scm.getmItemUserClient().getmUserId()) {
				newClients.add(smcexisting);
			}
			mClients=newClients;
		}
	}
	void updateListWithNewSCM(SocketCommunicationsManager scm) {
		removeUserFromList(scm);
		mClients.add(scm);
	}
}

