package com.diamondsoftware.android.masseur;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
	public BootCompleteReceiver() {
	}
	
	@Override
	public void onReceive(Context context, Intent intent) {
    	Intent jdItent2=new Intent(context,MasseurSocketService.class)
    		.setAction(ApplicationMasseur.ACTION_STARTING_FROM_BOOTUP_MASSEUR);
		context.startService(jdItent2);
	}	
}
