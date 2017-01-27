package com.start_up.dev.apilinkus.Adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.start_up.dev.apilinkus.Listener.RecyclerViewGalleryClickListener;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;
import java.util.List;

public class MiniCarouselAdapter extends RecyclerView.Adapter<MiniCarouselAdapter.GalleryViewHolder>{
    private List<RecyclerViewItem> listViewItem;
    private Context context;
    private RecyclerViewGalleryClickListener activity;
    private String type;

    public MiniCarouselAdapter(RecyclerViewGalleryClickListener context, ArrayList<RecyclerViewItem> listViewItem, String type) {
        this.context = (Activity) context;
        this.listViewItem = listViewItem;
        this.activity=context;
        this.type=type;
    }

    @Override
    public MiniCarouselAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=null;

        view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_gallery_selected_list, viewGroup, false);

        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        if(listViewItem.size()>0) {
            RecyclerViewItem example;

            example = listViewItem.get(position);

            int width = context.getResources().getDisplayMetrics().widthPixels;
            //On commence par la derniere image du repertoire (last date)

            if (type.equals("url")) {
                Picasso.with(context).load(example.getUrl()).centerCrop().resize(width / 2, width / 2).into(holder.img_android);
                holder.uri.setText(example.getUrl());
            }
            if (type.equals("file")) {
                Picasso.with(context).load(example.getFile()).centerCrop().resize(width / 2, width / 2).into(holder.img_android);
                holder.uri.setText(example.getId());
            }
        }

    }

    @Override
    public int getItemCount() {
        return listViewItem.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_android;
        TextView uri;
        public GalleryViewHolder(View view) {
            super(view);
            uri=(TextView)view.findViewById(R.id.gallery_list_path);
            CheckBox checkbox = (CheckBox)view.findViewById(R.id.gallery_list_check);
            checkbox.setVisibility(View.INVISIBLE);
            LinearLayout cardView=(LinearLayout) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
            img_android = (ImageView)view.findViewById(R.id.gallery_list_img);
        }

        @Override
        public void onClick(View view) {
            activity.recyclerViewGalleryListClicked(view,uri.getText().toString());
                  }
    }
    /**
     * Updates grid data and refresh grid items.
     *
     * @param listViewItem
     */
    public void setGridData(List<RecyclerViewItem> listViewItem) {
        this.listViewItem = listViewItem;
        notifyDataSetChanged();
    }
}