package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.util.List;

import co.dtechsystem.carefer.Models.ShopsDetailsModel;
import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.ShopDetailsActivity;
import co.dtechsystem.carefer.Utils.AppConfig;


public class ShopsImagesRecycleViewAdapter extends RecyclerView.Adapter<ShopsImagesRecycleViewAdapter.ViewHolder> {
    private List<ShopsDetailsModel.ShopsImagessRecord> _ShopsImagesDetails;
    private int lastPosition;
    private Activity activity;
    private String ShopID;

    public ShopsImagesRecycleViewAdapter(Activity activity, List<ShopsDetailsModel.ShopsImagessRecord> _ShopsImagesDetails, String ShopID) {
        this._ShopsImagesDetails = _ShopsImagesDetails;
        this.activity = activity;
        this.ShopID = ShopID;
    }

    @Override
    public ShopsImagesRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                       int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shops_details, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ShopsImagesRecycleViewAdapter.ViewHolder vh = new ShopsImagesRecycleViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ShopsImagesRecycleViewAdapter.ViewHolder holder, final int position) {
//        holder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        if (_ShopsImagesDetails.get(position).getImageName() != null) {
            holder.pg_image_load.setVisibility(View.VISIBLE);
            Glide.with(activity).load(AppConfig.BaseUrlImages + "shop-" + ShopID + "/" + _ShopsImagesDetails.get(position)
                    .getImageName())
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            holder.pg_image_load.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.pg_image_load.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.iv_shop_image_details);
            final ImagePopup imagePopup = new ImagePopup(activity);
            int width = activity.getResources().getDimensionPixelSize(R.dimen._300sdp);
            int height = activity.getResources().getDimensionPixelSize(R.dimen._300sdp);
            imagePopup.setBackgroundColor(activity.getResources().getColor(R.color.colorAccent));
//            imagePopup.setBackground(activity.getResources().getDrawable(R.drawable.dr_corner_fill));
            imagePopup.setWindowWidth(width);
            imagePopup.setWindowHeight(height);
            imagePopup.setHideCloseIcon(true);
            imagePopup.setImageOnClickClose(true);
            holder.iv_shop_image_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** Initiate Popup view **/
                    imagePopup.initiatePopup(holder.iv_shop_image_details.getDrawable());
                }
            });
        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView iv_shop_image_details;
        public ProgressBar pg_image_load;

        public ViewHolder(View v) {

            super(v);
            pg_image_load = (ProgressBar) v.findViewById(R.id.pg_image_load);
            iv_shop_image_details = (ImageView) v.findViewById(R.id.iv_shop_image_details);
        }

    }

    @Override
    public int getItemCount() {
        return _ShopsImagesDetails.size();
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