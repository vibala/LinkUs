package com.start_up.dev.apilinkus.Tool;


import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.start_up.dev.apilinkus.GalleryOldActivity;
import com.start_up.dev.apilinkus.R;

import java.io.File;
import java.util.ArrayList;

public class DataAdapterGallery extends RecyclerView.Adapter<DataAdapterGallery.GalleryViewHolder> {
    private ArrayList<File> files;
    private Context context;
    private GalleryOldActivity activity;
    private CardView cardView;
    public DataAdapterGallery(Context context, ArrayList<File> files) {
        this.context = context;
        this.files = files;
        this.activity=(GalleryOldActivity)context;
        System.out.println(activity);
    }

    @Override
    public DataAdapterGallery.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_gallery, viewGroup, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder viewHolder, int i) {
        int width= context.getResources().getDisplayMetrics().widthPixels;
        //On commence par la derniere image du repertoire (last date)
        Picasso.with(context).load(files.get(files.size()-1 - i)).centerCrop().resize(width/2,width/2).into(viewHolder.img_android);
        if(!activity.is_in_action_mode)
            viewHolder.checkbox.setVisibility(View.GONE);
        else {
            viewHolder.checkbox.setVisibility(View.VISIBLE);
            viewHolder.checkbox.setChecked(false);

        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_android;
        CheckBox checkbox;
        public GalleryViewHolder(View view) {
            super(view);

            checkbox = (CheckBox)view.findViewById(R.id.check_list_item);
            checkbox.setOnClickListener(this);
            img_android = (ImageView)view.findViewById(R.id.gallery_img);
            cardView=(CardView)view.findViewById(R.id.gallery_cardview);
            cardView.setOnLongClickListener(activity);
        }

        @Override
        public void onClick(View v) {
            activity.prepareSelection(v,files.size()-1 -getAdapterPosition());

        }
    }

    public void updateAdapter(ArrayList<File> list){
        System.out.println("a"+files);
        for(File file: list){
            files.remove(file);
            System.out.println("tttt"+file);
        }
        System.out.println("b"+files);
        notifyDataSetChanged();
    }
}