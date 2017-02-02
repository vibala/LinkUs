package com.start_up.dev.apilinkus.Adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.start_up.dev.apilinkus.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Vignesh on 1/31/2017.
 */
public class NotifFriendRequestAdapter extends RecyclerView.Adapter<NotifFriendRequestAdapter.NotifFriendRequestViewHolder> {

    private HashMap<String,String> friendsFromPendingList;
    private OnClickListener mCallback;

    public NotifFriendRequestAdapter(HashMap<String,String> friendsFromPendingList,OnClickListener mCallback){
        this.friendsFromPendingList = friendsFromPendingList;
        this.mCallback = mCallback;
    }

    public interface OnClickListener{
        void AcceptPropositionListener(String friendId);
        void DeclinePropositionListener(String friendId);
    }


    @Override
    public NotifFriendRequestViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d("NFRQ","TEST");
        View itemView = LayoutInflater
                            .from(parent.getContext())
                                .inflate(R.layout.cardview_recyclerview_notif_friend_request,parent,false);
        return new NotifFriendRequestViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final NotifFriendRequestViewHolder holder, int position) {
        Log.d("NFRQ","TEST0");
        if(friendsFromPendingList != null && friendsFromPendingList.size() > 0){
            Log.d("NFRQ","TEST1");
            String user = (String) friendsFromPendingList.values().toArray()[holder.getAdapterPosition()];
            if(user!=null){
                Log.d("NFRQ","TEST2");
                holder.friend_profile_name.setText(user);
                holder.btn_accept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.AcceptPropositionListener((String)friendsFromPendingList.keySet().toArray()[holder.getAdapterPosition()]);
                    }
                });

                holder.btn_decline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCallback.DeclinePropositionListener((String)friendsFromPendingList.keySet().toArray()[holder.getAdapterPosition()]);
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return friendsFromPendingList.size();
    }

    public class NotifFriendRequestViewHolder extends RecyclerView.ViewHolder{

        public TextView friend_profile_name;
        public Button btn_accept, btn_decline;

        public NotifFriendRequestViewHolder(View itemView) {
            super(itemView);
            friend_profile_name = (TextView)itemView.findViewById(R.id.friend_profile_name);
            btn_accept = (Button) itemView.findViewById(R.id.circle_fragment_button_1);
            btn_decline = (Button) itemView.findViewById(R.id.circle_fragment_button_2);
        }
    }
}
