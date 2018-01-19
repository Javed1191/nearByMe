package adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.infomanav.wahed.FragmentExposure;
import com.infomanav.wahed.FragmentHighlights;
import com.infomanav.wahed.FragmentPerformance;
import com.infomanav.wahed.FragmentShariahboard;

/**
 * Created by USER on 24-12-2015.
 */
public class PositionDetailsTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private String highlights,Symbol="";

    public PositionDetailsTabAdapter(FragmentManager fm, int NumOfTabs, String highlights, String Symbol)
    {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.highlights=highlights;
        this.Symbol=Symbol;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Bundle bundle = new Bundle();
                bundle.putString("highlights", highlights);
                FragmentHighlights fragmentHighlights = new FragmentHighlights();
                fragmentHighlights.setArguments(bundle);
                return fragmentHighlights;
            case 1:
                Bundle performanceBundle = new Bundle();
                performanceBundle.putString("Symbol", Symbol);
                FragmentPerformance fragmentPerformance = new FragmentPerformance();
                fragmentPerformance.setArguments(performanceBundle);
                return fragmentPerformance;
            case 2:
                Bundle exposureBundle = new Bundle();
                exposureBundle.putString("Symbol", Symbol);
                FragmentExposure fragmentExposure = new FragmentExposure();
                fragmentExposure.setArguments(exposureBundle);
                return fragmentExposure;

            case 3:
                Bundle shariahBundle = new Bundle();
                shariahBundle.putString("symbol", Symbol);
                FragmentShariahboard fragmentShariahad = new FragmentShariahboard();
                fragmentShariahad.setArguments(shariahBundle);
                return fragmentShariahad;
            default:
                return null;
        }
    }



    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}