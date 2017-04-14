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

    public ShopsListRecycleViewAdapter(Activity activity, List<ShopsListModel.ShopslistRecord> _ShopslistRecordList) {
        this._ShopslistRecordList = _ShopslistRecordList;
        this._ShopslistRecordListFilter = new ArrayList<ShopsListModel.ShopslistRecord>();
        this._ShopslistRecordListFilter.addAll(_ShopslistRecordList);
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
//        String serviceType = _ShopslistRecordList.get(position).getServiceType().replaceAll(",", " \u2022");
        String stringTypeArr[] = _ShopslistRecordList.get(position).getServiceType().split(",");
        if (stringTypeArr != null) {
            holder.tv_service_type_shop_list.setText(stringTypeArr[0]);
        }
        holder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        holder.rb_shop_shop_list.setRating((Float.parseFloat(_ShopslistRecordList.get(position).getShopRating())));
        holder.tv_desc_shop_list.setText(_ShopslistRecordList.get(position).getShopDescription());
        holder.tv_shop_name_shop_list.setText(_ShopslistRecordList.get(position).getShopName());
        holder.lay_shop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(activity, ShopDetailsActivity.class);
                mIntent.putExtra("ShopID", _ShopslistRecordList.get(position).getID().toString());
                activity.startActivity(mIntent);
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_shop_name_shop_list, tv_service_type_shop_list, tv_desc_shop_list;
        public RatingBar rb_shop_shop_list;
        LinearLayout lay_shop_item;

        public ViewHolder(View v) {

            super(v);
            tv_shop_name_shop_list = (TextView) v.findViewById(R.id.tv_shop_name_shop_list);
            tv_service_type_shop_list = (TextView) v.findViewById(R.id.tv_service_type_shop_list);
            tv_desc_shop_list = (TextView) v.findViewById(R.id.tv_desc_shop_list);
            rb_shop_shop_list = (RatingBar) v.findViewById(R.id.rb_shop_shop_list);
            lay_shop_item = (LinearLayout) v.findViewById(R.id.lay_shop_item);


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
                            .contains(service) || _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(Locale.getDefault())
                            .contains(Brand)) {
                        _ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }
            }


        }

    }

}