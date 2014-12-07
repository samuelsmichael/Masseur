package com.diamondsoftware.android.massagenearby.model;

import java.net.Socket;

import com.diamondsoftware.android.masseur.MasseurMainActivity.ClientThreadReceive;

public class ItemClient {
	private String mName;
	private ClientThreadReceive mClientThreadReceive;
	private int mClientId;
	/**
	 * @return the mClientId
	 */
	public int getmClientId() {
		return mClientId;
	}

	/**
	 * @param mClientId the mClientId to set
	 */
	public void setmClientId(int mClientId) {
		this.mClientId = mClientId;
	}

	public void close() {
		mClientThreadReceive.mClientIsAlive=true;
	}

	/**
	 * @return the mName
	 */
	public String getmName() {
		return mName;
	}
	/**
	 * @param mName the mName to set
	 */
	public void setmName(String mName) {
		this.mName = mName;
	}
	/**
	 * @return the mClientThreadReceive
	 */
	public ClientThreadReceive getmClientThreadReceive() {
		return mClientThreadReceive;
	}
	/**
	 * @param mClientThreadReceive the mClientThreadReceive to set
	 */
	public void setmClientThreadReceive(ClientThreadReceive mClientThreadReceive) {
		this.mClientThreadReceive = mClientThreadReceive;
	}


}
