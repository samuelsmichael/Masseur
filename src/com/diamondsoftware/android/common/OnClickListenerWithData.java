package com.diamondsoftware.android.common;

import java.util.ArrayList;

import android.view.View;

public class OnClickListenerWithData implements View.OnClickListener {
	protected ArrayList<Object> mData;
	public OnClickListenerWithData(ArrayList<Object> al) {
		mData=al;
	}
	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		
	}

}
