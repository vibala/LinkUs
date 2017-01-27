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

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>{
    private List<RecyclerViewItem> listViewItem;
    private Context context;
    private RecyclerViewGalleryClickListener itemListener;
    private String type;

    public GalleryAdapter(RecyclerViewGalleryClickListener activity, ArrayList<RecyclerViewItem> listViewItem, String type) {
        this.context = (Activity)activity;
        this.listViewItem = listViewItem;
        this.itemListener=activity;
        this.type=type;
    }

    @Override
    public GalleryAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view=null;
        if(type.equals("current"))
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_gallery_list, viewGroup, false);

        else if(type.equals("selected"))
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_gallery_selected_list, viewGroup, false);

        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        if(listViewItem.size()>0) {
            RecyclerViewItem example;

            example = listViewItem.get(listViewItem.size() - 1 - position);

            int width = context.getResources().getDisplayMetrics().widthPixels;
            //On commence par la derniere image du repertoire (last date)

            Picasso.with(context).load(example.getFile()).centerCrop().resize(width / 2, width / 2).into(holder.img_android);
            holder.checkbox.setChecked(example.isChecked());
            holder.path.setText(example.getId());
        }
    }

    @Override
    public int getItemCount() {
        return listViewItem.size();
    }


    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView img_android;
        CheckBox checkbox;
        TextView path;
        public GalleryViewHolder(View view) {
            super(view);
            path=(TextView)view.findViewById(R.id.gallery_list_path);
            checkbox = (CheckBox)view.findViewById(R.id.gallery_list_check);
            LinearLayout cardView=(LinearLayout)view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
            img_android = (ImageView)view.findViewById(R.id.gallery_list_img);
        }

        @Override
        public void onClick(View view) {
            if(checkbox.isChecked())
                checkbox.setChecked(false);
            else
                checkbox.setChecked(true);
            itemListener.recyclerViewGalleryListClicked(checkbox,path.getText().toString());
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