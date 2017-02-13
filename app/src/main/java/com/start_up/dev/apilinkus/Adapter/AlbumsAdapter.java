package com.start_up.dev.apilinkus.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.start_up.dev.apilinkus.Fragments.OwnedAlbumsFragment;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.R;
import com.start_up.dev.apilinkus.TimeLine.TimeLineActivity;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/11/2017.
 * AlbumsAdapter is a subclass of RecyclerView and is responsible for providing views that represent items in a data set.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {

    private Context mContext;
    private ArrayList<Album> albumList;
    private RecyclerViewClickListener itemListener;
    protected static final String TAG = AlbumsAdapter.class.getSimpleName();
    private ClickListener mCallback;
    private OwnedAlbumsFragment.OnOwnedAlbumSelectedListener listenerTL;
    private int selected_album_position;

    public class AlbumViewHolder extends RecyclerView.ViewHolder{
        public TextView title, count;
        public ImageView thumbnail, overflow;
        public Button timeline;


        public AlbumViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.countRelatives);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            thumbnail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemListener.recyclerViewListClicked(view,getAdapterPosition());
                }
            });
            overflow = (ImageView) itemView.findViewById(R.id.image_view_overflow);
            timeline = (Button) itemView.findViewById(R.id.timeline_activity_button_tl);

        }


    }

    /*Listener qui sera implémenté dans la classe ProfileActivity*/
    public interface ClickListener{
        void OnShareOwnedAlbumListener(int position,String scope);
    }

    public AlbumsAdapter(Context mContext, ArrayList<Album> albumList,RecyclerViewClickListener itemListener,ClickListener listener,OwnedAlbumsFragment.OnOwnedAlbumSelectedListener listenerTL) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.itemListener = itemListener;
        this.mCallback = listener;
        this.listenerTL=listenerTL;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.content_albums,parent,false);
        return new AlbumViewHolder(itemView);
    }
private  Album albumOnlyGetTmp;
    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, int position) {
        if(albumList.size()>0) {
            albumOnlyGetTmp = albumList.get(position);
            Log.d(TAG, "Album name " + albumOnlyGetTmp.getName());
            holder.title.setText(albumOnlyGetTmp.getName());
            holder.count.setText(albumOnlyGetTmp.getCountryName());
            // loading album cover using Glide library
            Glide
                    .with(mContext)
                    .load(albumOnlyGetTmp.getImageUrl())
                    .thumbnail(0.1f)
                    .into(holder.thumbnail);

            holder.overflow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    selected_album_position = holder.getAdapterPosition();
                    showPopupMenu(holder.overflow);
                }
            });
            if(listenerTL!=null) {
                holder.timeline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startTimeLine(albumOnlyGetTmp);
                    }
                });
            }
        }
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    public void showPopupMenu(View view){
        PopupMenu popupMenu = new PopupMenu(mContext,view);
        MenuInflater inflater = popupMenu.getMenuInflater();
        inflater.inflate(R.menu.menu_album, popupMenu.getMenu());
        popupMenu.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popupMenu.show();
    }
    public void startTimeLine(Album album){
        listenerTL.onOwnedTimeLineAlbumSelected(album.getId());
    }
    /**
     * Click listener for popup menu items
     */
    public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {}

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_share_gpf:
                    Log.d(TAG,"Share this album with group of friends " + selected_album_position);
                    mCallback.OnShareOwnedAlbumListener(selected_album_position,"friendGroup");
                    return true;
                case R.id.action_share_f:
                    Log.d(TAG,"Share this album with friends " + selected_album_position);
                    mCallback.OnShareOwnedAlbumListener(selected_album_position,"friends");
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
