package adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.infomanav.wahed.FragmentDocuments;
import com.infomanav.wahed.FragmentOverview;
import com.infomanav.wahed.FragmentPosition;
import com.infomanav.wahed.FragmentTabTransaction;

/**
 * Created by USER on 24-12-2015.
 */
public class HomeFragmentTabAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;
    private String businessID;

    public HomeFragmentTabAdapter(FragmentManager fm, int NumOfTabs)
    {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
        this.businessID=businessID;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:


                FragmentOverview fragmentOverview = new FragmentOverview();
                return fragmentOverview;
            case 1:

                FragmentPosition fragmentPosition = new FragmentPosition();
                return fragmentPosition;
            case 2:

                FragmentTabTransaction fragmentTabTransaction = new FragmentTabTransaction();
                return fragmentTabTransaction;

            case 3:

                FragmentDocuments fragmentDocuments = new FragmentDocuments();
                return fragmentDocuments;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}