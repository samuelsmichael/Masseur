package com.diamondsoftware.android.masseur;

import java.util.ArrayList;

import com.diamondsoftware.android.common.ImageLoaderRemote;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;

public class PrivatePhotosAdapter extends BaseAdapter {
	ArrayList<View> mPrivatePhotoViews;
	ImageLoaderRemote mImageLoaderRemote;
	int mCountGotPictures;
	ItemMasseur mItemMasseur;
	Activity mActivity;
	ArrayList<String> mUrls;
	Fragment mFragment;
	
	public PrivatePhotosAdapter(ArrayList<View> privatePhotoViews, 
			int countGotPictures, ItemMasseur itemMasseur, Activity activity, ArrayList<String> urls,
			Fragment fragment) {
		mPrivatePhotoViews=privatePhotoViews;
		mImageLoaderRemote=new ImageLoaderRemote(activity, false, 0);
		mCountGotPictures=countGotPictures;
		mItemMasseur=itemMasseur;
		mActivity=activity;
		mUrls=urls;
		mFragment=fragment;
	}
	
	@Override
	public int getCount() {
		return mPrivatePhotoViews==null?0:mPrivatePhotoViews.size();
	}

	@Override
	public Object getItem(int position) {
		return mPrivatePhotoViews.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#getItemViewType(int)
	 */
	@Override
	public int getItemViewType(int position) {
		if(position<mCountGotPictures) {
			return 1;
		} else {
			return 1;
		}
	}

	/* (non-Javadoc)
	 * @see android.widget.BaseAdapter#getViewTypeCount()
	 */
	@Override
	public int getViewTypeCount() {
		return 1;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int width=parent.getWidth();
		View zView=(View)getItem(position);
		
		if(position<mCountGotPictures) {
			zView.setLayoutParams(new GridView.LayoutParams((int)(width/2-15), (int)(width/2-15)));
		} else {
			//zView.setLayoutParams(new GridView.LayoutParams((int)(width/2-15), (int)(width/2+155)));
		}
		

		if(position<mCountGotPictures) {
			if(convertView==null) {
				ImageView iv=(ImageView)zView.findViewById(R.id.ivPrivatePicture);
				String url = "http://"
						+ com.diamondsoftware.android.massagenearby.common.CommonMethods
								.getBaseURL(mActivity)
						+ "/MassageNearby/files/"
						+ mUrls.get(position);
	
				mImageLoaderRemote.displayImage(url, iv);
			}
		} else {
		}
        return zView;
    }
}
