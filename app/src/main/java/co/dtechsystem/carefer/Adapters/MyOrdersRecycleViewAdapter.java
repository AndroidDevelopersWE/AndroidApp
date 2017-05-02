package co.dtechsystem.carefer.Adapters;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Transformation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import co.dtechsystem.carefer.Models.MyOrdersModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;


public class MyOrdersRecycleViewAdapter extends RecyclerView.Adapter<MyOrdersRecycleViewAdapter.ViewHolder> {
    private final List<MyOrdersModel.MyOrdersRecord> _MyOrdersRecords;
    private int lastPosition;
    private final Activity activity;
    private Boolean mExpand;

    public MyOrdersRecycleViewAdapter(Activity activity, List<MyOrdersModel.MyOrdersRecord> _MyOrdersRecords) {
        this._MyOrdersRecords = _MyOrdersRecords;
        this.activity = activity;
        this.mExpand = false;
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
        setAnimation(holder.itemView, position);
        if (_MyOrdersRecords.get(position).getOrderStatus().equals("1")) {
            holder.tv_my_order_status.setText(activity.getResources().getString(R.string.tv_completed_status));
        } else {
            holder.tv_my_order_status.setText(activity.getResources().getString(R.string.tv_pending_status));
        }
//        if (_MyOrdersRecords.get(position).getOrderType().equals("call")) {
//            holder.tv_my_order_type.setText("On Call");
//        } else {
//            holder.tv_my_order_type.setText("Navigate");
//
//        }
        holder.tv_my_order_type.setText(_MyOrdersRecords.get(position).getOrderType());
        holder.tv_my_order_number.setText(_MyOrdersRecords.get(position).getOrderNo());
        holder.tv_my_order_date.setText(Utils.formattedDateFromString("yyyy-mm-dd", "dd-MMM-yyyy", _MyOrdersRecords.get(position).getOrderDate()));
        holder.tv_my_order_shop_name.setText(_MyOrdersRecords.get(position).getShopName());
        holder.tv_my_order_shop_rating.setText(_MyOrdersRecords.get(position).getShopRating() + "/5");
        holder.tv_date_order_top.setText(activity.getResources().getString(R.string.tv_date) + Utils.formattedDateFromString("yyyy-mm-dd", "dd-MM-yyyy", _MyOrdersRecords.get(position).getOrderDate()));
        holder.tv_order_number_top.setText(activity.getResources().getString(R.string.tv_order_number) + _MyOrdersRecords.get(position).getOrderNo());

        holder.lay_top_my_order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mExpand) {
                    holder.iv_drop_shop_details.setBackground(activity.getResources().getDrawable(android.R.drawable.arrow_down_float));
                    mExpand = false;
                    collapse(holder.lay_bottom_my_order);
                } else {
                    holder.iv_drop_shop_details.setBackground(activity.getResources().getDrawable(android.R.drawable.arrow_up_float));
                    mExpand = true;
                    mExpand(holder.lay_bottom_my_order);

                }
            }
        });
    }

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
        a.setDuration(500);
        v.startAnimation(a);
    }

    public static void collapse(final View v) {
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

        // 1dp/ms
//        a.setDuration((int)(initialHeight / v.getContext().getResources().getDisplayMetrics().density));
        a.setDuration(500);
        v.startAnimation(a);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView tv_my_order_number, tv_my_order_date, tv_my_order_shop_name, tv_my_order_type,
                tv_my_order_shop_rating, tv_my_order_status, tv_date_order_top, tv_order_number_top;
        LinearLayout lay_top_my_order, lay_bottom_my_order;
        ImageView iv_drop_shop_details;

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
            iv_drop_shop_details = (ImageView) v.findViewById(R.id.iv_drop_shop_details);

        }

    }

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