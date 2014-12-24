package com.diamondsoftware.android.common;

import java.io.InputStream;
import java.util.List;

import org.apache.http.NameValuePair;

import android.app.ProgressDialog;

public class HttpFileUploadParameters {
	 String mRemoteURL;
	 List<NameValuePair> mParameters;
	 ProgressDialog mProgressDialog;
	 InputStream mInputStream;
	 ManagesFileUploads mCanShowAlert;
	 int mUserId;
	public HttpFileUploadParameters(String remoteURL, List<NameValuePair> parameters, ProgressDialog progressDialog, InputStream inputStream, ManagesFileUploads canShowAlert, int userId) {
		mRemoteURL=remoteURL;
		mParameters=parameters;
		mProgressDialog=progressDialog;
		mInputStream=inputStream;
		mCanShowAlert=canShowAlert;
		mUserId=userId;
	}

}
