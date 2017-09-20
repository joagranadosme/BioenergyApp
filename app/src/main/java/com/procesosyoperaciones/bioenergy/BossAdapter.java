package com.procesosyoperaciones.bioenergy;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.procesosyoperaciones.bioenergy.data_objects.Boss;

import java.util.List;

/**
 * Created by Jonathan on 9/16/2017.
 */

public class BossAdapter extends ArrayAdapter<Boss> {

    private Boss current;

    public BossAdapter(Context context, List<Boss> bosses){
        super(context, 0, bosses);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ViewHolder holder;
        if(null == convertView) {
            convertView = inflater.inflate(R.layout.list_item_boss, parent, false);
            holder = new ViewHolder();
            holder.avatar = (ImageView) convertView.findViewById(R.id.avatar);
            holder.name = (TextView) convertView.findViewById(R.id.name);
            holder.title = (TextView) convertView.findViewById(R.id.tittle);
            holder.company = (TextView) convertView.findViewById(R.id.company);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        Boss boss = getItem(position);

        holder.name.setText(boss.getName());
        holder.title.setText(boss.getTitle());
        holder.company.setText(boss.getCompany());
        Glide.with(getContext()).load(boss.getImage()).into(holder.avatar);

        return convertView;
    }

    static class ViewHolder{
        ImageView avatar;
        TextView name;
        TextView title;
        TextView company;
    }

}

