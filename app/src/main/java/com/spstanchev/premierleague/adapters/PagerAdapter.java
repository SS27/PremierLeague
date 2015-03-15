package com.spstanchev.premierleague.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.spstanchev.premierleague.fragments.FixturesFragment;
import com.spstanchev.premierleague.fragments.LeagueTableFragment;
import com.spstanchev.premierleague.fragments.PremierLeagueFragment;
import com.spstanchev.premierleague.fragments.ResultsFragment;
import com.spstanchev.premierleague.common.Constants;

/**
 * Created by Stefan on 1/24/2015.
 */
public class PagerAdapter extends FragmentPagerAdapter{

    public PagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {
        Fragment fragment;
        switch (i){
            case 0 :
                fragment = new PremierLeagueFragment();
                break;
            case 1 :
                fragment = new ResultsFragment();
                break;
            case 2 :
                fragment = new FixturesFragment();
                break;
            case 3 :
                fragment = new LeagueTableFragment();
                break;
            default:
                fragment = null;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        CharSequence title = "";
        switch (position){
            case 0:
                title = Constants.TAG_LEAGUE;
                break;
            case 1:
                title = Constants.TAG_RESULTS;
                break;
            case 2:
                title =  Constants.TAG_FIXTURES;
                break;
            case 3:
                title =  Constants.TAG_STANDINGS;
                break;
        }
        return title;
    }
}
