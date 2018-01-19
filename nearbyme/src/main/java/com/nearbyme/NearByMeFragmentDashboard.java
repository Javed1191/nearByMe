package com.nearbyme;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import services.Shared_Preferences_Class;

public class NearByMeFragmentDashboard extends Fragment
{


	View view;
	private LinearLayout lay_atm,lay_branch;
	public NearByMeFragmentDashboard()
	{
	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);

		view=inflater.inflate(R.layout.fragment_near_by_me_dashboard,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		lay_atm = (LinearLayout) view.findViewById(R.id.lay_atm);
		lay_branch = (LinearLayout) view.findViewById(R.id.lay_branch);

		Shared_Preferences_Class.writeString(getActivity(),Shared_Preferences_Class.SEARCH_TYPE,"");

		lay_atm.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Shared_Preferences_Class.writeString(getActivity(),Shared_Preferences_Class.SEARCH_TYPE,"atm");

				Bundle bundle = new Bundle();
				bundle.putString("location_type", "atm");
				FragmentNearByMe fragment = new FragmentNearByMe();
				fragment.setArguments(bundle);
				android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("NearByMe");
				fragmentTransaction.commit();


			}
		});

		lay_branch.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Shared_Preferences_Class.writeString(getActivity(),Shared_Preferences_Class.SEARCH_TYPE,"branch");

				Bundle bundle = new Bundle();
				bundle.putString("location_type", "branch");
				FragmentNearByMe fragment = new FragmentNearByMe();
				fragment.setArguments(bundle);
				android.support.v4.app.FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.frame, fragment).addToBackStack("NearByMe");
				fragmentTransaction.commit();


			}
		});


		return view;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}



}
