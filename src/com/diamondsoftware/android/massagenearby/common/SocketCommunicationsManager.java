package com.diamondsoftware.android.massagenearby.common;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Semaphore;

import android.content.ContentValues;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;

import com.diamondsoftware.android.client.MasseurListActivity;
import com.diamondsoftware.android.common.GlobalStaticValues;
import com.diamondsoftware.android.common.Logger;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.model.ItemClient;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.massagenearby.model.ItemUser;
import com.diamondsoftware.android.masseur.ApplicationMassageNearby;
import com.diamondsoftware.android.masseur.MasseurMainActivity;
import com.diamondsoftware.android.masseur.MasseurSocketService;

public class SocketCommunicationsManager  {

	private Socket mSocket;
	private ChatPageManager mChatPageManager;
	private ItemUser mItemUserME;
	ItemUserTimer mTimer;
	static int NBR_OF_SECONDS_ALLOWED_FOR_RESPONSE=15;
	private int mPendingACKs;
	private Object mSyncObject=new Object();
	private int mCountdownAwaitingACKs;
	private Context mContext;
	private static final String TAG="TAG_SocketCommunicationsManager";
	public static InetAddress marremaismarre=null;
	private Timer mNetworkPollingTimer=null;
	ConnectivityManager mConnectivityManager=null;
	private Handler mHandler=null;
	String mInetAddress;
	int mConnectivityType=-1;
	
