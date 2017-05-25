package co.dtechsystem.carefer.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.model.LatLng;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.ShopDetailsActivity;
import co.dtechsystem.carefer.Utils.AppConfig;


public class ShopsListRecycleViewAdapter extends RecyclerView.Adapter<ShopsListRecycleViewAdapter.ViewHolder> {
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordList;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordListFilter;
    private int lastPosition;
    @SuppressLint("StaticFieldLeak")
    private static Activity activity;
    @SuppressWarnings("unused")
    private Boolean expand;
    private final LatLng mLatlngCurrent;

    @SuppressWarnings({"unused", "Convert2Diamond"})
    public ShopsListRecycleViewAdapter(Activity activity, List<ShopsListModel.ShopslistRecord> _ShopslistRecordList,
                                       LatLng mLatlngCurrent) {
        ShopsListRecycleViewAdapter._ShopslistRecordList = _ShopslistRecordList;
        _ShopslistRecordListFilter = new ArrayList<ShopsListModel.ShopslistRecord>();
        _ShopslistRecordListFilter.addAll(_ShopslistRecordList);
        ShopsListRecycleViewAdapter.activity = activity;
        this.expand = false;
        this.mLatlngCurrent = mLatlngCurrent;
        setHasStableIds(true);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public ShopsListRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                     int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shops, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ShopsListRecycleViewAdapter.ViewHolder vh = new ShopsListRecycleViewAdapter.ViewHolder(v);
        return vh;
    }

