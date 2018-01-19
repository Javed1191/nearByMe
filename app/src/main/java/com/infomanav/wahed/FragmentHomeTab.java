package com.infomanav.wahed;


import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import adapter.HomeFragmentTabAdapter;

import static com.infomanav.wahed.R.id.toolbar;
import static com.infomanav.wahed.R.id.tv_action_title;

public class FragmentHomeTab extends Fragment
{
	View view;
	private TabLayout tabLayout;
	private Toolbar toolbar;
	private TextView tv_action_title;

	public FragmentHomeTab()
	{
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		// Enable the option menu for the Fragment
		setHasOptionsMenu(true);
		
		view=inflater.inflate(R.layout.fragment_home,container, false);
		getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		tabLayout = (TabLayout)view.findViewById(R.id.tab_layout);
		tabLayout.addTab(tabLayout.newTab().setText("Overview").setIcon(R.drawable.selector_tab_overview));
		tabLayout.addTab(tabLayout.newTab().setText("Position").setIcon(R.drawable.selector_tab_position));
		tabLayout.addTab(tabLayout.newTab().setText("Transaction").setIcon(R.drawable.selector_tab_transaction));
		tabLayout.addTab(tabLayout.newTab().setText("Documents").setIcon(R.drawable.selector_tab_document));

		// tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
		tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

		final ViewPager viewPager = (ViewPager) view.findViewById(R.id.pager);

		FragmentManager fm = getActivity().getSupportFragmentManager();
		final HomeFragmentTabAdapter adapter = new HomeFragmentTabAdapter(fm, tabLayout.getTabCount());
		viewPager.setAdapter(adapter);
		//tabLayout.setupWithViewPager(viewPager);
		viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

		toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
		//toolbar.setLogo(R.drawable.logo);
		// toolbar.setTitle(" Important Contacts");

		tv_action_title = (TextView) toolbar.findViewById(R.id.tv_action_title);



		tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
			@Override
			public void onTabSelected(TabLayout.Tab tab)
			{
				viewPager.setCurrentItem(tab.getPosition());

				if (tab.getPosition() == 0)
				{
					tv_action_title.setText("Overview");
				}
				else if(tab.getPosition() == 1)
				{
					tv_action_title.setText("Position");
				}
				else if(tab.getPosition() == 2)
				{
					tv_action_title.setText("Transaction");
				}
				else if(tab.getPosition() == 3)
				{
					tv_action_title.setText("Documents");
				}

			}

			@Override
			public void onTabUnselected(TabLayout.Tab tab) {

			}

			@Override
			public void onTabReselected(TabLayout.Tab tab) {

			}
		});

		changeTabsFont();

		return view;
	}


	public static void hideSoftKeyboard(Activity activity)
	{
		InputMethodManager inputMethodManager =
				(InputMethodManager) activity.getSystemService(
						Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(
				activity.getCurrentFocus().getWindowToken(), 0);
	}



	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.

		return super.onOptionsItemSelected(item);
	}

	private void changeTabsFont() {
		Typeface type = Typeface.createFromAsset(getActivity().getAssets(),"fonts/CenturyGothic_regular.ttf");

		ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
		int tabsCount = vg.getChildCount();
		for (int j = 0; j < tabsCount; j++) {
			ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
			int tabChildsCount = vgTab.getChildCount();
			for (int i = 0; i < tabChildsCount; i++) {
				View tabViewChild = vgTab.getChildAt(i);

				if (tabViewChild instanceof TextView) {
					((TextView) tabViewChild).setTypeface(type);
					((TextView) tabViewChild).setTextSize(11);



				}
			}
		}
	}



/*	private void getCategory()
	{

		if(utility.checkInternet())
		{
			//final ArrayList<Department> feedsArrayList = new ArrayList<Department>();

			final Map<String, String> params = new HashMap<String, String>();

			ServiceHandler serviceHandler = new ServiceHandler(getActivity());


			serviceHandler.registerUser(params, str_get_category_url, new ServiceHandler.VolleyCallback() {
				@Override
				public void onSuccess(String result) {
					System.out.println("str_get_category_url Json responce" + result);

					//Toast.makeText(MainActivity.this, "Json responce"+result, Toast.LENGTH_SHORT).show();
					String str_json = result;
					String str_status, str_msg;
					String  categoryID, parentID,categoryTitle,description,categoryImage,seoUri,colorCode,
							templateFile,metaTitle,metaKeyword,metaDesc,dateAdded,status;
					appsList = new ArrayList<>();
					try {
						if (str_json != null) {
							JSONObject jobject = new JSONObject(str_json);
							str_status = jobject.getString("status");
							str_msg = jobject.getString("msg");
							if (str_status.equalsIgnoreCase("success"))
							{
								JSONArray jsonArray = new JSONArray();
								jsonArray = jobject.getJSONArray("data");

								for(int i=0; i<jsonArray.length();i++)
								{
									JSONObject Obj = jsonArray.getJSONObject(i);

									categoryID = Obj.getString("categoryID");
									parentID = Obj.getString("parentID");
									categoryTitle = Obj.getString("categoryTitle");
									description = Obj.getString("description");
									categoryImage = Obj.getString("categoryImage");
									seoUri = Obj.getString("seoUri");
									colorCode = Obj.getString("colorCode");
									templateFile = Obj.getString("templateFile");
									metaTitle = Obj.getString("metaTitle");
									metaKeyword = Obj.getString("metaKeyword");
									metaDesc = Obj.getString("metaDesc");
									dateAdded = Obj.getString("dateAdded");
									status = Obj.getString("status");

									list_category.add(categoryTitle);
									list_category_id.add(categoryID);

								}

								ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list_category);
								auto_category.setAdapter(adapter);
                               *//* ArrayAdapter<String> adapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,list_name);
                                mAutoCompleteTextView.setAdapter(adapter);*//*



							} else {
								Toast.makeText(getActivity(), str_msg, Toast.LENGTH_SHORT).show();
							}
						} else {
							Toast.makeText(getActivity(), "This may be server issue", Toast.LENGTH_SHORT).show();
						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
		}

	}*/





}
