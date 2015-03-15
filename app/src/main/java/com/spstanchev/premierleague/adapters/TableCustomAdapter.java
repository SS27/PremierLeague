package com.spstanchev.premierleague.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.spstanchev.premierleague.R;
import com.spstanchev.premierleague.models.LeagueTable;

import java.util.ArrayList;

/**
 * Created by Stefan on 1/26/2015.
 */
public class TableCustomAdapter extends BaseAdapter {
    private ArrayList<LeagueTable> leagueTable;
    private Context context;

    public TableCustomAdapter(Context context) {
        this.context = context;
        this.leagueTable = new ArrayList<LeagueTable>();
    }

    public void updateCollection (ArrayList<LeagueTable> latestCollection){
        leagueTable.clear();
        leagueTable.addAll(latestCollection);
        //this.leagueTable = latestCollection;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return leagueTable.size();
    }

    @Override
    public LeagueTable getItem(int position) {
        return leagueTable.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvPosition, tvTeamName, tvPlayedGames, tvGoalsFor, tvGoalsAgainst, tvGoalDifference, tvPoints;

        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_table_row, parent, false);
            tvPosition = (TextView) convertView.findViewById(R.id.tvPosition);
            tvTeamName = (TextView) convertView.findViewById(R.id.tvTeamName);
            tvPlayedGames = (TextView) convertView.findViewById(R.id.tvPlayedGames);
            tvGoalsFor = (TextView) convertView.findViewById(R.id.tvGoalsFor);
            tvGoalsAgainst = (TextView) convertView.findViewById(R.id.tvGoalsAgainst);
            tvGoalDifference = (TextView) convertView.findViewById(R.id.tvGoalDifference);
            tvPoints = (TextView) convertView.findViewById(R.id.tvPoints);

            convertView.setTag(R.id.tvPosition, tvPosition);
            convertView.setTag(R.id.tvTeamName, tvTeamName);
            convertView.setTag(R.id.tvPlayedGames, tvPlayedGames);
            convertView.setTag(R.id.tvGoalsFor, tvGoalsFor);
            convertView.setTag(R.id.tvGoalsAgainst, tvGoalsAgainst);
            convertView.setTag(R.id.tvGoalDifference, tvGoalDifference);
            convertView.setTag(R.id.tvPoints, tvPoints);
        } else {
            tvPosition = (TextView) convertView.getTag(R.id.tvPosition);
            tvTeamName = (TextView) convertView.getTag(R.id.tvTeamName);
            tvPlayedGames = (TextView) convertView.getTag(R.id.tvPlayedGames);
            tvGoalsFor = (TextView) convertView.getTag(R.id.tvGoalsFor);
            tvGoalsAgainst = (TextView) convertView.getTag(R.id.tvGoalsAgainst);
            tvGoalDifference = (TextView) convertView.getTag(R.id.tvGoalDifference);
            tvPoints = (TextView) convertView.getTag(R.id.tvPoints);
        }

        LeagueTable leagueTableEntry = getItem(position);

        tvPosition.setText(leagueTableEntry.getPosition().toString());
        tvTeamName.setText(leagueTableEntry.getTeamName());
        tvPlayedGames.setText(leagueTableEntry.getPlayedGames().toString());
        tvGoalsFor.setText(leagueTableEntry.getGoals().toString());
        tvGoalsAgainst.setText(leagueTableEntry.getGoalsAgainst().toString());
        tvGoalDifference.setText(leagueTableEntry.getGoalDifference().toString());
        tvPoints.setText(leagueTableEntry.getPoints().toString());

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}
