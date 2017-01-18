package com.start_up.dev.apilinkus.Adapter;

import android.content.Context;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.start_up.dev.apilinkus.Fragments.ProfileFragment;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Album;
import com.start_up.dev.apilinkus.Model.AlbumTestModel;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vignesh on 1/11/2017.
 * AlbumsAdapter is a subclass of RecyclerView and is responsible for providing views that represent items in a data set.
 */
public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder> {

    private Context mContext;
    private ArrayList<Album> albumList;
    private RecyclerViewClickListener itemListener;
    protected static final String TAG = AlbumsAdapter.class.getSimpleName();
    private String MODE_AUTH;
    public static String access_token;
    public static String token_type;
    public static String refresh_token;
    private TextView profileTextView;




    public class AlbumViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView title, count;
        public ImageView thumbnail, overflow;

        public AlbumViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            count = (TextView) itemView.findViewById(R.id.countRelatives);
            thumbnail = (ImageView) itemView.findViewById(R.id.thumbnail);
            thumbnail.setOnClickListener(this);
            overflow = (ImageView) itemView.findViewById(R.id.overflow);
        }


        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view,this.getAdapterPosition());
        }
    }

    /*Listener qui sera implémenté dans la classe ProfileActivity*/
    public interface ClickListener{
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }

    public AlbumsAdapter(Context mContext, ArrayList<Album> albumList,RecyclerViewClickListener itemListener) {
        this.mContext = mContext;
        this.albumList = albumList;
        this.itemListener = itemListener;
    }

    @Override
    public AlbumViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.content_albums,parent,false);
        return new AlbumViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AlbumViewHolder holder, int position) {
        Album album = albumList.get(position);
        Log.d(TAG,"Album name " + album.getName());
        holder.title.setText(album.getName());
        holder.count.setText(album.getMoments().size() + " moments");
        // loading album cover using Glide library
        Glide
                .with(mContext)
                .load(album.getThumbnail())
                .thumbnail(0.1f)
                .into(holder.thumbnail);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
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

    /**
     * Click listener for popup menu items
     */
    public class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {}

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_comment:
                    Toast.makeText(mContext, "Leave a comment", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_share:
                    Toast.makeText(mContext, "Share", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    // A retirer
    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        AlbumsAdapter.ClickListener clickListener;
        // Detects various gestures and events using the supplied MotionEvents.
        GestureDetector dectector;
        public RecyclerTouchListener(Context context, final RecyclerView recyclerView,final AlbumsAdapter.ClickListener clickListener){
            this.clickListener = clickListener;
            this.dectector = new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(),e.getY());
                    if(child != null && clickListener != null){
                        clickListener.onLongClick(child,recyclerView.getChildAdapterPosition(child));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    Log.d(AlbumsAdapter.class.getSimpleName(),"From onSingleTapUp");
                    return true;
                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && dectector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public int getItemCount() {
        return albumList.size();
    }
}
