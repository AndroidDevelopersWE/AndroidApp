package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

import co.dtechsystem.carefer.Models.ReviewsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.Utils;


public class ReviewsRecycleViewAdapter extends RecyclerView.Adapter<ReviewsRecycleViewAdapter.ViewHolder> {
    private final List<ReviewsModel.ShopReview> _shopReviews;
    private int lastPosition;
    private final Activity activity;

    @SuppressWarnings("unused")
    public ReviewsRecycleViewAdapter(Activity activity, List<ReviewsModel.ShopReview> _shopReviews) {
        this._shopReviews = _shopReviews;
        this.activity = activity;
    }

    @SuppressWarnings("UnnecessaryLocalVariable")
    @Override
    public ReviewsRecycleViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_review, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ReviewsRecycleViewAdapter.ViewHolder vh = new ReviewsRecycleViewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final ReviewsRecycleViewAdapter.ViewHolder holder, final int position) {
        setAnimation(holder.itemView, position);
        if (_shopReviews.get(position).getCustomerName().equals("")) {
            holder.tv_customer_name_rate.setText(activity.getResources().getString(R.string.tv_name_anonymous));

        } else {
            holder.tv_customer_name_rate.setText(_shopReviews.get(position).getCustomerName());
        }
        String DateFormed = Utils.formattedDateFromString("yyyy-MM-dd", "dd-MMM-yyyy", _shopReviews.get(position).getDateAdded());
        holder.tv_date_rate.setText(DateFormed);
        holder.tv_coments_rate.setText(_shopReviews.get(position).getComment());
        holder.rb_price_rate.setRating(Float.parseFloat(_shopReviews.get(position).getPriceRating()));
        holder.rb_quality_rate.setRating(Float.parseFloat(_shopReviews.get(position).getQualityRating()));
        holder.rb_time_rate.setRating(Float.parseFloat(_shopReviews.get(position).getTimeRating()));
        holder.tv_coments_rate.setMovementMethod(new ScrollingMovementMethod());
        holder.tv_coments_rate.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                holder.tv_coments_rate.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        public final RatingBar rb_price_rate, rb_quality_rate, rb_time_rate;
        public final TextView tv_coments_rate, tv_customer_name_rate, tv_date_rate;

        public ViewHolder(View v) {

            super(v);
            tv_customer_name_rate = (TextView) v.findViewById(R.id.tv_customer_name_rate);
            tv_date_rate = (TextView) v.findViewById(R.id.tv_date_rate);
            rb_price_rate = (RatingBar) v.findViewById(R.id.rb_price_rate);
            rb_quality_rate = (RatingBar) v.findViewById(R.id.rb_quality_rate);
            rb_time_rate = (RatingBar) v.findViewById(R.id.rb_time_rate);
            tv_coments_rate = (TextView) v.findViewById(R.id.tv_coments_rate);

        }

    }


    @Override
    public int getItemCount() {
        return _shopReviews.size();
    }

    @Override
    public long getItemId(int position) {
        return Long.parseLong(_shopReviews.get(position).getID());
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