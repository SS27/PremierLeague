package com.spstanchev.premierleague.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.spstanchev.premierleague.R;
import com.spstanchev.premierleague.models.Fixture;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * Created by Stefan on 1/25/2015.
 */
public class ResultsCustomAdapter extends BaseAdapter {
    private ArrayList<Fixture> results;
    private Context context;

    public ResultsCustomAdapter(Context context) {
        this.context = context;
        this.results = new ArrayList<Fixture>();
    }

    public void updateCollection (ArrayList<Fixture> latestCollection){
        results.clear();
        results.addAll(latestCollection);
        //this.results = latestCollection;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return results.size();
    }

    @Override
    public Fixture getItem(int position) {
        return results.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView tvMatchday, tvTime, tvDate, tvHomeTeamName, tvAwayTeamName, tvGoalsHomeTeam, tvGoalsAwayTeam;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_result, parent, false);
            tvMatchday = (TextView) convertView.findViewById(R.id.tvMatchday);
            tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            tvDate = (TextView) convertView.findViewById(R.id.tvDate);
            tvHomeTeamName = (TextView) convertView.findViewById(R.id.tvHomeTeamName);
            tvAwayTeamName = (TextView) convertView.findViewById(R.id.tvAwayTeamName);
            tvGoalsHomeTeam = (TextView) convertView.findViewById(R.id.tvGoalsHomeTeam);
            tvGoalsAwayTeam = (TextView) convertView.findViewById(R.id.tvGoalsAwayTeam);

            convertView.setTag(R.id.tvMatchday, tvMatchday);
            convertView.setTag(R.id.tvTime, tvTime);
            convertView.setTag(R.id.tvDate, tvDate);
            convertView.setTag(R.id.tvHomeTeamName, tvHomeTeamName);
            convertView.setTag(R.id.tvAwayTeamName, tvAwayTeamName);
            convertView.setTag(R.id.tvGoalsHomeTeam, tvGoalsHomeTeam);
            convertView.setTag(R.id.tvGoalsAwayTeam, tvGoalsAwayTeam);
        } else {
            tvMatchday = (TextView) convertView.getTag(R.id.tvMatchday);
            tvTime = (TextView) convertView.getTag(R.id.tvTime);
            tvDate = (TextView) convertView.getTag(R.id.tvDate);
            tvHomeTeamName = (TextView) convertView.getTag(R.id.tvHomeTeamName);
            tvAwayTeamName = (TextView) convertView.getTag(R.id.tvAwayTeamName);
            tvGoalsHomeTeam = (TextView) convertView.getTag(R.id.tvGoalsHomeTeam);
            tvGoalsAwayTeam = (TextView) convertView.getTag(R.id.tvGoalsAwayTeam);
        }

        Fixture result = getItem(position);
        String date = "";
        String time = "";
        SimpleDateFormat jsonDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        jsonDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        SimpleDateFormat myDateFormat = new SimpleDateFormat("dd MMM");
        SimpleDateFormat myTimeFormat = new SimpleDateFormat("HH:mm");
        myDateFormat.setTimeZone(TimeZone.getDefault());
        myTimeFormat.setTimeZone(TimeZone.getDefault());
        try {
            Date jsonDate = jsonDateFormat.parse(result.getDate());
            Calendar cal = Calendar.getInstance();
            cal.setTime(jsonDate);
            date = myDateFormat.format(cal.getTime());
            time = myTimeFormat.format(cal.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        tvMatchday.setText(result.getMatchday().toString());
        tvTime.setText(time);
        tvDate.setText(date);
        tvHomeTeamName.setText(result.getHomeTeamName());
        tvAwayTeamName.setText(result.getAwayTeamName());
        String goalsHomeTeam = result.getGoalsHomeTeam() == -1 ? "NA" : result.getGoalsHomeTeam().toString() ;
        tvGoalsHomeTeam.setText(goalsHomeTeam);
        String goalsAwayTeam = result.getGoalsAwayTeam() == -1 ? "NA" : result.getGoalsAwayTeam().toString() ;
        tvGoalsAwayTeam.setText(goalsAwayTeam);

        return convertView;
    }

    @Override
    public boolean isEnabled(int position) {
        return false;
    }
}