    @SuppressWarnings("ConstantConditions")
    @SuppressLint({"RecyclerView", "SetTextI18n"})
    @Override
    public void onBindViewHolder(final ShopsListRecycleViewAdapter.ViewHolder holder, final int position) {
//        List<String> serviceType = Arrays.asList(_ShopslistRecordList.get(position).getServiceType().split(","));
//        String serviceType = _ShopslistRecordList.get(position).getServiceType().replaceAll(",", " \u2022");
        setAnimation(holder.itemView, position);
        String stringTypeArr[] = _ShopslistRecordList.get(position).getServiceType().split(",");
        if (stringTypeArr != null) {
            holder.tv_service_type_shop_list.setText(stringTypeArr[0]);
        }
        holder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        holder.rb_shop_shop_list.setRating((Float.parseFloat(_ShopslistRecordList.get(position).getShopRating())));
        holder.tv_desc_shop_list.setText(_ShopslistRecordList.get(position).getShopDescription());
        holder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        holder.btn_details_shops_list.setOnClickListener(new View.OnClickListener() {
            @SuppressWarnings("RedundantStringToString")
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(activity, ShopDetailsActivity.class);
                mIntent.putExtra("ShopID", _ShopslistRecordList.get(position).getID().toString());
                activity.startActivity(mIntent);
            }
        });
        holder.lay_details.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //noinspection UnusedAssignment
        final int height = holder.lay_details.getMeasuredHeight();
        holder.lay_expand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (expand) {
////                    holder.lay_shops_names.setBackgroundColor(activity.getResources().getColor(R.color.colorCreamDark));
//                    holder.iv_drop_shop_details.setBackground(activity.getResources().getDrawable(android.R.drawable.arrow_down_float));
//                    holder.iv_drop_details_shop_details.setBackground(activity.getResources().getDrawable(android.R.drawable.arrow_down_float));
//                    collapse(holder.lay_details);
//                    expand = false;
//                } else {
                //noinspection deprecation
                holder.iv_drop_shop_details.setBackground(activity.getResources().getDrawable(R.drawable.ic_arrow_up));
                //noinspection deprecation
                holder.iv_drop_details_shop_details.setBackground(activity.getResources().getDrawable(R.drawable.ic_arrow_up));
                holder.lay_shop_item.setVisibility(View.GONE);
//                    holder.lay_shops_names.setBackgroundColor(activity.getResources().getColor(R.color.colorLightBlue));
                expand = true;
                int i = (int) activity.getResources().getDimension(R.dimen._150sdp);
                expand(holder.lay_details, 1000, i, holder);

//                }
            }
        });
        holder.lay_collpase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (expand) {
//                    holder.lay_shops_names.setBackgroundColor(activity.getResources().getColor(R.color.colorCreamDark));
                //noinspection deprecation
                holder.iv_drop_shop_details.setBackground(activity.getResources().getDrawable(R.drawable.ic_arrow_down));
                //noinspection deprecation
                holder.iv_drop_details_shop_details.setBackground(activity.getResources().getDrawable(R.drawable.ic_arrow_down));
//                int i = (int) activity.getResources().getDimension(R.dimen._150sdp);
                collapse(holder.lay_details, holder);

//                holder.lay_details.setVisibility(View.GONE);
                expand = false;
//                } else {
//                    holder.iv_drop_shop_details.setBackground(activity.getResources().getDrawable(android.R.drawable.arrow_up_float));
//                    holder.iv_drop_details_shop_details.setBackground(activity.getResources().getDrawable(android.R.drawable.arrow_up_float));
//
////                    holder.lay_shops_names.setBackgroundColor(activity.getResources().getColor(R.color.colorLightBlue));
//                    expand = true;
//                    int i = (int) activity.getResources().getDimension(R.dimen._100sdp);
//                    mExpand(holder.lay_details);
//
//                }
            }
        });

        if (_ShopslistRecordList.get(position).getShopImage() != null) {
            holder.pg_image_load.setVisibility(View.VISIBLE);
            //noinspection deprecation
            Glide.with(activity).load(AppConfig.BaseUrlImages + "shop-" + _ShopslistRecordList.get(position).getID() + "/" + _ShopslistRecordList.get(position)
                    .getShopImage())
                    .override((int) activity.getResources().getDimension(R.dimen._120sdp), (int) activity.getResources().getDimension(R.dimen._120sdp))
                    .error(activity.getResources().getDrawable(R.drawable.ic_img_place_holder))
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
                    .into(holder.iv_item_top_shops_list);

        }
        //for large setting
        holder.tv_shop_name_large_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        holder.rb_shop_large__shop_list.setRating((Float.parseFloat(_ShopslistRecordList.get(position).getShopRating())));
        if (_ShopslistRecordList.get(position).getShopImage() != null) {
            holder.pg_image_load_large_shops.setVisibility(View.VISIBLE);
            //noinspection deprecation
            Glide.with(activity).load(AppConfig.BaseUrlImages + "shop-" + _ShopslistRecordList.get(position).getID() + "/" + _ShopslistRecordList.get(position)
                    .getShopImage())
                    .override((int) activity.getResources().getDimension(R.dimen._120sdp), (int) activity.getResources().getDimension(R.dimen._120sdp))
                    .error(activity.getResources().getDrawable(R.drawable.ic_img_place_holder))
//                    .placeholder(android.R.drawable.progress_indeterminate_horizontal)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            e.printStackTrace();
                            holder.pg_image_load_large_shops.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            holder.pg_image_load_large_shops.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(holder.iv_shop_detail_large_expand);
            try {

                if (mLatlngCurrent != null) {
                    Location curentLocation = new Location("");
                    curentLocation.setLatitude(mLatlngCurrent.latitude);
                    curentLocation.setLongitude(mLatlngCurrent.longitude);

                    Location destination = new Location("");
                    destination.setLatitude(Double.parseDouble(_ShopslistRecordList.get(position).getLatitude()));
                    destination.setLongitude(Double.parseDouble(_ShopslistRecordList.get(position).getLongitude()));

                    double distanceInMeters = curentLocation.distanceTo(destination) / 1000;
//                    DecimalFormat newFormat = new DecimalFormat("#####");
//                    double kmInDec = Float.valueOf(newFormat.format(distanceInMeters));
                    distanceInMeters = Math.round(distanceInMeters * 10) / 10.0d;
                    holder.tv_distance_item.setText(distanceInMeters + " km");
                    holder.tv_distance_details.setText(distanceInMeters + " km");
                }
            } catch (Exception e) {
                holder.tv_distance_item.setText("0 km");
                holder.tv_distance_details.setText("0 km");
                e.printStackTrace();
            }

        }
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_shop_name_shop_list;
        public final TextView tv_service_type_shop_list;
        public final TextView tv_desc_shop_list;
        public final TextView tv_shop_name_large_shop_list;
        public final TextView tv_distance_item;
        public final TextView tv_distance_details;
        public final RatingBar rb_shop_shop_list;
        public final RatingBar rb_shop_large__shop_list;
        final LinearLayout lay_shop_item;
        @SuppressWarnings("unused")
        final LinearLayout lay_shops_names;
        final LinearLayout lay_details;
        final LinearLayout lay_expand;
        final LinearLayout lay_collpase;
        @SuppressWarnings("unused")
        final ImageView iv_fav_shop_list;
        final ImageView iv_drop_shop_details;
        final ImageView iv_item_top_shops_list;
        final ImageView iv_drop_details_shop_details;
        final ImageView iv_shop_detail_large_expand;
        final Button btn_details_shops_list;
        public final ProgressBar pg_image_load;
        public final ProgressBar pg_image_load_large_shops;

        public ViewHolder(View v) {

            super(v);
            tv_shop_name_shop_list = (TextView) v.findViewById(R.id.tv_shop_name_shop_list);
            tv_service_type_shop_list = (TextView) v.findViewById(R.id.tv_service_type_shop_list);
            tv_desc_shop_list = (TextView) v.findViewById(R.id.tv_desc_shop_list);
            rb_shop_shop_list = (RatingBar) v.findViewById(R.id.rb_shop_shop_list);
            rb_shop_large__shop_list = (RatingBar) v.findViewById(R.id.rb_shop_large__shop_list);
            lay_shop_item = (LinearLayout) v.findViewById(R.id.lay_shop_item);
            iv_fav_shop_list = (ImageView) v.findViewById(R.id.iv_fav_shop_list);
            lay_shops_names = (LinearLayout) v.findViewById(R.id.lay_shops_names);
            lay_details = (LinearLayout) v.findViewById(R.id.lay_details);
            lay_expand = (LinearLayout) v.findViewById(R.id.lay_expand);
            lay_collpase = (LinearLayout) v.findViewById(R.id.lay_collapse);
            btn_details_shops_list = (Button) v.findViewById(R.id.btn_details_shops_list);
            iv_drop_shop_details = (ImageView) v.findViewById(R.id.iv_drop_shop_details);
            iv_drop_details_shop_details = (ImageView) v.findViewById(R.id.iv_drop_details_shop_details);
            iv_shop_detail_large_expand = (ImageView) v.findViewById(R.id.iv_shop_detail_large_expand);
            iv_item_top_shops_list = (ImageView) v.findViewById(R.id.iv_item_top_shops_list);
            pg_image_load = (ProgressBar) v.findViewById(R.id.pg_image_load);
            pg_image_load_large_shops = (ProgressBar) v.findViewById(R.id.pg_image_load_large_shops);
            tv_shop_name_large_shop_list = (TextView) v.findViewById(R.id.tv_shop_name_large_shop_list);
            tv_distance_item = (TextView) v.findViewById(R.id.tv_distance_item);
            tv_distance_details = (TextView) v.findViewById(R.id.tv_distance_details);

        }

    }

    @Override
    public int getItemCount() {
        return _ShopslistRecordList.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(_ShopslistRecordList.get(position).getID());
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    /**
     * Here is the key method to apply the animation
     */
    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(activity, android.R.anim.fade_in);
//            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @SuppressWarnings("unused")
    public static void mExpand(final View v) {
        v.measure(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? RecyclerView.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
//        a.setDuration((int)(targetHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(1000);
        v.startAnimation(a);
    }

    private static void collapse(final View v, final ShopsListRecycleViewAdapter.ViewHolder holder) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };
        a.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                holder.lay_shop_item.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        // 1dp/ms
//        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(1000);
        v.startAnimation(a);
    }

    @SuppressWarnings("SameParameterValue")
    private static void expand(final View v, int duration, int targetHeight,
                               final ShopsListRecycleViewAdapter.ViewHolder holder) {

        int prevHeight = v.getHeight();

        v.setVisibility(View.VISIBLE);
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                holder.lay_shop_item.setVisibility(View.GONE);
            }
        });
        valueAnimator.start();

    }

    @SuppressWarnings("unused")
    public static void collapse1(final View v, int duration, int targetHeight) {
        int prevHeight = v.getHeight();
        ValueAnimator valueAnimator = ValueAnimator.ofInt(prevHeight, targetHeight);
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                v.getLayoutParams().height = (int) animation.getAnimatedValue();
                v.requestLayout();
            }
        });
        valueAnimator.setInterpolator(new DecelerateInterpolator());
        valueAnimator.setDuration(duration);
        valueAnimator.start();
    }

    // Filter Class
    @SuppressWarnings("unused")
    public static void filterShops(String Type, String Both, String service, String Brand) {
        service = service.toLowerCase(Locale.getDefault());
        Brand = Brand.toLowerCase(Locale.getDefault());
        if (_ShopslistRecordList != null) {
            _ShopslistRecordList.clear();
            if (service.length() == 0 && Brand.length() == 0) {
                _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
            } else {
                if (Type.equals("Service") && Both.equals("No")) {
                    for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                        if (_ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(Locale.getDefault())
                                .contains(service)) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                        }
                    }
                } else if (Type.equals("Brand") && Both.equals("No")) {
                    for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                        if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(Locale.getDefault())
                                .contains(Brand)) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                        }
                    }
                } else if (Both.equals("Yes")) {
                    for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                        if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(Locale.getDefault())
                                .contains(service)) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));

                        } else if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(Locale.getDefault())
                                .contains(Brand)) {
                            _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));


                        }
                    }
                } else if (Type.equals("Default") && Both.equals("No")) {
                    _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
                }


            }
        }

    }

    // Filter for Shops Names Class
    @SuppressWarnings("unused")
    public static void filterShopsName(String Text) {
        Text = Text.toLowerCase(Locale.getDefault());
        if (_ShopslistRecordList != null) {
            _ShopslistRecordList.clear();
            if (Text.length() == 0) {
                _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
            } else {

                for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                    if (_ShopslistRecordListFilter.get(i).getShopName().toLowerCase(Locale.getDefault())
                            .contains(Text)) {
                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }
                if (_ShopslistRecordList.size() == 0) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
                }
            }

        }


    }

    // Filter Class
    @SuppressWarnings("unused")
    public static void filterShopsWithProviders(ArrayList<String> selectedItem, final String ProvideWarranty, final String ProvideReplacementParts,
                                                final String shopType, final String topRated) {
        if (_ShopslistRecordList != null) {
            if (selectedItem.size() > 0) {
                _ShopslistRecordList.clear();
                for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {

                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(Locale.getDefault())
                            .equals(shopType) || _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(Locale.getDefault())
                            .equals(ProvideReplacementParts) || _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(Locale.getDefault())
                            .equals(ProvideWarranty) || _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault())
                            .equals(topRated)) {
                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    } else if (selectedItem.size() == 4 && _ShopslistRecordListFilter.get(i).getShopType().toLowerCase(Locale.getDefault())
                            .equals(shopType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(Locale.getDefault())
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(Locale.getDefault())
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault())
                            .equals(topRated)) {
                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }
                if (_ShopslistRecordList.size() == 0) {
                    Toast.makeText(activity, activity.getResources().getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
                }
            } else {
                _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
            }

        }
    }

    //sorting Function
    public static void SortingShopsWithNameRating(ArrayList<String> selectedItem, final String ShopName, final String ShopRating) {
        if (_ShopslistRecordList != null) {
            if (selectedItem.size() > 0) {
                _ShopslistRecordList.clear();
                final List<ShopsListModel.ShopslistRecord> _ShopRating5 = new ArrayList<ShopsListModel.ShopslistRecord>();
                final List<ShopsListModel.ShopslistRecord> _ShopRating4 = new ArrayList<ShopsListModel.ShopslistRecord>();
                final List<ShopsListModel.ShopslistRecord> _ShopRating3 = new ArrayList<ShopsListModel.ShopslistRecord>();
                final List<ShopsListModel.ShopslistRecord> _ShopRating2 = new ArrayList<ShopsListModel.ShopslistRecord>();
                final List<ShopsListModel.ShopslistRecord> _ShopRating1 = new ArrayList<ShopsListModel.ShopslistRecord>();
                final List<ShopsListModel.ShopslistRecord> _ShopRating0 = new ArrayList<ShopsListModel.ShopslistRecord>();
                for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                    if (ShopRating.equals("5")) {
                        if (Float.parseFloat(_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault()))<=5.5) {
                            _ShopRating5.add(_ShopslistRecordListFilter.get(i));

                        }
                       else if (Float.parseFloat(_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault()))<=4.5) {
                            _ShopRating4.add(_ShopslistRecordListFilter.get(i));

                        }
                        else  if (Float.parseFloat(_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault()))<=3.5) {
                            _ShopRating3.add(_ShopslistRecordListFilter.get(i));

                        }
                        else if (Float.parseFloat(_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault()))<=2.5) {
                            _ShopRating2.add(_ShopslistRecordListFilter.get(i));

                        }
                        else if (Float.parseFloat(_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault()))<=1.5) {
                            _ShopRating1.add(_ShopslistRecordListFilter.get(i));

                        }
                        else if (Float.parseFloat(_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(Locale.getDefault()))<=0.5) {
                            _ShopRating0.add(_ShopslistRecordListFilter.get(i));
                        }
                    }
                    else {
                        _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
                        Collator arabicCollator = Collator.getInstance(new Locale("ar"));
                        Collections.sort(Arrays.asList(_ShopslistRecordList), arabicCollator);
                    }
                }
                _ShopslistRecordList.addAll(_ShopRating5);
                _ShopslistRecordList.addAll(_ShopRating4);
                _ShopslistRecordList.addAll(_ShopRating3);
                _ShopslistRecordList.addAll(_ShopRating2);
                _ShopslistRecordList.addAll(_ShopRating1);
                _ShopslistRecordList.addAll(_ShopRating0);

            } else {
                _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
            }
        }
    }


}