package co.dtechsystem.carefer.Adapters;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.ShopDetailsActivity;


public class ShopsListRecycleViewAdapter extends RecyclerView.Adapter<ShopsListRecycleViewAdapter.ViewHolder> {
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordList;
    private static List<ShopsListModel.ShopslistRecord> _ShopslistRecordListFilter;
    private int lastPosition;
    Activity activity;
    Boolean expand;

    public ShopsListRecycleViewAdapter(Activity activity, List<ShopsListModel.ShopslistRecord> _ShopslistRecordList) {
        this._ShopslistRecordList = _ShopslistRecordList;
        this._ShopslistRecordListFilter = new ArrayList<ShopsListModel.ShopslistRecord>();
        this._ShopslistRecordListFilter.addAll(_ShopslistRecordList);
        this.activity = activity;
        this.expand = false;
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
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(activity, ShopDetailsActivity.class);
                mIntent.putExtra("ShopID", _ShopslistRecordList.get(position).getID().toString());
                activity.startActivity(mIntent);
            }
        });

        holder.lay_shops_names.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (expand) {
                    holder.lay_shops_names.setBackgroundColor(activity.getResources().getColor(R.color.colorCreamDark));
                    holder.iv_drop_shop_details.setBackground(activity.getResources().getDrawable(android.R.drawable.arrow_down_float));
                    collapse(holder.lay_details, 500, 0);
                    expand = false;
                } else {
                    holder.iv_drop_shop_details.setBackground(activity.getResources().getDrawable(android.R.drawable.arrow_up_float));
                    holder.lay_shops_names.setBackgroundColor(activity.getResources().getColor(R.color.colorLightBlue));
                    expand = true;
                    int i = (int) activity.getResources().getDimension(R.dimen._100sdp);
                    expand(holder.lay_details, 500, i);

                }
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_shop_name_shop_list, tv_service_type_shop_list, tv_desc_shop_list;
        public RatingBar rb_shop_shop_list;
        LinearLayout lay_shop_item, lay_shops_names, lay_details;
        ImageView iv_fav_shop_list, iv_drop_shop_details;
        Button btn_details_shops_list;

        public ViewHolder(View v) {

            super(v);
            tv_shop_name_shop_list = (TextView) v.findViewById(R.id.tv_shop_name_shop_list);
            tv_service_type_shop_list = (TextView) v.findViewById(R.id.tv_service_type_shop_list);
            tv_desc_shop_list = (TextView) v.findViewById(R.id.tv_desc_shop_list);
            rb_shop_shop_list = (RatingBar) v.findViewById(R.id.rb_shop_shop_list);
            lay_shop_item = (LinearLayout) v.findViewById(R.id.lay_shop_item);
            iv_fav_shop_list = (ImageView) v.findViewById(R.id.iv_fav_shop_list);
            lay_shops_names = (LinearLayout) v.findViewById(R.id.lay_shops_names);
            lay_details = (LinearLayout) v.findViewById(R.id.lay_details);
            btn_details_shops_list = (Button) v.findViewById(R.id.btn_details_shops_list);
            iv_drop_shop_details = (ImageView) v.findViewById(R.id.iv_drop_shop_details);


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
//            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
//            ScaleAnimation animation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    public static void expand(final View v, int duration, int targetHeight) {

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
        valueAnimator.start();
    }

    public static void collapse(final View v, int duration, int targetHeight) {
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
    public static void filterShops(String Type, String Both, String service, String Brand) {
        service = service.toLowerCase(Locale.getDefault());
        Brand = Brand.toLowerCase(Locale.getDefault());
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

    // Filter for Shops Names Class
    public static void filterShopsName(String Text) {
        Text = Text.toLowerCase(Locale.getDefault());
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
        }

    }

    // Filter Class
    public static void filterShopsWithProviders(ArrayList<String> selectedItems) {
        if (selectedItems.size() > 0) {
            _ShopslistRecordList.clear();
            for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                for (int j = 0; j < selectedItems.size(); j++) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(Locale.getDefault())
                            .contains(selectedItems.get(j).toString())) {
                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
//                    else if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(Locale.getDefault())
//                            .contains(selectedItems.get(j).toString())) {
//                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
//                    }
                }
            }

        } else {
            _ShopslistRecordList.addAll(_ShopslistRecordListFilter);
        }


    }

}