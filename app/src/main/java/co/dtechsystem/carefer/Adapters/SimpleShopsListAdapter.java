package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.UI.Activities.ShopDetailsActivity;


public class SimpleShopsListAdapter extends RecyclerView.Adapter<SimpleShopsListAdapter.SimpleViewHolder> {
    Activity activity;

    public SimpleShopsListAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_shops, parent, false);
        final LinearLayout lay_shop_item = (LinearLayout) view.findViewById(R.id.lay_shop_item);
        lay_shop_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(activity, ShopDetailsActivity.class);
                activity.startActivity(intent);

            }
        });
        return new SimpleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SimpleViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 12;
    }

    class SimpleViewHolder extends RecyclerView.ViewHolder {

        SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}