package com.diamondsoftware.android.massagenearby.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.ContentValues;
import android.net.NetworkInfo;
import android.util.Log;

import com.diamondsoftware.android.common.GlobalStaticValues;
import com.diamondsoftware.android.massagenearby.model.ItemClient;
import com.diamondsoftware.android.massagenearby.model.ItemUser;
import com.diamondsoftware.android.masseur.ApplicationMassageNearby;
import com.diamondsoftware.android.masseur.MasseurMainActivity;
import com.diamondsoftware.android.masseur.MasseurSocketService;

public class SocketCommunicationsManager  {

	private Socket mSocket;
	private ChatPageManager mChatPageManager;
	private ItemUser mItemUserClient;
	private ItemUser mItemUserME;
	Timer mTimer;
	static int NBR_OF_SECONDS_ALLOWED_FOR_RESPONSE=5;
	private int mPendingACKs;
	private Object mSyncObject=new Object();
	private int mCountdownAwaitingACKs;
	private static final String TAG="TAG_SocketCommunicationsManager";
	
	
	public SocketCommunicationsManager(Socket socket, ChatPageManager chatPageManager, ItemUser itemUserClient, ItemUser itemUserMe) {
		mSocket=socket;
		mChatPageManager=chatPageManager;
		mItemUserME=itemUserMe;
        mItemUserClient=itemUserClient;
        mPendingACKs=0;
        mCountdownAwaitingACKs=0;
        new Thread(new ClientThreadReceive()).start();
	}
	
	public void close() {
		stopMyTimer();
		try {
			mSocket.close();
		} catch (Exception e) {}
		mSocket=null;
		mChatPageManager.lostCommunicationsWith(this);
	}

    private class ClientThreadReceive implements Runnable {
    	public boolean mClientIsAlive;
    	public ClientThreadReceive() {
    		mClientIsAlive=true;
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
                    	String id=sa[1];
                    	String command=sa[2];
                		if(command.equals(GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG)) {
                        	String msg=sa[3];
                        	String remName=mItemUserClient.getmName();
                    		mItemUserClient.setmName(name);
                    		mItemUserClient.setmUserId(Integer.valueOf(id));
                			ContentValues values = new ContentValues(2);
                			values.put(com.diamondsoftware.android.masseur.DataProvider.COL_MSG, msg);
                			values.put(com.diamondsoftware.android.masseur.DataProvider.COL_FROM, String.valueOf(mItemUserClient.getmUserId()));
                			values.put(com.diamondsoftware.android.masseur.DataProvider.COL_TO, String.valueOf(mItemUserME.getmUserId()));
                			mChatPageManager.getContentResolver().insert(com.diamondsoftware.android.masseur.DataProvider.CONTENT_URI_MESSAGES, values);
                			if(remName==null) { // we just now got added
                				mChatPageManager.weveGotANewChat(SocketCommunicationsManager.this);
                			}
                			doSend(GlobalStaticValues.COMMAND_ACK,null);
            			} else {
            				if(command.equals(GlobalStaticValues.COMMAND_ACK)) {
            					synchronized(mSyncObject) {
	            					mPendingACKs--;
	            					if(mPendingACKs<=0) {
	            						mPendingACKs=0;
	            						mCountdownAwaitingACKs=0;
	            						stopMyTimer();
	            					}
            					}
            				}
            			}
		            }

				} catch (Exception e) {
					mClientIsAlive=false;
				}
			}
			try {
				if(!mSocket.isClosed()) {
					close();
				}
			} catch (Exception e) {}
		}
    	
    }    
    
    /*
     * Make msg null, if it's not pertinent
     */
	public void doSend(String transactionType,String msg) throws Exception {
		String txt=getmItemUserME().getmName()+"~"+getmItemUserME().getmUserId()+"~"+transactionType+"~"+(msg==null?"":msg);   	
		if(transactionType.equals(GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG)) {
			mPendingACKs++;
			mCountdownAwaitingACKs+=NBR_OF_SECONDS_ALLOWED_FOR_RESPONSE;
			if(mPendingACKs==1) {
				startMyTimer();
			}
		}
    	long backoff = ApplicationMassageNearby.BACKOFF_MILLI_SECONDS +  ApplicationMassageNearby.random.nextInt(1000);
    	for (int i = 1; i <=  ApplicationMassageNearby.MAX_ATTEMPTS; i++) {
    		try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket
                        .getOutputStream())), true);
                out.println(txt);  
                return;
    		} catch (IOException e) {
    			Log.e( TAG, "Failed on attempt " + i + ":" + e);
                try {
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                }
                backoff *= 2;    			
    		}
    	}
    	close();
	}


	/**
	 * @return the mSocket
	 */
	public Socket getmSocket() {
		return mSocket;
	}

	/**
	 * @param mSocket the mSocket to set
	 */
	public void setmSocket(Socket mSocket) {
		this.mSocket = mSocket;
	}


	/**
	 * @return the mItemUserClient
	 */
	public ItemUser getmItemUserClient() {
		return mItemUserClient;
	}

	/**
	 * @param mItemUserClient the mItemUserClient to set
	 */
	public void setmItemUserClient(ItemUser mItemUserClient) {
		this.mItemUserClient = mItemUserClient;
	}

	/**
	 * @return the mItemUserME
	 */
	public ItemUser getmItemUserME() {
		return mItemUserME;
	}

	/**
	 * @param mItemUserME the mItemUserME to set
	 */
	public void setmItemUserME(ItemUser mItemUserME) {
		this.mItemUserME = mItemUserME;
	}
    
	private Timer getMyTimer() {
		if (mTimer == null) {
			mTimer = new Timer("SocketCommunicationsFor"+this.mItemUserClient.getmName());
		}
		return mTimer;
	}
	private void stopMyTimer() {
		if (mTimer != null) {
			try {
				mTimer.cancel();
			} catch (Exception e) {
			}
			try {
				mTimer.purge();
			} catch (Exception e) {
			}
			mTimer = null;
		}
	}	
	private void startMyTimer() {
		stopMyTimer();
		getMyTimer().schedule(new TimerTask() {
			public void run() {		
				synchronized(mSyncObject) {
					mCountdownAwaitingACKs--;
					if(mCountdownAwaitingACKs<=0) {
						close();
					}
				}
			}
		}, 1000, 1000);
	}


}
