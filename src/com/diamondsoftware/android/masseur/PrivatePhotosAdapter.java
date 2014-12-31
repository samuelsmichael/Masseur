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

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		int width=parent.getWidth();
		View zView=(View)getItem(position);
		if(position<mCountGotPictures) {
			zView.setLayoutParams(new GridView.LayoutParams((int)(width/2-15), (int)(width/2-15)));
		} else {
			zView.setLayoutParams(new GridView.LayoutParams((int)(width/2-15), (int)(width/2+155)));
		}

		if(position<mCountGotPictures) {
			ImageView iv=(ImageView)((ViewGroup)getItem(position)).findViewById(R.id.ivPrivatePicture);
			String url = "http://"
					+ com.diamondsoftware.android.massagenearby.common.CommonMethods
							.getBaseURL(mActivity)
					+ "/MassageNearby/files/"
					+ mUrls.get(position);

			mImageLoaderRemote.displayImage(url, iv);
		} else {
			final int d=position;
			Button btnFromGallery=(Button)((ViewGroup)getItem(position)).findViewById((R.id.btnPrivateNoImageFromGallery));
			btnFromGallery.setOnClickListener(new View.OnClickListener() {
			
				@Override
				public void onClick(View v) {
					Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
					photoPickerIntent.setType("image/*");
					mFragment.startActivityForResult(
							photoPickerIntent,
							com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby.PHOTO_RESULT_IDS[d]);					
					}
			});
		}

        return (View)getItem(position);
    }
}
