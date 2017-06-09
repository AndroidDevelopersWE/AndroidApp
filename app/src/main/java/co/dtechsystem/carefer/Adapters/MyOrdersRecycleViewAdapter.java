package co.dtechsystem.carefer.Adapters;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
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
import android.widget.TextView;

import java.util.List;

import co.dtechsystem.carefer.Models.MyOrdersModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.RatingActivity;
import co.dtechsystem.carefer.Utils.Utils;


public class MyOrdersRecycleViewAdapter extends RecyclerView.Adapter<MyOrdersRecycleViewAdapter.ViewHolder> {
    private final List<MyOrdersModel.MyOrdersRecord> _MyOrdersRecords;
    private int lastPosition;
    private final Activity activity;

    @SuppressWarnings({"unused", "UnusedAssignment"})
    public MyOrdersRecycleViewAdapter(Activity activity, List<MyOrdersModel.MyOrdersRecord> _MyOrdersRecords) {
        this._MyOrdersRecords = _MyOrdersRecords;
        this.activity = activity;
        Boolean mExpand = false;
        setHasStableIds(true);
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public MyOrdersRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                    int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_orders, parent, false);
        // set the view's size, margins, paddings and layout parameters
        MyOrdersRecycleViewAdapter.ViewHolder vh = new MyOrdersRecycleViewAdapter.ViewHolder(v);
        return vh;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        if (_MyOrdersRecords.get(position).getOrderStatus().equals("1")) {
            holder.tv_my_order_status.setText(activity.getResources().getString(R.string.tv_completed_status));
        } else {
            holder.tv_my_order_status.setText(activity.getResources().getString(R.string.tv_pending_status));
        }

        String OrderType = _MyOrdersRecords.get(position).getOrderType();
        if (OrderType.equals("navigate")) {
            holder.tv_my_order_type.setText(activity.getResources().getString(R.string.order_type_shop));
        } else {
            holder.tv_my_order_type.setText(activity.getResources().getString(R.string.order_type_call));
        }
        holder.tv_my_order_number.setText(_MyOrdersRecords.get(position).getOrderNo());
        String DateFormed = Utils.formattedDateFromString("yyyy-MM-dd", "dd-MMM-yyyy", _MyOrdersRecords.get(position).getOrderDate());
        holder.tv_my_order_date.setText(DateFormed);
        holder.tv_my_order_shop_name.setText(_MyOrdersRecords.get(position).getShopName());
        holder.tv_my_order_shop_rating.setText(_MyOrdersRecords.get(position).getShopRating() + "/5");
        holder.tv_date_order_top.setText(DateFormed);
        holder.tv_order_number_top.setText(activity.getResources().getString(R.string.tv_order_number) + _MyOrdersRecords.get(position).getOrderNo());
        holder.lay_iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lay_top_my_order.setVisibility(View.GONE);
                int i = (int) activity.getResources().getDimension(R.dimen._210sdp);
                expand(holder.lay_bottom_my_order, 1000, i, holder);

            }
        });
        holder.lay_iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse(holder.lay_bottom_my_order, holder);

            }
        });
        holder.btn_add_rate_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, RatingActivity.class);
                intent.putExtra("orderID", _MyOrdersRecords.get(position).getID());
                intent.putExtra("shopID", _MyOrdersRecords.get(position).getShopID());
                intent.putExtra("ShopName", _MyOrdersRecords.get(position).getShopName());
                activity.startActivity(intent);

            }
        });

    }

    @SuppressWarnings("SameParameterValue")
    private static void expand(final View v, int duration, int targetHeight,
                               final MyOrdersRecycleViewAdapter.ViewHolder holder) {

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
                holder.lay_top_my_order.setVisibility(View.GONE);
            }
        });
        valueAnimator.start();

    }

    private static void collapse(final View v, final MyOrdersRecycleViewAdapter.ViewHolder holder) {
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
                holder.lay_top_my_order.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        a.setDuration(1000);
        v.startAnimation(a);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_my_order_number, tv_my_order_date, tv_my_order_shop_name, tv_my_order_type,
                tv_my_order_shop_rating, tv_my_order_status, tv_date_order_top, tv_order_number_top;
        final LinearLayout lay_top_my_order;
        final LinearLayout lay_bottom_my_order;
        final LinearLayout lay_iv_down;
        final LinearLayout lay_iv_up;
        Button btn_add_rate_shop;
        @SuppressWarnings("unused")
        final ImageView iv_drop_shop_details;

        public ViewHolder(View v) {

            super(v);
            tv_my_order_number = (TextView) v.findViewById(R.id.tv_my_order_number);
            tv_my_order_date = (TextView) v.findViewById(R.id.tv_my_order_date);
            tv_my_order_shop_name = (TextView) v.findViewById(R.id.tv_my_order_shop_name);
            tv_my_order_type = (TextView) v.findViewById(R.id.tv_my_order_type);
            tv_my_order_shop_rating = (TextView) v.findViewById(R.id.tv_my_order_shop_rating);
            tv_my_order_status = (TextView) v.findViewById(R.id.tv_my_order_status);
            tv_date_order_top = (TextView) v.findViewById(R.id.tv_date_order_top);
            tv_order_number_top = (TextView) v.findViewById(R.id.tv_order_number_top);
            lay_top_my_order = (LinearLayout) v.findViewById(R.id.lay_top_my_order);
            lay_bottom_my_order = (LinearLayout) v.findViewById(R.id.lay_bottom_my_order);
            lay_iv_down = (LinearLayout) v.findViewById(R.id.lay_iv_down);
            lay_iv_up = (LinearLayout) v.findViewById(R.id.lay_iv_up);
            iv_drop_shop_details = (ImageView) v.findViewById(R.id.iv_drop_shop_details);
            btn_add_rate_shop = (Button) v.findViewById(R.id.btn_add_rate_shop);

        }

    }

    @SuppressWarnings("unused")
    public static void mExpand(final View v, int duration, int targetHeight) {

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

    @SuppressWarnings("unused")
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

    @Override
    public int getItemCount() {
        return _MyOrdersRecords.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(_MyOrdersRecords.get(position).getID());
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
            animation.setDuration(1000);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }


}