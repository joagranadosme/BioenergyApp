package com.procesosyoperaciones.bioenergy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.procesosyoperaciones.bioenergy.data_objects.Goal;

import java.util.List;

/**
 * Created by Jonathan on 9/18/2017.
 */

public class GoalAdapter extends ArrayAdapter<Goal> {

    public GoalAdapter(Context context, List<Goal> goals){
        super(context, 0, goals);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if(null == convertView){
            convertView = inflater.inflate(R.layout.list_item_goal, parent, false);
            holder = new ViewHolder();
            holder.perspective = (TextView) convertView.findViewById(R.id.perspective);
            holder.strategic = (TextView) convertView.findViewById(R.id.strategic);
            holder.weight = (TextView) convertView.findViewById(R.id.weight);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Goal goal = getItem(position);

        holder.perspective.setText(goal.getPerspective());
        holder.strategic.setText(goal.getGeneral());
        holder.weight.setText(goal.getWeight() + "%");

        return convertView;
    }

    static class ViewHolder{
        TextView perspective;
        TextView strategic;
        TextView weight;
    }

}

