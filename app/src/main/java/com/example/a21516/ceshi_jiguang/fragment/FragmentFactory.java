package com.example.a21516.ceshi_jiguang.fragment;

import android.support.v4.app.Fragment;

import java.util.HashMap;

public class FragmentFactory {
    private static HashMap<Integer,Fragment> fragments;
    public static Fragment createFragment(int position){
        fragments=new HashMap<Integer, Fragment>();
        Fragment fragment=fragments.get(position);
        if (fragment == null){
            switch (position){

                case 0:
                        fragment=new MessageFragment();
                    break;
                case 1:
                        fragment=new ContactFragment();
                    break;
                case 2 :
                        fragment= new QZoneFragment();
                    break;
            }
            fragments.put(position,fragment);
        }
        return fragment;
    }
}
