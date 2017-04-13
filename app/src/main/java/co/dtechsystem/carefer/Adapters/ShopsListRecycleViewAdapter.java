package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.androidquery.AQuery;

import java.util.List;

import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.ShopDetailsActivity;


public class ShopsListRecycleViewAdapter extends RecyclerView.Adapter<ShopsListRecycleViewAdapter.ViewHolder> {
    private List<ShopsListModel.ShopslistRecord> _ShopslistRecordList;
    private int lastPosition;
    Activity activity;

    public ShopsListRecycleViewAdapter(Activity activity, List<ShopsListModel.ShopslistRecord> _ShopslistRecordList) {
        this._ShopslistRecordList = _ShopslistRecordList;
        this.activity = activity;
    }

    @Override
    public ShopsListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shops, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ShopsListRecycleViewAdapter.ViewHolder vh = new ShopsListRecycleViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ShopsListRecycleViewAdapter.ViewHolder holder, final int position) {
//        List<String> serviceType = Arrays.asList(_ShopslistRecordList.get(position).getServiceType().split(","));
        String serviceType = _ShopslistRecordList.get(position).getServiceType().replaceAll(",", "\u2022");
        holder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        holder.rb_shop_shop_list.setRating((Float.parseFloat(_ShopslistRecordList.get(position).getShopRating())));
        holder.tv_service_type_shop_list.setText(serviceType);
        holder.tv_desc_shop_list.setText(_ShopslistRecordList.get(position).getShopDescription());
        holder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_shop_name_shop_list, tv_service_type_shop_list, tv_desc_shop_list;
        public RatingBar rb_shop_shop_list;

        public ViewHolder(View v) {

            super(v);
            tv_shop_name_shop_list = (TextView) v.findViewById(R.id.tv_shop_name_shop_list);
            tv_service_type_shop_list = (TextView) v.findViewById(R.id.tv_service_type_shop_list);
            tv_desc_shop_list = (TextView) v.findViewById(R.id.tv_desc_shop_list);
            rb_shop_shop_list = (RatingBar) v.findViewById(R.id.rb_shop_shop_list);


        }

    }

    @Override
    public int getItemCount() {
        return _ShopslistRecordList.size();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}