package com.spstanchev.premierleague.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.spstanchev.premierleague.R;
import com.spstanchev.premierleague.models.Team;

import java.util.ArrayList;

/**
 * Created by Stefan on 1/24/2015.
 */
public class TeamsCustomAdapter extends BaseAdapter {
    private ArrayList<Team> teams;
    private Context context;

    public TeamsCustomAdapter(Context context) {
        this.context = context;
        this.teams = new ArrayList<Team>();
    }

    public void updateCollection (ArrayList<Team> latestCollection){
        teams.clear();
        teams.addAll(latestCollection);
        //this.teams = latestCollection;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return teams.size();
    }

    @Override
    public Team getItem(int position) {
        return teams.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvTeamName, tvSquadMarketValue;
        ImageView ivTeamCrest;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_team, parent, false);
            tvTeamName = (TextView) convertView.findViewById(R.id.tvTeamName);
            ivTeamCrest = (ImageView) convertView.findViewById(R.id.ivTeamCrest);
            tvSquadMarketValue = (TextView) convertView.findViewById(R.id.tvSquadMarketValue);

            convertView.setTag(R.id.tvTeamName, tvTeamName);
            convertView.setTag(R.id.ivTeamCrest, ivTeamCrest);
            convertView.setTag(R.id.tvSquadMarketValue, tvSquadMarketValue);
        } else {
            tvTeamName = (TextView) convertView.getTag(R.id.tvTeamName);
            ivTeamCrest = (ImageView) convertView.getTag(R.id.ivTeamCrest);
            tvSquadMarketValue = (TextView) convertView.getTag(R.id.tvSquadMarketValue);
        }

        Team team = getItem(position);
        tvTeamName.setText(team.getName());
        int drawableId = getResourceId(team.getCrestResource(), "drawable", context.getPackageName());
        if (drawableId == 0){
            drawableId = R.drawable.premierleague_logo;
        }
        ivTeamCrest.setImageResource(drawableId);
        tvSquadMarketValue.setText(team.getSquadMarketValue());

        return convertView;
    }

    private int getResourceId(String pVariableName, String pResourceName, String pPackageName){
        try {
            return context.getResources().getIdentifier(pVariableName, pResourceName, pPackageName);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
