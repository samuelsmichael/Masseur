package com.diamondsoftware.android.masseur;

import java.net.Socket;
import java.util.Hashtable;

import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

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

public class ApplicationMasseur extends Application {
	public static final String ACTION_STARTING_FROM_BOOTUP_MASSEUR="StartingFromBootupMasseur";
	public static final String ACTION_STARTING_FROM_ACTIVITY_MASSEUR="StartingFromActivityMasseur";
	public static final String ACTION_NEW_CLIENT_CONNECTION="actionnewclientconnection";
	public static final String ACTION_CLIENT_IS_NOW_AVAILABLE = "actionisnowavailable";
	
	public static final String KEY_IS_ENABLED_MASSEUR="MasseurIsEnabled";
	public static final String KEY_MASSEUR_NAME="masseur_name";
	
	public static final int SERVERPORT = 8080;
	public static final int NETWORK_STATUS_POLLING_INTERVAL_IN_MILLISECONDS=5000;
	
	public Hashtable<String,ItemClient> mClients=new Hashtable<String,ItemClient>();
	public Hashtable<Integer,Socket> mPendingSockets=new Hashtable<Integer,Socket>();
}

