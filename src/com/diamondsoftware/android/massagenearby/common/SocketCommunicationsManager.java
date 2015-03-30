package com.diamondsoftware.android.massagenearby.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import  com.diamondsoftware.android.massagenearby.common.CommonMethods;
import com.diamondsoftware.android.client.MasseurListActivity;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.NetworkInfo;
import android.util.Log;

import com.diamondsoftware.android.common.GlobalStaticValues;
import com.diamondsoftware.android.common.Logger;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.model.ItemClient;
import com.diamondsoftware.android.massagenearby.model.ItemUser;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.ApplicationMassageNearby;
import com.diamondsoftware.android.masseur.MasseurMainActivity;
import com.diamondsoftware.android.masseur.MasseurSocketService;

public class SocketCommunicationsManager  {

	private Socket mSocket;
	private ChatPageManager mChatPageManager;
	private ItemUser mItemUserClient;
	private ItemUser mItemUserME;
	Timer mTimer;
	static int NBR_OF_SECONDS_ALLOWED_FOR_RESPONSE=15;
	private int mPendingACKs;
	private Object mSyncObject=new Object();
	private int mCountdownAwaitingACKs;
	private Context mContext;
	private static final String TAG="TAG_SocketCommunicationsManager";
	
	
	public SocketCommunicationsManager(Socket socket, ChatPageManager chatPageManager, ItemUser itemUserClient, ItemUser itemUserMe, Context context) {
		mContext=context;
		mSocket=socket;
		mChatPageManager=chatPageManager;
		mItemUserME=itemUserMe;
        mItemUserClient=itemUserClient;
        mPendingACKs=0;
        mCountdownAwaitingACKs=0;
		if(mSocket==null) {
        	Semaphore stick2=new Semaphore(0);
        	ItemUser itemUser=itemUserMe;
        	ClientThread ct=new ClientThread(itemUser,stick2,mContext);
            Thread cThread = new Thread(ct);
            cThread.start();
            try {
            	stick2.acquire();
            } catch (InterruptedException e) {
            	return;
            }
			mSocket=itemUser.getmSocket();
		}        
		if(mSocket!=null) {
			new Thread(new ClientThreadReceive()).start();
		}
	}
	   public class ClientThread implements Runnable {
	    	String mIpAddress;
	    	Context mContext;
	    	ItemUser mUser;
	    	Semaphore mStick2;
	    	String errMessage=null;
	    	public ClientThread(com.diamondsoftware.android.massagenearby.model.ItemUser user,Semaphore stick2, Context context) {
	    		mIpAddress=user.getmURL();
	    		mUser=user;
	    		mStick2=stick2;
	    		mContext=context;
	    	}
	  
	        public void run() {
	            try {
	                InetAddress serverAddr = InetAddress.getByName(CommonMethods.getBaseURLForSocketAttaching(mContext,mIpAddress));
	                Log.d("ClientActivity", "C: Connecting...");
		    		new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Connecting to ServerSocket off of: ServerAddress-"+serverAddr.toString()+"; Port-"+mUser.getPort(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);

	                Socket soket= new Socket( serverAddr, mUser.getPort());
					new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Success connecting-"+soket.toString(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
	                mUser.setmSocket(soket);
	                if(mUser instanceof ItemMasseur) {
	                	((ItemMasseur)mUser).setmConnected(true);
	                }
	            } catch (UnknownHostException e) {
		    		new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Failed connecting. Msg: "+e.getMessage(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
	                Log.e("ClientActivity", "C: Error", e);
	                if(mUser instanceof ItemMasseur) {
	                	((ItemMasseur)mUser).setmConnected(false);
	                }
	                errMessage=e.getMessage();
	                if(MasseurMainActivity.mSingleton!=null) {
	                	try {
		                	new Utils.Complainer("Socket connection failure",errMessage,mContext).show(
		                			((MasseurMainActivity)mContext).getFragmentManager(), "SocketError");
	                	} catch (Exception ee) {}
	                } else {
	                	try {
		                	new Utils.Complainer("Socket connection failure",errMessage,mContext).show(
		                			((MasseurListActivity)mContext).getFragmentManager(), "SocketError");
	                	} catch (Exception ed2) {}
	                }
	            } catch (IOException e) {
		    		new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Failed connecting. Msg: "+e.getMessage(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
	                Log.e("ClientActivity", "C: Error", e);
	                if(mUser instanceof ItemMasseur) {
	                	((ItemMasseur)mUser).setmConnected(false);
	                }
	                errMessage=e.getMessage();
	                if(MasseurMainActivity.mSingleton!=null) {
	                	try {
		                	new Utils.Complainer("Socket connection failure",errMessage,mContext).show(
		                			((MasseurMainActivity)mContext).getFragmentManager(), "SocketError");
	                	} catch (Exception ee) {}
	                } else {
	                	try {
		                	new Utils.Complainer("Socket connection failure",errMessage,mContext).show(
		                			((MasseurListActivity)mContext).getFragmentManager(), "SocketError");
	                	} catch (Exception ed2) {}
	                }
	            }
                if(mUser instanceof ItemMasseur) {
                	//TODO: indicate online status? or let socket server do it
                }
	           	mStick2.release();               
	        }
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
                    	String fromName=sa[0];
                    	String fromUserId=sa[1];
                    	String toName=sa[2];
                    	String toUserId=sa[3];
                    	String command=sa[4];
                		if(command.equals(GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG)) {
                        	String msg=sa[5];
                        	String remName=mItemUserClient.getmName();
                    		mItemUserClient.setmName(fromName);
                    		mItemUserClient.setmUserId(Integer.valueOf(fromUserId));
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
		String txt=
				getmItemUserME().getmName()+"~"+getmItemUserME().getmUserId()+"~"+ 
				getmItemUserClient().getmName()+"~"+getmItemUserClient().getmUserId()+ "~"+
				transactionType+"~"+(msg==null?"":msg)+"\r";   	
		if(transactionType.equals(GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG)||transactionType.equals(GlobalStaticValues.COMMAND_IAM)) {
			mPendingACKs++;
			mCountdownAwaitingACKs+=NBR_OF_SECONDS_ALLOWED_FOR_RESPONSE;
			if(mPendingACKs==1) {
	//bbhbb			startMyTimer();
			}
		}
    	long backoff = ApplicationMassageNearby.BACKOFF_MILLI_SECONDS +  ApplicationMassageNearby.random.nextInt(1000);
    	for (int i = 1; i <=  ApplicationMassageNearby.MAX_ATTEMPTS; i++) {
    		try {
                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket
                        .getOutputStream())), true);
                out.println(txt);
//                out.flush();
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
