package com.hvacci.vaccination;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hvacci.vaccination.java.BookVaccine;

import java.util.ArrayList;


/**
 * Created by Harsh on 01-06-2017.
 */

public class ApprovedListAdapter extends  RecyclerView.Adapter<ApprovedListAdapter.ApprovedViewHolder>{

    private GetItemPostionOnClick getItemPostionOnClick;

    ArrayList<BookVaccine> list ;

    public ApprovedListAdapter(ArrayList<BookVaccine> list) {
        this.list = list;
    }

    @Override
    public ApprovedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.approved_item, parent, false);

        return new ApprovedViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ApprovedViewHolder holder, final int position) {
        BookVaccine bookVaccine = list.get(position);

        holder.tvname_ap.setText(bookVaccine.getVaccine_name());
        holder.tvdate_ap.setText(bookVaccine.getDue_date());
        holder.price_ap.setText(bookVaccine.getPrice());
        holder.confirm.setText(bookVaccine.getStatus());

        holder.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                getItemPostionOnClick.getItemPostionOnClick(view,postion,0);
            }
        });

    }

    public void setGetItemPostionOnClick(GetItemPostionOnClick getItemPostionOnClick) {
        this.getItemPostionOnClick = getItemPostionOnClick;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ApprovedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private OnItemClickListener onItemClickListener;

        public TextView tvname_ap,tvdate_ap,price_ap,confirm;

        public ApprovedViewHolder(View itemView) {
            super(itemView);
            tvname_ap = (TextView) itemView.findViewById(R.id.tvname_ap);
            tvdate_ap = (TextView) itemView.findViewById(R.id.tvdate_ap);
            price_ap = (TextView) itemView.findViewById(R.id.price_ap);
            confirm = (TextView) itemView.findViewById(R.id.confirm);
            itemView.setOnClickListener(this);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.onItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View view) {
            if(onItemClickListener!=null){
                onItemClickListener.onItemClick(view,getAdapterPosition());
            }
        }
    }
}
