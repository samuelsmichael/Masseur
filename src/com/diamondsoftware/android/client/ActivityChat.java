package com.diamondsoftware.android.client;

import android.content.ContentValues;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.diamondsoftware.android.common.GlobalStaticValues;
import com.diamondsoftware.android.massagenearby.common.ChatPageManager;
import com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby;
import com.diamondsoftware.android.massagenearby.common.MessagesFragment;
import com.diamondsoftware.android.massagenearby.common.SocketCommunicationsManager;
import com.diamondsoftware.android.massagenearby.common.TellMeWhenYouveGotNewMesseurs;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.massagenearby.model.ItemUser;
import com.diamondsoftware.android.masseur.ApplicationMassageNearby;
import com.diamondsoftware.android.masseur.DataProvider;
import com.diamondsoftware.android.masseur.R;

public class ActivityChat extends FragmentActivity implements
		MessagesFragment.OnFragmentInteractionListener,
		ChatPageManager, TellMeWhenYouveGotNewMesseurs {
	String mProfileChatId;
	SocketCommunicationsManager mSCM;
	Button mBtnSend;
	EditText mEditText;
	ItemMasseur mItemMasseur;

	@Override
	public String getProfileChatId() {

		return mProfileChatId;
	}	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activity_chat);
		mEditText=(EditText)findViewById(R.id.msg_edit_client);
		mBtnSend=(Button)findViewById(R.id.send_btn_client);
		mBtnSend.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String msg=mEditText.getText().toString().trim();
				if(!TextUtils.isEmpty(msg)) {
					mBtnSend.setEnabled(false);
				}
				try {
					mSCM.doSend(com.diamondsoftware.android.common.GlobalStaticValues.COMMAND_HERES_MY_CHAT_MSG, msg,mItemMasseur);
				} catch (Exception e) {
    				mBtnSend.setEnabled(true);
       				return;
				}
				
				ContentValues values=new ContentValues(2);
				values.put(DataProvider.COL_MSG, msg);
				values.put(DataProvider.COL_TO, String.valueOf(mItemMasseur.getmUserId()));
				getContentResolver().insert(DataProvider.CONTENT_URI_MESSAGES, values);
				
				mEditText.setText(null);
				mBtnSend.setEnabled(true);
			}
		});
		mProfileChatId=this.getIntent().getExtras().getString(GlobalStaticValuesMassageNearby.KEY_CHAT_ID);
		mItemMasseur=MasseurListActivity.mSingleton.getMasseurWhoseUserIdIs(Integer.valueOf(mProfileChatId));
		getActionBar().setTitle(mItemMasseur.getmName());

		if(mSCM!=null) {
			try {
				mSCM=new SocketCommunicationsManager(
						this, 
						ApplicationMassageNearby.mSingletonApp.mItemClientMe,this, new Handler());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		fragmentManager.beginTransaction()
				.add(R.id.container, MessagesFragment.newInstance(mItemMasseur)).commit();

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chat, menu);
		return true;
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

	@Override
	public void lostCommunicationsWith(ItemUser itemUser, SocketCommunicationsManager.REASON_FOR_CLOSE reasonForClose) {
		// 1. get new list of mMasseurs
		MasseurListActivity.mSingleton.updateMasseursList(this);
	}

	@Override
	public void weveGotAChat(ItemUser user) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void IveGotNewMesseurs() {
		android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
		MessagesFragment theOldOne = (MessagesFragment)fragmentManager.findFragmentById(R.id.container);
		fragmentManager.beginTransaction()
				.remove(theOldOne)
				.add(R.id.container, MessagesFragment.newInstance(mItemMasseur)).commit();
	}
}
