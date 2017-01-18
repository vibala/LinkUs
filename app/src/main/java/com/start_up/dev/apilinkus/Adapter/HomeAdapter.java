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
        public TextView number_of_likes,number_of_comments,number_of_shares;
        public LinearLayout likes_layout,comments_layout,shares_layout;

        public HomeViewHolder(View itemView){
            super(itemView);
            imagePost = (ImageView) itemView.findViewById(R.id.card_view_home_body_post);
            number_of_likes = (TextView) itemView.findViewById(R.id.number_of_likes);
            number_of_comments = (TextView) itemView.findViewById(R.id.number_of_comments);
            number_of_shares = (TextView) itemView.findViewById(R.id.number_of_shares);
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
