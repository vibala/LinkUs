package com.start_up.dev.apilinkus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.Service.DateUtil;

import java.util.List;

/**
 * Created by Vignesh on 1/17/2017.
 */

public class HomeAdapter extends RecyclerView.Adapter<HomeAdapter.HomeViewHolder> {

    private List<Instant> instants;
    private Context context;
    private RecyclerViewClickListener itemListener;

    public HomeAdapter(Context context, List<Instant> instants,RecyclerViewClickListener itemListener){
        this.context = context;
        this.instants = instants;
        this.itemListener = itemListener;
    }

    public class HomeViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public ImageView imagePost;
        public TextView number_of_likes,number_of_comments,number_of_shares,time_elapsed;
        public LinearLayout likes_layout,comments_layout,shares_layout;

        public HomeViewHolder(View itemView){
            super(itemView);
            imagePost = (ImageView) itemView.findViewById(R.id.card_view_home_body_post);
            number_of_likes = (TextView) itemView.findViewById(R.id.number_of_likes);
            number_of_comments = (TextView) itemView.findViewById(R.id.number_of_comments);
            number_of_shares = (TextView) itemView.findViewById(R.id.number_of_shares);
            time_elapsed = (TextView) itemView.findViewById(R.id.card_view_home_timeelapsed);
            /*layout def*/
            likes_layout = (LinearLayout) itemView.findViewById(R.id.likes_layout);
            likes_layout.setClickable(true);
            comments_layout = (LinearLayout) itemView.findViewById(R.id.comments_layout);
            comments_layout.setClickable(true);
            shares_layout = (LinearLayout) itemView.findViewById(R.id.shares_layout);
            comments_layout.setClickable(true);
        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view,getAdapterPosition());
        }
    }

    @Override
    public HomeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.posts_home_page_layout,parent,false);
        return new HomeViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(HomeViewHolder holder, int position) {
        Instant instant = instants.get(position);
        Long[] time_elapsed_details = DateUtil.printDifference(instant.getPublishDate(),DateUtil.getCurrentDate());
        if(time_elapsed_details[0] > 0){
            if(time_elapsed_details[0] == 1) {
                holder.time_elapsed.setText(time_elapsed_details[0] + " day ago");
            }else{
                holder.time_elapsed.setText(time_elapsed_details[0] + " days ago");
            }
        }else{
            if(time_elapsed_details[1] > 0) {
                if(time_elapsed_details[1] == 1) {
                    holder.time_elapsed.setText(time_elapsed_details[1] + " hour ago");
                }else{
                    holder.time_elapsed.setText(time_elapsed_details[1] + " hours ago");
                }
            }else{
                if(time_elapsed_details[2] > 0) {
                    if(time_elapsed_details[2] == 1) {
                        holder.time_elapsed.setText(time_elapsed_details[2] + " minute ago");
                    }else{
                        holder.time_elapsed.setText(time_elapsed_details[2] + " minutes ago");
                    }
                }else{
                    if(time_elapsed_details[3] > 0) {
                        if (time_elapsed_details[3] == 1) {
                            holder.time_elapsed.setText(time_elapsed_details[3] + " second ago");
                        } else {
                            holder.time_elapsed.setText(time_elapsed_details[3] + " seconds ago");
                        }
                    }else if(time_elapsed_details[3] == 0){
                        holder.time_elapsed.setText("now");
                    }
                }

            }
        }

        // A implementer avec ce qui a été fait ds le cas réeal avec description, classe comment,etc.
        // loading album cover using Glide library
        Glide
                .with(context)
                .load(instant.getUrl())
                .into(holder.imagePost);

    }

    @Override
    public int getItemCount() {
        return instants.size();
    }
}
