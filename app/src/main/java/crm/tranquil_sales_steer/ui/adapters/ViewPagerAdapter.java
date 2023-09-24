package crm.tranquil_sales_steer.ui.adapters;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by venkei on 03-Apr-18.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    List<String> name = new ArrayList<>();

    public ViewPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();

    }

    @Override
    public CharSequence getPageTitle(int position) {
        return name.get(position);

    }

    public void addFragment(Fragment fragment, String tittle) {
        fragments.add(fragment);
        name.add(tittle);
    }

}
