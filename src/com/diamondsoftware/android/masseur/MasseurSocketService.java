package com.diamondsoftware.android.masseur;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.diamondsoftware.android.common.GlobalStaticValues;
import com.diamondsoftware.android.common.Logger;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;

import android.app.ActivityManager;
import android.app.Service;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;


public class MasseurSocketService extends Service implements 
		com.diamondsoftware.android.common.WaitingForDataAcquiredAsynchronously,
		com.diamondsoftware.android.common.DataGetter
 {
    
	private static String TAG="MasseurSocketService";
	SettingsManager mSettingsManager;
	ServerSocket mServerSocket;
	ItemMasseur mItemMasseurMe;
	String mInetAddress=null;
	private Timer mNetworkPollingTimer=null;
	ConnectivityManager mConnectivityManager=null;
	private boolean mDontReenter=false;
	SocketListenerThread mSocketListenerThread=null;
	Hashtable<Integer,Socket> pendingSockets = new Hashtable<Integer,Socket>();

	public MasseurSocketService() {
	}
    @Override
    public void onCreate() {
        super.onCreate();
        mSettingsManager=new SettingsManager(this);
    	mConnectivityManager=(ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
    }
    
	/* (non-Javadoc)
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		cleanUp();
		super.onDestroy();
	}
	
	private void cleanUpButLeaveNetworkPolling() {
		try {
			if(mServerSocket!=null) {
				mServerSocket.close();
			}
			mServerSocket=null;
		} catch (IOException e) {}
		mInetAddress=null;
		if (mSocketListenerThread!=null) {
			mSocketListenerThread.keepGoing=false;
			mSocketListenerThread=null;
		}
	}
	
	private void cleanUp() {
		stopMyNetworkPollingTimer();
		cleanUpButLeaveNetworkPolling();
	}
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		
		super.onStartCommand(intent, flags, startId);
		if(mSettingsManager.getMasseurName()==null) {
			cleanUp();
			return Service.START_STICKY;
		} else {
			if(mNetworkPollingTimer==null) {
		    	mDontReenter=false;
				startMyNetworkPollingTimer();
				return Service.START_STICKY;
			}
		}

		if(intent!=null) {
			String action=intent.getAction();
			if(action!=null) {
				if(action.equals(ApplicationMasseur.ACTION_CLIENT_IS_NOW_AVAILABLE)) {
					doActivityIsNowAvailableActions();
				} else {
					if(action.equals(ApplicationMasseur.ACTION_STARTING_FROM_ACTIVITY_MASSEUR)) {
						doACTION_STARTING_FROM_MAINACTIVITY();
					} else {
						if(action.equals(ApplicationMasseur.ACTION_STARTING_FROM_BOOTUP_MASSEUR)) {
							doACTION_STARTING_FROM_BOOTUP();
						}
					}
				}
			}
		}
		return Service.START_STICKY;
	}		
    
	protected void doACTION_STARTING_FROM_MAINACTIVITY() {
	}	

	protected void doACTION_STARTING_FROM_BOOTUP() {		
	}
	
	class SocketListenerThread implements Runnable {
		boolean keepGoing=true;
		@Override
		public void run() {			
            while (keepGoing) {
                // LISTEN FOR INCOMING CLIENTS
            	try {
            		Socket clientSocket = mServerSocket.accept();
            		
        	    	if(this.isMyActivityRunning()) {
        	    		MasseurMainActivity.mSingleton.addNewClientSocket(clientSocket, mItemMasseurMe.getmName());

        	    	} else {
        	    		pendingSockets.put(clientSocket.getPort(), clientSocket);
        	    		Intent intent=new Intent(MasseurSocketService.this,MasseurMainActivity.class);
        	    		intent.setAction(ApplicationMasseur.ACTION_NEW_CLIENT_CONNECTION);
        		    	intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        		    	startActivity(intent);
        		    	// TODO
        		    	/*
        		    	 * 1. After the activity starts, it needs to send me a notice so I can do a
        		    	 * MasseurMainActivity.mSingleton.addNewClientSocket(clientSocket, mItemMasseurMe.getmName());
        		    	 */
        	    	}

            	} catch (IOException e) {
            		new Logger(mSettingsManager.getLoggingLevel(),"SocketListenerThread",MasseurSocketService.this).log("Failed accepting client socket request: "+ e.getMessage(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_INFORMATION);
            		cleanUpButLeaveNetworkPolling();
            	}
            }
            closeClientSockets();
		}	
		private void closeClientSockets() {
			// TODO 
		}
		public boolean isMyActivityRunning(){
			return MasseurMainActivity.mSingleton==null;
/*			
			ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
			 List< ActivityManager.RunningTaskInfo > runningTaskInfo = manager.getRunningTasks(200); 
			 for(ActivityManager.RunningTaskInfo info : runningTaskInfo) {
				 String className=info.baseActivity.getClassName();
				 if(className.indexOf("MasseurMainActivity")!=-1 && info.baseActivity.getPackageName().equals(getPackageName())) {
					 return true;
				 }
			 }
			return false;
					*/

		}
	}
	private void doActivityIsNowAvailableActions() {
		Enumeration<Socket> socks=pendingSockets.elements(); 
		while(socks.hasMoreElements()) {
			Socket socket=socks.nextElement();
    		MasseurMainActivity.mSingleton.addNewClientSocket(socket, mItemMasseurMe.getmName());
		}
		pendingSockets.clear();
	}
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public ArrayList<Object> getRemoteData(String keyname) {
		try {
			String[] array = keyname.split("\\~", -1);
			String key=array[0];
			String name=array[1];
			// 10.0.0.253 when wifi on my computer
			String url=null;
			if(key.equals("moi")) {
				url="http://listplus.no-ip.org/MassageNearby/Masseur.aspx"+"?Name="+URLEncoder.encode(name)+"&URL="+URLEncoder.encode(mInetAddress);
			} else {
				if(key.equals("byebye")) {
					url="http://listplus.no-ip.org/MassageNearby/Masseur.aspx"+"?MasseurId="+ mItemMasseurMe.getmMasserId();
				} else {
					return null;
				}
			}
			ArrayList<Object> data = new com.diamondsoftware.android.common.JsonReaderFromRemotelyAcquiredJson(
				new com.diamondsoftware.android.massagenearby.model.ParsesJsonMasseur(name), 
				url
				).parse();
			return data;
		} catch (Exception e) {
			int bkhere1=3;
			int bkhere2=bkhere1;
		} finally {
		}			
	return null;
	}
	@Override
	public void gotMyData(String keyname, ArrayList<Object> data) {
		String[] array = keyname.split("\\~", -1);
		String key=array[0];
		String name=array[1];
		if(data!=null && data.size()>0) {
			if(key.equals("moi")) {
				mItemMasseurMe=(ItemMasseur)data.get(0);
				this.mDontReenter=false;
		       	try {
		       		mServerSocket = new ServerSocket(ApplicationMasseur.SERVERPORT);
		       		mSocketListenerThread=new SocketListenerThread();
			        new Thread(mSocketListenerThread).start();
		       	} catch (IOException e) {
		       		// TODO What if we're not connected to the Internet?  We didn't do any of this until we got connectivity, so this shouldn't happen.
		       	}				
			} else {
				if(key.equals("byebye")) {
					this.cleanUp();
				}
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
            Log.e(TAG, ex.toString());
        }
        return null;
    }

    
	private Timer getMyNetWorkPollingTimer() {
		if (mNetworkPollingTimer == null) {
			mNetworkPollingTimer = new Timer("RestTimer");
		}
		return mNetworkPollingTimer;
	}
	private void stopMyNetworkPollingTimer() {
		if (mNetworkPollingTimer != null) {
			try {
				mNetworkPollingTimer.cancel();
				mNetworkPollingTimer.purge();
			} catch (Exception e) {
			}
			mNetworkPollingTimer = null;
		}
	}	
	private void startMyNetworkPollingTimer() {
		stopMyNetworkPollingTimer();
		getMyNetWorkPollingTimer().schedule(new TimerTask() {
			public void run() {				
				if(!mDontReenter) {
					mDontReenter=true;					
					 NetworkInfo networkInfo =mConnectivityManager.getActiveNetworkInfo ();
					 if(networkInfo!=null && networkInfo.isConnected()) {
						 String localIpAddress=getLocalIpAddress();
						 if(!localIpAddress.equals(mInetAddress)) {
					        // Tell web server that we're here, and here's my inet address
					       	new com.diamondsoftware.android.common.AcquireDataRemotelyAsynchronously("moi~"+ mSettingsManager.getMasseurName(), MasseurSocketService.this, MasseurSocketService.this);
						 }
					 }
				}
			}
		}, ApplicationMasseur.NETWORK_STATUS_POLLING_INTERVAL_IN_MILLISECONDS, ApplicationMasseur.NETWORK_STATUS_POLLING_INTERVAL_IN_MILLISECONDS);
	}

    
}
