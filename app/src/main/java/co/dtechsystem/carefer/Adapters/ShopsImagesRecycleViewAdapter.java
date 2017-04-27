package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import co.dtechsystem.carefer.Models.ShopsDetailsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.OnSwipeTouchListener;


public class ShopsImagesRecycleViewAdapter extends RecyclerView.Adapter<ShopsImagesRecycleViewAdapter.ViewHolder> {
    private List<ShopsDetailsModel.ShopsImagessRecord> _ShopsImagesDetails;
    private int lastPosition;
    private Activity activity;
    private String ShopID;
    private ImageView iv_full_image;
    private LinearLayout lay_full_image, lay_shop_details, lay_builts_images;


    public ShopsImagesRecycleViewAdapter(Activity activity,
                                         List<ShopsDetailsModel.ShopsImagessRecord> _ShopsImagesDetails, String ShopID
            , ImageView iv_full_image, LinearLayout lay_full_image, LinearLayout lay_shop_details, LinearLayout lay_builts_images) {
        this._ShopsImagesDetails = _ShopsImagesDetails;
        this.activity = activity;
        this.ShopID = ShopID;
        this.iv_full_image = iv_full_image;
        this.lay_full_image = lay_full_image;
        this.lay_shop_details = lay_shop_details;
        this.lay_builts_images = lay_builts_images;
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
        setAnimation(holder.itemView, position);
        if (_ShopsImagesDetails.get(position).getImageName() != null) {
            holder.pg_image_load.setVisibility(View.VISIBLE);
            Glide.with(activity).load(AppConfig.BaseUrlImages + "shop-" + ShopID + "/" + _ShopsImagesDetails.get(position)
                    .getImageName())
                    .override((int) activity.getResources().getDimension(R.dimen._100sdp), (int) activity.getResources().getDimension(R.dimen._100sdp))
//                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
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

            final String Url = AppConfig.BaseUrlImages + "shop-" + ShopID + "/";
            final int[] Clickedposition = new int[1];
            holder.iv_shop_image_details.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /** Initiate Popup view **/
                    lay_full_image.setVisibility(View.VISIBLE);
                    lay_shop_details.setVisibility(View.GONE);
                    Glide.with(activity).load(Url + _ShopsImagesDetails.get(position)
                            .getImageName()).into(iv_full_image);
                    Clickedposition[0] = position;
                    setBuiltColor(Clickedposition[0]);
                }
            });
            final int[] counter = {Clickedposition[0]};
            iv_full_image.setOnTouchListener(new OnSwipeTouchListener(activity) {

                int bckto;

                public void onSwipeRight() {
                    lay_full_image.setVisibility(View.VISIBLE);
                    lay_shop_details.setVisibility(View.GONE);
                    if (counter[0] < _ShopsImagesDetails.size()) {
                        String UrlAfter = Url + _ShopsImagesDetails.get(counter[0]++).getImageName();
                        setBuiltColor(counter[0]);
                        Glide.with(activity).load(UrlAfter).into(iv_full_image);
                        if (_ShopsImagesDetails.size() == counter[0]) {
                            counter[0]--;
                            bckto = counter[0];
                        }
                    }
                }

                public void onSwipeLeft() {
                    if (counter[0] < _ShopsImagesDetails.size()) {
                        if (counter[0] > -1) {
                            lay_full_image.setVisibility(View.VISIBLE);
                            lay_shop_details.setVisibility(View.GONE);
                            if (bckto == counter[0]) {
                                String UrlAfter = Url + _ShopsImagesDetails.get(counter[0]--).getImageName();
                                setBuiltColor(counter[0]);
                                Glide.with(activity).load(UrlAfter).into(iv_full_image);
                            } else {
                                String UrlAfter = Url + _ShopsImagesDetails.get(counter[0]--).getImageName();
                                setBuiltColor(counter[0]);
                                Glide.with(activity).load(UrlAfter).into(iv_full_image);
                            }
                            if (counter[0] == -1) {
                                counter[0] = 0;
                            }
                        }
                    }
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
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public void setBuiltColor(int selectedPosition) {

        if (_ShopsImagesDetails != null && _ShopsImagesDetails.size() > 0) {
            lay_builts_images.removeAllViews();
            for (int i = 0; i < _ShopsImagesDetails.size(); i++) {
                ImageView myButton = new ImageView(activity);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(20, 20);
                params.setMargins(10, 10, 10, 10);
                myButton.setLayoutParams(params);
                if (selectedPosition==i) {
                    myButton.setBackgroundResource(R.drawable.dr_round_about_us);
                }
                else {
                    myButton.setBackgroundResource(R.drawable.dr_round_icon);
                }
                lay_builts_images.addView(myButton);
            }
        }
    }

}