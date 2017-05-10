package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import co.dtechsystem.carefer.Models.FavouriteShopsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;


public class FavouriteShopsRecycleViewAdapter extends RecyclerView.Adapter<FavouriteShopsRecycleViewAdapter.ViewHolder> {
    private List<FavouriteShopsModel.FavouriteShopsRecord> _FavouriteShopsDetails;
    private int lastPosition;
    private Activity activity;

    public FavouriteShopsRecycleViewAdapter(Activity activity, List<FavouriteShopsModel.FavouriteShopsRecord> _FavouriteShopsDetail) {
        this._FavouriteShopsDetails = _FavouriteShopsDetail;
        this.activity = activity;
    }

    @Override
    public FavouriteShopsRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_fav_shops, parent, false);
        // set the view's size, margins, paddings and layout parameters
        FavouriteShopsRecycleViewAdapter.ViewHolder vh = new FavouriteShopsRecycleViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final FavouriteShopsRecycleViewAdapter.ViewHolder holder, final int position) {
        setAnimation(holder.itemView,position);
        holder.tv_fav_shop_name.setText(_FavouriteShopsDetails.get(position).getShopName());
        if (_FavouriteShopsDetails.get(position).getShopImage() != null) {
            Glide.with(activity).load(AppConfig.BaseUrlImages + "shop-" + _FavouriteShopsDetails.get(position).getID() + "/" + _FavouriteShopsDetails.get(position)
                    .getShopImage())
                    .override((int) activity.getResources().getDimension(R.dimen._100sdp), (int) activity.getResources().getDimension(R.dimen._100sdp))
//                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            return false;
                        }
                    })
                    .into(holder.iv_shop_fav);
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_fav_shop_name;
        public ImageView iv_shop_fav;

        public ViewHolder(View v) {

            super(v);
            tv_fav_shop_name = (TextView) v.findViewById(R.id.tv_fav_shop_name);
            iv_shop_fav=(ImageView)v.findViewById(R.id.iv_shop_fav);
        }

    }

    @Override
    public int getItemCount() {
        return _FavouriteShopsDetails.size();
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}