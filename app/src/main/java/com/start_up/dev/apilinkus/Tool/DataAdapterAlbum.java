package com.start_up.dev.apilinkus.Tool;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.start_up.dev.apilinkus.AlbumActivityIntentMomentDetailed;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

public class DataAdapterAlbum extends RecyclerView.Adapter<DataAdapterAlbum.AlbumViewHolder> {
    private ArrayList<GridItem> gridItems;
    private Context context;

    public DataAdapterAlbum(Context context, ArrayList<GridItem> gridItems) {
        this.context = context;
        this.gridItems = gridItems;

    }
    /**
     * Updates grid data and refresh grid items.
     *
     * @param gridItems
     */
    public void setGridData(ArrayList<GridItem> gridItems) {
        this.gridItems = gridItems;
        notifyDataSetChanged();
    }
    @Override
    public DataAdapterAlbum.AlbumViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.grid_item_album, viewGroup, false);
        return new AlbumViewHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumViewHolder viewHolder, int i) {

        GridItem item =gridItems.get(i);
        viewHolder.setItem(item);
        if(viewHolder.tv_android!=null && viewHolder.img_android!=null) {
            System.out.println(item.getTitle());
            viewHolder.tv_android.setText(item.getTitle());
            int width = context.getResources().getDisplayMetrics().widthPixels;

            Picasso.with(context).load(item.getImage()).centerCrop().resize(width / 2, width / 2).into(viewHolder.img_android);
        }
    }

    @Override
    public int getItemCount() {
        return gridItems.size();
    }

    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        TextView tv_android;
        ImageView img_android;
        Context context;

        GridItem item;
        public void setItem(GridItem item){
            this.item = item;
        }
        public AlbumViewHolder(View itemView) {
            super(itemView);
            context = itemView.getContext();
            this.tv_android = (TextView) itemView.findViewById(R.id.album_txt);
            this.img_android = (ImageView) itemView.findViewById(R.id.album_img_moment);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Get item at position
                    Intent intent = new Intent(context, AlbumActivityIntentMomentDetailed.class);
                    intent.addFlags(FLAG_ACTIVITY_NEW_TASK);

                    // Interesting data to pass across are the thumbnail size/location, the
                    // resourceId of the source bitmap, the picture description, and the
                    // orientation (to avoid returning back to an obsolete configuration if
                    // the device rotates again in the meantime)

                    int[] screenLocation = new int[2];
                    img_android.getLocationOnScreen(screenLocation);

                    //Pass the image title and url to DetailsActivity
                    intent.putExtra("left", screenLocation[0]).
                            putExtra("top", screenLocation[1]).
                            putExtra("width", img_android.getWidth()).
                            putExtra("height", img_android.getHeight()).
                            putExtra("title", item.getTitle()).
                            putExtra("image", item.getImage());

                    //Start details activity
                    context.startActivity(intent);
                }

            });

        }
    }
}