	public enum REASON_FOR_CLOSE {
		NORMAL,
		FAILED_SENDING,
		JUST_LOST_CONNECTION,
		TIMED_OUT,
		CHANGED_NETWORK_TYPE
	}
	
	
	public SocketCommunicationsManager(ChatPageManager chatPageManager, ItemUser itemUserMe, Context context, Handler handler) throws Exception {
		mContext=context;
		mChatPageManager=chatPageManager;
		mItemUserME=itemUserMe;
        mPendingACKs=0;
        mCountdownAwaitingACKs=0;
    	mConnectivityManager=(ConnectivityManager)context.getSystemService(context.CONNECTIVITY_SERVICE);
    	mHandler=handler;

        initialize();
	}
	private void initialize() {
		new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Initialize()", com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);

		mInetAddress=null;
		mConnectivityType=-1;
    	Semaphore stick2=new Semaphore(0);
    	ClientThread ct=new ClientThread(mItemUserME,stick2,mContext);
        Thread cThread = new Thread(ct);
        cThread.start();
        try {
        	stick2.acquire();
        } catch (InterruptedException e) {
        	return;
        }
		mSocket=mItemUserME.getmSocket();
		if(mSocket!=null) {
			try {
				new Thread(new ClientThreadReceive()).start();
				new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("doSend(COMMAND_IAM)", com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);

				doSend(GlobalStaticValues.COMMAND_IAM, "",null);
				startMyNetworkPollingTimer();
			} catch (Exception e) {
				close(REASON_FOR_CLOSE.FAILED_SENDING,null);
			}			
		} else {
			new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Starting handler", com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
			Runnable mUpdateTimeTask = new Runnable() {
			    public void run() {
			    	 initialize();
			    }
			};

			mHandler.postDelayed(mUpdateTimeTask, 5000);
/*
			mUpdateTimeHandler.removeCallbacks(mUpdateTimeTask);			CountDownTimer cdt= new CountDownTimer(5000, 1000) {
			     public void onTick(long millisUntilFinished) {
						new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Starting CountDownTimer - tictoc", com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
			     }
			     public void onFinish() {
			         initialize();
			     }
			  };
			  cdt.start();*/
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
	            	if(marremaismarre==null) {
	            		marremaismarre=InetAddress.getByName(mIpAddress);
	            	}
	            	Socket soket=null;
	            	mUser.setmSocket(soket);
		    		try {
		                InetAddress serverAddr = InetAddress.getByName(CommonMethods.getBaseURLForSocketAttaching(mContext,mIpAddress));
		                Log.d("ClientActivity", "C: Connecting...");
			    //		new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("------1--------Connecting to ServerSocket off of: ServerAddress-"+serverAddr.toString()+"; Port-"+mUser.getPort(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
			    		soket= new Socket( serverAddr, mUser.getPort());
		    		} catch (Exception e) {
		    			try {
		                InetAddress serverAddr = InetAddress.getByName(mIpAddress);
		                Log.d("ClientActivity", "C: Connecting...");
			    	//	new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("-------2-------Connecting to ServerSocket off of: ServerAddress-"+serverAddr.toString()+"; Port-"+mUser.getPort(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
			    		soket= new Socket( serverAddr, mUser.getPort());
		    			} catch (Exception e2) {
				   // 		new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("-------3-------Connecting to ServerSocket off of: ServerAddress-"+marremaismarre.toString()+"; Port-"+mUser.getPort(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
				    		soket= new Socket( marremaismarre, mUser.getPort());
		    			}
		    		}
				//	new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Success connecting-"+soket.toString(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
	                mUser.setmSocket(soket);
	                if(mUser instanceof ItemMasseur) {
	                	((ItemMasseur)mUser).setmConnected(true);
	                }
	            } catch (UnknownHostException e) {
		    		new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Failed connecting A. Msg: "+e.getMessage(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
	                Log.e("ClientActivity", "C: Error", e);
	                if(mUser instanceof ItemMasseur) {
	                	((ItemMasseur)mUser).setmConnected(false);
	                }
	                errMessage=e.getMessage();
	                if(MasseurMainActivity.mSingleton!=null) {
	                	try {
		           //     	new Utils.Complainer("Socket connection failure",errMessage,mContext).show(
		           //     			((MasseurMainActivity)mContext).getFragmentManager(), "SocketError");
	                	} catch (Exception ee) {}
	                } else {
	                	try {
		            //    	new Utils.Complainer("Socket connection failure",errMessage,mContext).show(
		            //    			((MasseurListActivity)mContext).getFragmentManager(), "SocketError");
	                	} catch (Exception ed2) {}
	                }
	            } catch (IOException e) {
		    		new Logger(new SettingsManager(mContext).getLoggingLevel(),"SocketCommunicationsManager",mContext).log("Failed connecting B. Msg: "+e.getMessage(), com.diamondsoftware.android.common.GlobalStaticValues.LOG_LEVEL_CRITICAL);
	                Log.e("ClientActivity", "C: Error", e);
	                if(mUser instanceof ItemMasseur) {
	                	((ItemMasseur)mUser).setmConnected(false);
	                }
	                errMessage=e.getMessage();
	                if(MasseurMainActivity.mSingleton!=null) {
	                	try {
		            //    	new Utils.Complainer("Socket connection failure",errMessage,mContext).show(
		            //    			((MasseurMainActivity)mContext).getFragmentManager(), "SocketError");
	                	} catch (Exception ee) {}
	                } else {
	                	try {
		            //    	new Utils.Complainer("Socket connection failure",errMessage,mContext).show(
		           //     			((MasseurListActivity)mContext).getFragmentManager(), "SocketError");
	                	} catch (Exception ed2) {}
	                }
	            }
                if(mUser instanceof ItemMasseur) {
                	//TODO: indicate online status? or let socket server do it
                }
	           	mStick2.release();               
	        }
	    }

	public synchronized void close(REASON_FOR_CLOSE reasonForClose, ItemUser itemUser) {
		if(mSocket!=null) {
			try {
				mSocket.close();
			} catch (Exception e) {}
			mSocket=null;
			stopMyTimer();
			
			if(reasonForClose.equals(REASON_FOR_CLOSE.TIMED_OUT)) {
				mChatPageManager.lostCommunicationsWith(itemUser,reasonForClose);
			} else {
				if(!reasonForClose.equals(REASON_FOR_CLOSE.NORMAL)) {
					initialize();
				}
			}
		}
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
				jdfdontenter=true;
				mNetworkPollingTimer.cancel();
				mNetworkPollingTimer.purge();
			} catch (Exception e) {
			}
			mNetworkPollingTimer = null;
			mInetAddress=null;
			mConnectivityType=-1;
		}
	}	
	int nbrOfConsecutiveDontReenters=0;
	boolean jdfdontenter=false;
	private void startMyNetworkPollingTimer() {
		stopMyNetworkPollingTimer();
		jdfdontenter=false;
		getMyNetWorkPollingTimer().schedule(new TimerTask() {
			public void run() {				
				if(!jdfdontenter) {
					try {
						nbrOfConsecutiveDontReenters=0;
						 NetworkInfo networkInfo =mConnectivityManager.getActiveNetworkInfo ();
						 if(networkInfo!=null && networkInfo.isConnected()) {
							 String localIpAddress=com.diamondsoftware.android.common.CommonMethods.getLocalIpAddress(mContext);
							 if(mInetAddress!=null && !localIpAddress.equals(mInetAddress)) {
								SocketCommunicationsManager.this.close(REASON_FOR_CLOSE.CHANGED_NETWORK_TYPE,null);
							 } else {
								 if(mInetAddress==null) {
									 mInetAddress=localIpAddress;
								 }
								 int connectivityType=networkInfo.getType();
								 if(mConnectivityType!=-1 && connectivityType != mConnectivityType) {
										SocketCommunicationsManager.this.close(REASON_FOR_CLOSE.CHANGED_NETWORK_TYPE,null);
								 } else {
									 if(mConnectivityType==-1) {
										 mConnectivityType=connectivityType;
									 }
								 }
							 }
						 }
					} catch (Exception e) {}
				}
			}
		}, ApplicationMassageNearby.NETWORK_STATUS_POLLING_INTERVAL_IN_MILLISECONDS, ApplicationMassageNearby.NETWORK_STATUS_POLLING_INTERVAL_IN_MILLISECONDS);
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
                        	ItemClient itemOtherGuy=new ItemClient();
                    		itemOtherGuy.setmName(fromName);
                    		itemOtherGuy.setmUserId(Integer.valueOf(fromUserId));
                			ContentValues values = new ContentValues(2);
                			values.put(com.diamondsoftware.android.masseur.DataProvider.COL_MSG, msg);
                			values.put(com.diamondsoftware.android.masseur.DataProvider.COL_FROM, String.valueOf(itemOtherGuy.getmUserId()));
                			values.put(com.diamondsoftware.android.masseur.DataProvider.COL_TO, String.valueOf(mItemUserME.getmUserId()));
                			mChatPageManager.getContentResolver().insert(com.diamondsoftware.android.masseur.DataProvider.CONTENT_URI_MESSAGES, values);
               				mChatPageManager.weveGotAChat(itemOtherGuy);
                			doSend(GlobalStaticValues.COMMAND_ACK,null,itemOtherGuy);
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
					close(REASON_FOR_CLOSE.JUST_LOST_CONNECTION,null);
				}
			} catch (Exception e) {}
		}
    	
    }    
    
    /*
     * Make msg null, if it's not pertinent
     */
	public void doSend(String transactionType,String msg, ItemUser itemSendee) throws Exception {
		if(mSocket!=null) {
			String txt=
					getmItemUserME().getmName()+"~"+getmItemUserME().getmUserId()+"~"+ 
					(itemSendee!=null?itemSendee.getmName():"")+"~"+(itemSendee!=null?itemSendee.getmUserId():"")+ "~"+
					transactionType+"~"+(msg==null?"":msg)+"\r";   	
			if(transactionType.equals(GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG)||transactionType.equals(GlobalStaticValues.COMMAND_IAM) ||
					transactionType.equals(GlobalStaticValues.COMMAND_CLIENTIS)) {
				mPendingACKs++;
				mCountdownAwaitingACKs+=NBR_OF_SECONDS_ALLOWED_FOR_RESPONSE;
				if(mPendingACKs==1) {
					startMyTimer(itemSendee);
				}
			}
	    	long backoff = ApplicationMassageNearby.BACKOFF_MILLI_SECONDS +  ApplicationMassageNearby.random.nextInt(1000);
	    	for (int i = 1; i <=  ApplicationMassageNearby.MAX_ATTEMPTS; i++) {
	    		try {
	                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(mSocket
	                        .getOutputStream())), true);
	                out.println(txt);
	                out.flush();
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
	    	close(REASON_FOR_CLOSE.FAILED_SENDING,null);
    	}
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
    
	private class ItemUserTimer extends Timer {
		private ItemUser mItemUser;
		public ItemUserTimer(String name,ItemUser itemUser) {
			super(name);
			mItemUser=itemUser;
		}
	}
	
	private ItemUserTimer getMyTimer(ItemUser itemUser) {
		if (mTimer == null) {
			mTimer = new ItemUserTimer("SocketCommunicationsFor"+new Date().getTime(),itemUser);
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
	private void startMyTimer(final ItemUser itemUser) {
		stopMyTimer();
		getMyTimer(itemUser).schedule(new TimerTask() {
			public void run() {		
				synchronized(mSyncObject) {
					mCountdownAwaitingACKs--;
					if(mCountdownAwaitingACKs<=0) {
						close(REASON_FOR_CLOSE.TIMED_OUT,itemUser);
					}
				}
			}
		}, 1000, 1000);
	}


}
