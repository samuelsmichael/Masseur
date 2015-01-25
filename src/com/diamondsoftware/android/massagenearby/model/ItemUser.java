package com.diamondsoftware.android.massagenearby.model;

import java.net.Socket;

public abstract class ItemUser {
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return mName;
	}
	protected int mUserId;
	protected String mName;
	protected String mURL;
	protected String Password;
	protected String Email;
	int Port;
	Socket mSocket;

	public Socket getmSocket() {
		return mSocket;
	}
	public void setmSocket(Socket mSocket) {
		this.mSocket = mSocket;
	}
	/**
	 * @return the port
	 */
	public int getPort() {
		return Port;
	}
	/**
	 * @param port the port to set
	 */
	public void setPort(int port) {
		Port = port;
	}
	/**
	 * @return the email
	 */
	public String getEmail() {
		return Email;
	}
	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		Email = email;
	}
	/**
	 * @return the password
	 */
	public String getPassword() {
		return Password;
	}
	/**
	 * @param password the password to set
	 */
	public void setPassword(String password) {
		Password = password;
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
	 * @return the mURL
	 */
	public String getmURL() {
		return mURL;
	}
	/**
	 * @param mURL the mURL to set
	 */
	public void setmURL(String mURL) {
		this.mURL = mURL;
	}
	/**
	 * @return the mUserId
	 */
	public int getmUserId() {
		return mUserId;
	}
	/**
	 * @param mUserId the mUserId to set
	 */
	public void setmUserId(int mUserId) {
		this.mUserId = mUserId;
	}
}
