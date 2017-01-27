package com.start_up.dev.apilinkus.Adapter;


import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.start_up.dev.apilinkus.Listener.RecyclerViewGalleryFolderClickListener;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;
import java.util.List;

public class GalleryFolderAdapter extends RecyclerView.Adapter<GalleryFolderAdapter.GalleryViewHolder>{
    private List<RecyclerViewFolderItem> listViewItem;
    private Context context;
    private Activity activity;

    public GalleryFolderAdapter(Activity context, ArrayList<RecyclerViewFolderItem> listViewItem) {
        this.context = context;
        this.listViewItem = listViewItem;
        this.activity=context;
    }

    @Override
    public GalleryFolderAdapter.GalleryViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cardview_gallery_folder_list, viewGroup, false);
        return new GalleryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GalleryViewHolder holder, int position) {
        if(listViewItem.size()>0) {
            RecyclerViewFolderItem example = listViewItem.get(listViewItem.size() - 1 - position);
            holder.absolutePath = example.getPath();
            String[] names = example.getPath().split("/");
            holder.path.setText((names.length > 1 ? names[names.length - 1] : names[0]));
        }
    }

    @Override
    public int getItemCount() {
        return listViewItem.size();
    }


    public class GalleryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView path;
        String absolutePath;
        public GalleryViewHolder(View view) {
            super(view);
            path=(TextView)view.findViewById(R.id.gallery_folder_path);
            CardView cardView=(CardView) view.findViewById(R.id.card_view);
            cardView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            ((RecyclerViewGalleryFolderClickListener)activity).recyclerViewFolderListClicked(view,absolutePath);
                  }
    }
    /**
     * Updates grid data and refresh grid items.
     *
     * @param listViewItem
     */
    public void setGridData(List<RecyclerViewFolderItem> listViewItem) {
        this.listViewItem = listViewItem;
        notifyDataSetChanged();
    }
}