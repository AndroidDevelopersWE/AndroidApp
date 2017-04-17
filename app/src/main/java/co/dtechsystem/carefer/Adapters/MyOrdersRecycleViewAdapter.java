package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ceylonlabs.imageviewpopup.ImagePopup;

import java.util.List;

import co.dtechsystem.carefer.Models.MyOrdersModel;
import co.dtechsystem.carefer.Models.ShopsDetailsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;
import co.dtechsystem.carefer.Utils.Utils;


public class MyOrdersRecycleViewAdapter extends RecyclerView.Adapter<MyOrdersRecycleViewAdapter.ViewHolder> {
    private List<MyOrdersModel.MyOrdersRecord> _MyOrdersRecords;
    private int lastPosition;
    private Activity activity;

    public MyOrdersRecycleViewAdapter(Activity activity, List<MyOrdersModel.MyOrdersRecord> _MyOrdersRecords) {
        this._MyOrdersRecords = _MyOrdersRecords;
        this.activity = activity;
    }

    @Override
    public MyOrdersRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_orders, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyOrdersRecycleViewAdapter.ViewHolder vh = new MyOrdersRecycleViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final MyOrdersRecycleViewAdapter.ViewHolder holder, final int position) {
        if (_MyOrdersRecords.get(position).getOrderStatus().equals("1")) {
            holder.tv_my_order_status.setText("In Process");
        } else {
            holder.tv_my_order_status.setText("Completed");
        }
        if (_MyOrdersRecords.get(position).getOrderType().equals("call")) {
            holder.tv_my_order_type.setText("On Call");
        } else {
            holder.tv_my_order_type.setText("Navigate");

        }
        holder.tv_my_order_number.setText(_MyOrdersRecords.get(position).getOrderNo());
        holder.tv_my_order_date.setText(Utils.formattedDateFromString("yyyy-mm-dd", "dd-MMM-yyyy", _MyOrdersRecords.get(position).getOrderDate()));
        holder.tv_my_order_shop_name.setText(_MyOrdersRecords.get(position).getShopName());
        holder.tv_my_order_shop_rating.setText(_MyOrdersRecords.get(position).getShopRating() + "/5");

        ;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tv_my_order_number, tv_my_order_date, tv_my_order_shop_name, tv_my_order_type,
                tv_my_order_shop_rating, tv_my_order_status;

        public ViewHolder(View v) {

            super(v);
            tv_my_order_number = (TextView) v.findViewById(R.id.tv_my_order_number);
            tv_my_order_date = (TextView) v.findViewById(R.id.tv_my_order_date);
            tv_my_order_shop_name = (TextView) v.findViewById(R.id.tv_my_order_shop_name);
            tv_my_order_type = (TextView) v.findViewById(R.id.tv_my_order_type);
            tv_my_order_shop_rating = (TextView) v.findViewById(R.id.tv_my_order_shop_rating);
            tv_my_order_status = (TextView) v.findViewById(R.id.tv_my_order_status);

        }

    }

    @Override
    public int getItemCount() {
        return _MyOrdersRecords.size();
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