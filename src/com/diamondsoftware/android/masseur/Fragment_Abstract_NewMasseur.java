package com.diamondsoftware.android.masseur;

import com.diamondsoftware.android.massagenearby.common.SettingsManager;
import com.diamondsoftware.android.masseur.NavigationDrawerFragment.NavigationDrawerCallbacks;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.MenuItem;

public abstract class Fragment_Abstract_NewMasseur extends Fragment {
    protected NavigationDrawerCallbacks mCallbacks;
	protected SettingsManager mSettingsManager;

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
        try {
            mCallbacks = (NavigationDrawerCallbacks) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException("Activity must implement NavigationDrawerCallbacks.");
        }	
	}
	/* (non-Javadoc)
	 * @see android.support.v4.app.Fragment#onCreate(android.os.Bundle)
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
		getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
        	getActivity().onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    } 
    @Override
    public void onDetach() {
        super.onDetach();
        mCallbacks = null;
    }
}
