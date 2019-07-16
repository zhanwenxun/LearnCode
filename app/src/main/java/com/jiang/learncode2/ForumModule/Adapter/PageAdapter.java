package com.jiang.learncode2.ForumModule.Adapter;



import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.jiang.learncode2.ForumModule.Fragment.ContentFragment;




import java.util.List;

public class PageAdapter extends FragmentPagerAdapter {
    public static final String TAB_TAG = "@dream@";

    private List<String> mTitles;

    public PageAdapter(FragmentManager fm, List<String> titles) {
        super(fm);
        mTitles = titles;
    }

    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public android.support.v4.app.Fragment getItem(int position) {
        //初始化Fragment数据
        ContentFragment fragment = new ContentFragment();
        String[] title = mTitles.get(position).split(TAB_TAG);
        fragment.setType(Integer.parseInt(title[1]));
        fragment.setTitle(title[0]);
        return fragment;
    }

    @Override
    public int getCount() {
        return mTitles.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles.get(position).split(TAB_TAG)[0];
    }

}
