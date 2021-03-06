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
import android.widget.Toast;

import java.util.List;

import co.dtechsystem.carefer.Models.MyOrdersModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.OrderDetailActivity;
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

//        1 = Shops ==> rating
//        2 = Moved Shop ==> order detail
//        3 = Received Car ==> order detail


        String DateFormed = Utils.formattedDateFromString("yyyy-MM-dd", "dd-MM-yyyy",
                _MyOrdersRecords.get(position).getOrderDate());

        // Order no and order no Top
        holder.tv_order_number_top.setText(activity.getResources().getString(R.string.tv_order_number) +
                _MyOrdersRecords.get(position).getOrderNo());

        holder.tv_date_order_top.setText(DateFormed);

        // order no bottom ,order date and order type  bottom
        holder.tv_my_order_number.setText(_MyOrdersRecords.get(position).getOrderNo());
        holder.tv_my_order_date.setText(DateFormed);


        if(_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("1")){

            //Order No, order date, shop name, order status, rating, order type

            holder.tv_name1_heading.setText(activity.getResources().getString(R.string.tv_shop));
            holder.tv_name1.setText(_MyOrdersRecords.get(position).getShopName());

            holder.tv_name2_heading.setText(activity.getResources().getString(R.string.tv_shop_rating));
            holder.tv_name2.setText(_MyOrdersRecords.get(position).getShopRating() + "/5");

            holder.tv_name3_heading.setText(activity.getResources().getString(R.string.tv_order_type));
            String OrderType = _MyOrdersRecords.get(position).getOrderType();
            if (OrderType.equals("navigate")) {
                holder.tv_name3.setText(activity.getResources().getString(R.string.order_type_shop));
            } else{
                holder.tv_name3.setText(activity.getResources().getString(R.string.order_type_call));
            }


            holder.view4.setVisibility(View.GONE);
            holder.layout4.setVisibility(View.GONE);


        }else if(_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("2")){
            //Order No, order date, brand name, model name, service type, order type

            holder.btn_add_rate_shop.setText(activity.getResources().getString(R.string.title_order_detail));
            holder.tv_name1_heading.setText(activity.getResources().getString(R.string.tv_brand_name));
            holder.tv_name1.setText(_MyOrdersRecords.get(position).getBrandName());

            holder.tv_name2_heading.setText(activity.getResources().getString(R.string.tv_model_name));
            holder.tv_name2.setText(_MyOrdersRecords.get(position).getModelName());

            holder.tv_name3_heading.setText(activity.getResources().getString(R.string.tv_order_type));
            holder.tv_name3.setText(activity.getResources().getString(R.string.title_moved_shop));

            holder.view4.setVisibility(View.GONE);
            holder.layout4.setVisibility(View.GONE);



        }else if(_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("3")){
            //Order No, order date, brand name, model name, order type

            holder.btn_add_rate_shop.setText(activity.getResources().getString(R.string.title_order_detail));
            holder.tv_name1_heading.setText(activity.getResources().getString(R.string.tv_brand_name));
            holder.tv_name1.setText(_MyOrdersRecords.get(position).getBrandName());

            holder.tv_name2_heading.setText(activity.getResources().getString(R.string.tv_model_name));
            holder.tv_name2.setText(_MyOrdersRecords.get(position).getModelName());

            holder.tv_name3_heading.setText(activity.getResources().getString(R.string.tv_order_type));
            holder.tv_name3.setText(activity.getResources().getString(R.string.title_receive_car));

            holder.view4.setVisibility(View.GONE);
            holder.layout4.setVisibility(View.GONE);

        }






        holder.lay_iv_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.lay_top_my_order.setVisibility(View.GONE);
                int i = (int) activity.getResources().getDimension(R.dimen._180sdp);
                expand(holder.lay_bottom_my_order, 1000, i, holder);

            }
        });
        holder.lay_iv_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                collapse(holder.lay_bottom_my_order, holder);

            }
        });
        if (_MyOrdersRecords.get(position).getIsRated().equals("1")) {
            holder.btn_add_rate_shop.setAlpha(.7f);
        }
        holder.btn_add_rate_shop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //shpos 1
                //moved shops 2
                //recevie car 3
                if (_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("1")) {

                    if (!_MyOrdersRecords.get(position).getIsRated().equals("1")) {
                        Intent intent = new Intent(activity, RatingActivity.class);
                        intent.putExtra("orderID", _MyOrdersRecords.get(position).getID());
                        intent.putExtra("shopID", _MyOrdersRecords.get(position).getShopID());
                        intent.putExtra("ShopName", _MyOrdersRecords.get(position).getShopName());
                        activity.startActivity(intent);
                    } else {
                        Toast.makeText(activity, activity.getResources().getString(R.string.toast_review_already_added), Toast.LENGTH_SHORT).show();
                    }

                } else if (_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("2")) {

                    Intent intent = new Intent(activity, OrderDetailActivity.class);
                    intent.putExtra("orderID", _MyOrdersRecords.get(position).getOrderNo());
                    activity.startActivity(intent);

                } else if (_MyOrdersRecords.get(position).getOrderServiceTypeID().equals("3")) {
                    Intent intent = new Intent(activity, OrderDetailActivity.class);
                    intent.putExtra("orderID", _MyOrdersRecords.get(position).getOrderNo());
                    activity.startActivity(intent);
                }


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
        public final TextView tv_my_order_number, tv_my_order_date, tv_date_order_top, tv_order_number_top,tv_name1,tv_name2,tv_name3, tv_name4,
        tv_name1_heading, tv_name2_heading, tv_name3_heading, tv_name4_heading;

        final LinearLayout lay_top_my_order;
        final LinearLayout lay_bottom_my_order;
        final LinearLayout lay_iv_down;
        final LinearLayout lay_iv_up;
        final LinearLayout layout3;
        final LinearLayout layout4;
        final View view3;
        final View view4;
        Button btn_add_rate_shop;
        @SuppressWarnings("unused")
        final ImageView iv_drop_shop_details;

        public ViewHolder(View v) {

            super(v);


            tv_order_number_top = (TextView) v.findViewById(R.id.tv_order_number_top);
            tv_date_order_top = (TextView) v.findViewById(R.id.tv_date_order_top);

            tv_my_order_number = (TextView) v.findViewById(R.id.tv_my_order_number);
            tv_my_order_date = (TextView) v.findViewById(R.id.tv_my_order_date);

            tv_name1 = (TextView) v.findViewById(R.id.tv_name1);
            tv_name2 = (TextView) v.findViewById(R.id.tv_name2);
            tv_name3 = (TextView) v.findViewById(R.id.tv_name3);
            tv_name4 = (TextView) v.findViewById(R.id.tv_name4);

            tv_name1_heading = (TextView) v.findViewById(R.id.tv_name1_heading);
            tv_name2_heading = (TextView) v.findViewById(R.id.tv_name2_heading);
            tv_name3_heading = (TextView) v.findViewById(R.id.tv_name3_heading);
            tv_name4_heading = (TextView) v.findViewById(R.id.tv_name4_heading);

            view3 = (View) v.findViewById(R.id.view3);
            view4 = (View) v.findViewById(R.id.view4);
            layout3 = (LinearLayout) v.findViewById(R.id.layout3);
            layout4 = (LinearLayout) v.findViewById(R.id.layout4);
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
        return 4;
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