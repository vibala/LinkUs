package com.start_up.dev.apilinkus.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.start_up.dev.apilinkus.HomeActivity;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.Instant;
import com.start_up.dev.apilinkus.Model.Moment;
import com.start_up.dev.apilinkus.R;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/13/2017.
 */

public class MomentsAdapater extends RecyclerView.Adapter<MomentsAdapater.MomentViewHolder> {

    private Context mContext;
    private ArrayList<Moment> momentList;
    private String TAG = MomentsAdapater.class.getSimpleName();
    private RecyclerViewClickListener itemListener;

    public MomentsAdapater(Context context, ArrayList<Moment> momentList, RecyclerViewClickListener itemListener){
        this.mContext = context;
        this.momentList = momentList;
        this.itemListener = itemListener;
    }

    @Override
    public MomentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater
                            .from(parent.getContext())
                            .inflate(R.layout.moment_item_display_layout,parent,false);

        return new MomentViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MomentViewHolder holder, int position) {
        if(momentList.size()>0) {
            Moment moment = momentList.get(position);
            Log.i(TAG, moment.getName());
            holder.moment_title.setText(moment.getName());
            if(moment.getInstantList() != null && !moment.getInstantList().isEmpty()){
                for (Instant instant : moment.getInstantList()) {
                    TextSliderView textSliderView = new TextSliderView(mContext);
                    //#DEPENDANCE TextSliderView ne gere pas le jpeg le jpg fonctionne , les autres pas testé
                    System.out.println("HEINNNNNNNNNNNNNN3 "+instant.getUrl()+"&userId="+ HomeActivity.userId);
                    textSliderView.image(instant.getUrl()+"&userId="+ HomeActivity.userId);
                    //textSliderView.image(R.drawable.album1);
                    textSliderView.description(instant.getName() + " - " + instant.getPublishDate().toString()); // On récupère uniquement la description pour l'instant
                    holder.sliderShow.addSlider(textSliderView);
                }
            }
        }

    }


    @Override
    public int getItemCount() {
        return momentList.size();
    }

    public class MomentViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView moment_title;
        public SliderLayout sliderShow;
        public ImageView expandImage;

        public MomentViewHolder(View itemView) {
            super(itemView);
            this.moment_title = (TextView) itemView.findViewById(R.id.moment_title);
            this.sliderShow = (SliderLayout) itemView.findViewById(R.id.moment_slider);
            this.expandImage = (ImageView) itemView.findViewById(R.id.expand_image);
            this.expandImage.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            itemListener.recyclerViewListClicked(view,this.getAdapterPosition());
        }
    }


}
