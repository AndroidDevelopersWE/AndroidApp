package co.dtechsystem.carefer.Adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import co.dtechsystem.carefer.R;


public class SimpleMyOrdersAdapter extends RecyclerView.Adapter<SimpleMyOrdersAdapter.SimpleViewHolder> {

    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_orders, parent, false);
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