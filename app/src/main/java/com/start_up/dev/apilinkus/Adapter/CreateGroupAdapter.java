package com.start_up.dev.apilinkus.Adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.start_up.dev.apilinkus.Listener.RecyclerViewCircleClickListener;
import com.start_up.dev.apilinkus.R;

import java.util.List;

/**
 * Created by Huong on 17/01/2017.
 */

public class CreateGroupAdapter extends RecyclerView.Adapter<CreateGroupAdapter.CircleViewHolder>{
    private List<RecyclerViewItem> listViewItem;
    private Context context;
    private RecyclerViewCircleClickListener itemListener;

    public CreateGroupAdapter(Context context, List<RecyclerViewItem> listViewItem, RecyclerViewCircleClickListener itemListener){
        this.context = context;
        this.listViewItem = listViewItem;
        this.itemListener = itemListener;
    }

    public class CircleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        com.mikhaellopez.circularimageview.CircularImageView imagePost;
        TextView name;
        TextView id;
        TextView type;
        CheckBox checkbox;
        public CircleViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.friend_profile_name);
            imagePost = (com.mikhaellopez.circularimageview.CircularImageView) itemView.findViewById(R.id.friend_profile_picture);
            id=(TextView) itemView.findViewById(R.id.friend_profile_id);
            type=(TextView) itemView.findViewById(R.id.friend_profile_type);

            CardView cardView=(CardView)itemView.findViewById(R.id.card_view);
            checkbox = (CheckBox)itemView.findViewById(R.id.check_list_item);
            cardView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if(checkbox.isChecked())
                checkbox.setChecked(false);
            else
                checkbox.setChecked(true);

            itemListener.recyclerViewCircleListClicked(checkbox,id.getText().toString());
        }
    }

    @Override
    public CircleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.cardview_create_group,parent,false);
        return new CreateGroupAdapter.CircleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CircleViewHolder holder, int position) {
        if(listViewItem.size()>0) {
            RecyclerViewItem example = listViewItem.get(position);
            // A implementer avec ce qui a été fait ds le cas réeal avec description, classe comment,etc.
            // loading album cover using Glide library
            if (holder.name != null)
                holder.name.setText(example.getName());
            holder.id.setText(example.getId());
            holder.type.setText(example.getType());
            Glide
                    .with(context)
                    .load(example.getUrl())
                    .into(holder.imagePost);

            holder.checkbox.setVisibility(View.VISIBLE);
            holder.checkbox.setChecked(example.isChecked());
        }

    }


    @Override
    public int getItemCount() {
        return listViewItem.size();
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
