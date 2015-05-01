package com.diamondsoftware.android.massagenearby.common;

import android.content.ContentResolver;

import com.diamondsoftware.android.massagenearby.model.ItemUser;

public interface ChatPageManager {
	void lostCommunicationsWith(ItemUser itemUser, SocketCommunicationsManager.REASON_FOR_CLOSE reasonForClose);
	ContentResolver getContentResolver();
	void weveGotAChat(ItemUser user);
}
