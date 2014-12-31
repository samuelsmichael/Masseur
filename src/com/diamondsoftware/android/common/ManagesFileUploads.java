package com.diamondsoftware.android.common;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

public interface ManagesFileUploads {
	void alert(String message);
	void heresTheResponse(String response, ArrayList<NameValuePair> parms);
}
