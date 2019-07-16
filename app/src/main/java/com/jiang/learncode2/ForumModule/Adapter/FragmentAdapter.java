package com.jiang.learncode2.ForumModule.Adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import com.jiang.learncode2.ForumModule.Fragment.Algorithm_Fragment;
import com.jiang.learncode2.ForumModule.Fragment.C_Fragment;
import com.jiang.learncode2.ForumModule.Fragment.Data_structure_Fragment;
import com.jiang.learncode2.ForumModule.Fragment.Recommend_Fragment;

import java.util.HashMap;

public class FragmentAdapter extends FragmentPagerAdapter {

    private int num;
    private HashMap<Integer, Fragment> mFragmentHashMap = new HashMap<>();


    public FragmentAdapter(FragmentManager fm , int num) {
        super(fm);
        this.num = num;

    }

    @Override
    public Fragment getItem(int i) {

        return createFragment(i);
    }



    @Override
    public int getCount() {


        return num;
    }


    private Fragment createFragment(int pos) {
        Fragment fragment = mFragmentHashMap.get(pos);

        if (fragment == null) {
            switch (pos) {
                case 0:
                    fragment = new Recommend_Fragment();
                    Log.i("fragment", "fragment1");
                    break;
                case 1:
                    fragment = new Algorithm_Fragment();
                    Log.i("fragment", "fragment2");
                    break;
                case 2:
                    fragment = new Data_structure_Fragment();
                    Log.i("fragment", "fragment3");
                    break;
                case 3:
                    fragment = new C_Fragment();
                    Log.i("fragment", "fragment4");
                    break;
            }
            mFragmentHashMap.put(pos, fragment);
        }
        return fragment;
    }

}
