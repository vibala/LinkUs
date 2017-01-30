package com.start_up.dev.apilinkus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.start_up.dev.apilinkus.API.APILinkUS;
import com.start_up.dev.apilinkus.API.APIPostOneString_Observer;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Listener.RecyclerViewCircleClickListener;
import com.start_up.dev.apilinkus.R;

import java.util.List;

/**
 * Created by Huong on 17/01/2017.
 */

public class CircleAdapter extends RecyclerView.Adapter<CircleAdapter.CircleViewHolder>{
    private List<RecyclerViewItem> exampleList;
    private APIPostOneString_Observer obs;
    private Context context;
    private RecyclerViewCircleClickListener itemListener;

    public CircleAdapter(Context context,APIPostOneString_Observer obs, List<RecyclerViewItem> exampleList, RecyclerViewCircleClickListener itemListener){
        this.context = context;
        this.exampleList = exampleList;
        this.itemListener = itemListener;
        this.obs = obs;
    }

    public class CircleViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        com.mikhaellopez.circularimageview.CircularImageView imagePost;
        TextView name;
        Button button_1;
        Button button_2;
        TextView id;
        TextView type;

        public CircleViewHolder(View itemView){
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.friend_profile_name);
            imagePost = (com.mikhaellopez.circularimageview.CircularImageView) itemView.findViewById(R.id.friend_profile_picture);
            id=(TextView) itemView.findViewById(R.id.friend_profile_id);
            type=(TextView) itemView.findViewById(R.id.friend_profile_type);

            button_1=(Button) itemView.findViewById(R.id.circle_fragment_button_1);
            button_2=(Button) itemView.findViewById(R.id.circle_fragment_button_2);

        }

        public void update(){
            System.out.println("TYPE " +type.getText().toString());
            System.out.println("ID " +id.getText().toString());
            switch (type.getText().toString()){
                case "my_group_friend":
                    button_1.setText(R.string.remove_group_friend);
                    button_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //id.getText().toString() renvoit des guillement sur les extrémitées, il faut les enlever
                            new APILinkUS().removeGroupFriend(obs,id.getText().toString());
                            System.out.println("L'utilisateur a supprimé le groupe de proche: " + name.getText() +" id : "+id.getText().toString());

                        }
                    });
                    button_2.setVisibility(View.GONE);
                    /*button_2.setText(R.string.edit_group_friend);
                    button_2.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                        }
                    });*/
                    break;
                case "my_friend":
                    button_1.setText(R.string.remove_friend);
                    button_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            new APILinkUS().removeFriend(obs,id.getText().toString());
                            System.out.println("L'utilisateur a supprimé le proche : " + name.getText() +" id : "+id.getText().toString());
                        }
                    });
                    button_2.setVisibility(View.GONE);
                    break;
                case "search_friend":
                    button_1.setText(R.string.add_friend);
                    button_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("id.getText().toString() "+ id.getText().toString());
                            new APILinkUS().sendFriendRequest(obs,id.getText().toString());
                            System.out.println("L'utilisateur a envoyé une demande d'ami a la personne suivante : " + name.getText() +" id : "+id.getText().toString());
                        }
                    });
                    button_2.setVisibility(View.GONE);
                    break;
                case "my_friend_request_pending":
                    button_1.setText(R.string.pending_friend);
                    button_1.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            System.out.println("En attente d'une confirmation de la personne suivante : " + name.getText() +" id : "+id.getText().toString());
                            Toast.makeText(context.getApplicationContext(),"En attente d'une confirmation",Toast.LENGTH_SHORT).show();
                        }
                    });
                    button_2.setVisibility(View.GONE);
                    break;
                default:
            }
        }

        @Override
        public void onClick(View view) {
           // itemListener.recyclerViewCircleListClicked(view,getAdapterPosition());
        }
    }

    @Override
    public CircleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                .from(parent.getContext())
                .inflate(R.layout.cardview_friend,parent,false);
        return new CircleAdapter.CircleViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CircleViewHolder holder, int position) {
        //Il peut ne pas etre encore initialisé
        if(exampleList.size()>0) {
            RecyclerViewItem example = exampleList.get(position);
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

            holder.update();
        }
    }


    @Override
    public int getItemCount() {
        return exampleList.size();
    }

    /**
     * Updates grid data and refresh grid items.
     *
     * @param exampleList
     */
    public void setGridData(List<RecyclerViewItem> exampleList) {
        this.exampleList = exampleList;
        notifyDataSetChanged();
    }
}
