package com.diamondsoftware.android.client;

import java.util.Date;
import java.util.GregorianCalendar;

import android.os.Bundle;
import android.app.Fragment;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.diamondsoftware.android.common.ImageLoaderRemote;
import com.diamondsoftware.android.common.Utils;
import com.diamondsoftware.android.massagenearby.common.GlobalStaticValuesMassageNearby;
import com.diamondsoftware.android.massagenearby.model.ItemMasseur;
import com.diamondsoftware.android.masseur.MasseurMainActivity;
import com.diamondsoftware.android.masseur.R;

/**
 * A fragment representing a single Masseur detail screen.
 * This fragment is either contained in a {@link MasseurListActivity}
 * in two-pane mode (on tablets) or a {@link MasseurDetailActivity}
 * on handsets.
 */
public class MasseurDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private ItemMasseur mItemMasseur;
	ImageView mImageMasseur;
	TextView mTvAge;
	TextView tvHeight;
	TextView tvEthnicity;
	TextView tvServices;
	TextView tvBio;


    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public MasseurDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
        	mItemMasseur=MasseurListActivity.mSingleton.getMasseurWhoseUserIdIs(Integer.valueOf(getArguments().getString(ARG_ITEM_ID)));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	if(mItemMasseur!=null) {
    		getActivity().getActionBar().setTitle(mItemMasseur.getmName());
	        View rootView = inflater.inflate(R.layout.fragment_masseur_detail, container, false);
			tvHeight = (TextView) rootView.findViewById(R.id.tvHomeHeight_client);
			tvEthnicity = (TextView) rootView.findViewById(R.id.tvHomeEthnicity_client);
			tvServices = (TextView) rootView.findViewById(R.id.tvHomeServices_client);
			tvBio = (TextView) rootView.findViewById(R.id.tvHomeBio_client);
			mImageMasseur = (ImageView) rootView.findViewById(R.id.imageMasseur_client);
			mTvAge = (TextView) rootView.findViewById(R.id.tvHomeAge_client);
			displayMasseurImage(false);
			GregorianCalendar bdate;
			ItemMasseur im =mItemMasseur;
			tvHeight.setText(im.getHeight());
			bdate = im.getBirthdate();
			Date now = new Date();
			long millisecondsAlive = now.getTime() - bdate.getTimeInMillis();
			int age = (int) (millisecondsAlive / 3.15569e10);
			mTvAge.setText(String.valueOf(age));
			tvEthnicity.setText(im.getEthnicity());
			StringBuilder sb = new StringBuilder();
			String newLine = "";
			if (im.getServices() != null
					&& im.getServices().trim().length() > 0) {
				String[] services = im.getServices().split("\\^", -1);
				for (String svc : services) {
					if (svc.trim().length() > 0) {
						sb.append(newLine + svc);
						newLine = "\n";
					}
				}
			}

			tvServices.setText(sb.toString());
			tvBio.setText(im.getBio());

	        
	        Button btnChat=(Button)rootView.findViewById(R.id.bnt_chat);
	        btnChat.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(),ActivityChat.class)
						.putExtra(GlobalStaticValuesMassageNearby.KEY_CHAT_ID, String.valueOf(mItemMasseur.getmUserId()))
						.putExtra("MasseurName",mItemMasseur.getmName());
					startActivity(intent);
				}
			});
	        return rootView;
    	}
    	return null;
    }
	private void displayMasseurImage(boolean clearCache) {
		if (this.mItemMasseur!=null && mItemMasseur.getMainPictureURL() != null) {
			String url = "http://"
					+ com.diamondsoftware.android.massagenearby.common.CommonMethods
							.getBaseURL(getActivity())
					+ "/MassageNearby/files/"
					+ mItemMasseur
							.getMainPictureURL();
			ImageLoaderRemote ilm = new com.diamondsoftware.android.common.ImageLoaderRemote(
					getActivity(), true, .80f);
			if (clearCache && 1==2) { // in other words ... I don't have to clear cache anymore
				ilm.clearCache();
			}
			ilm.displayImage(url, mImageMasseur);
		}

	}

}
