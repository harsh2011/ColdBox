package com.hvacci.vaccination;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hvacci.vaccination.java.Vaccine;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Harsh on 18-02-2017.
 */

public class VaccinationListAdapter extends RecyclerView.Adapter<VaccinationListAdapter.VaccinationHolder> {

    private int days;
    private ArrayList<Vaccine> vaccines ;
    private ArrayList<String> dates;
    private GetItemPostionOnClick getItemPostionOnClick;

    public class VaccinationHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        OnItemClickListener onItemClickListener;


        public TextView days,name,date;
        public LinearLayout upper_view;
        public ImageView infobutton;


        public VaccinationHolder(View itemView) {
            super(itemView);
            days = (TextView) itemView.findViewById(R.id.Due_days);
            name = (TextView) itemView.findViewById(R.id.Vaccine_name);
            date = (TextView) itemView.findViewById(R.id.On_date);
            upper_view = (LinearLayout) itemView.findViewById(R.id.upper_view);
            infobutton = (ImageView) itemView.findViewById(R.id.infobutton);

            infobutton.setOnClickListener(this);
            upper_view.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }

    public void setGetItemPostionOnClick(GetItemPostionOnClick getItemPostionOnClick) {
        this.getItemPostionOnClick = getItemPostionOnClick;
    }

    public VaccinationListAdapter(ArrayList<Vaccine> vaccines,int days,ArrayList<String> dates) {
        this.vaccines = vaccines;
        this.days =days;
        this.dates = dates;
    }

    @Override
    public VaccinationHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item, parent, false);

        return new VaccinationHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final VaccinationHolder holder, int postion) {
        Vaccine vaccine = vaccines.get(postion);

        String day = vaccine.getdate();

        int dayint = Integer.parseInt(day);

        int weekint = dayint/7;

        String week = String.valueOf(weekint);

        if(dayint < days){
            holder.upper_view.setBackgroundColor(Color.LTGRAY);

        }
        else{
            holder.upper_view.setBackgroundColor(Color.WHITE);
        }
        holder.date.setText(dates.get(postion));
        holder.days.setText(week);
        holder.name.setText(vaccine.getVName());
        holder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                if(view == holder.upper_view) {
                    if(Integer.parseInt(vaccines.get(postion).getdate()) > days) {
                        getItemPostionOnClick.getItemPostionOnClick(view,postion,1);
                    }

                }
                else if(view == holder.infobutton){
                    getItemPostionOnClick.getItemPostionOnClick(view,postion,2);
                }
            }
        });
    }



    @Override
    public int getItemCount() {
        return vaccines.size();
    }


}
