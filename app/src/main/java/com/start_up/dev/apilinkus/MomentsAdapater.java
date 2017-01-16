package com.start_up.dev.apilinkus;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.start_up.dev.apilinkus.Listener.RecyclerViewClickListener;
import com.start_up.dev.apilinkus.Model.AlbumTestModel;
import com.start_up.dev.apilinkus.Model.MomentTestModel;

import java.util.ArrayList;

/**
 * Created by Vignesh on 1/13/2017.
 */

public class MomentsAdapater extends RecyclerView.Adapter<MomentsAdapater.MomentViewHolder> {

    private Context mContext;
    private ArrayList<MomentTestModel> momentList;
    private String TAG = MomentsAdapater.class.getSimpleName();
    private RecyclerViewClickListener itemListener;

    public MomentsAdapater(Context context, ArrayList<MomentTestModel> momentList, RecyclerViewClickListener itemListener){
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
        MomentTestModel moment = momentList.get(position);
        Log.i(TAG,moment.getMomentName());
        holder.moment_title.setText(moment.getMomentName());
        /* TODO A REMPLACER defaultSliderView par  TextSliderView */
        for (AlbumTestModel album: moment.getListOfInstants()) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(mContext);
            defaultSliderView.image(album.getThumbnail());
            holder.sliderShow.addSlider(defaultSliderView);
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
