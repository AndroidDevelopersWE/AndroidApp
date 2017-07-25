package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.bogdwellers.pinchtozoom.ImageMatrixTouchHandler;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.List;

import co.dtechsystem.carefer.Models.ShopsDetailsModel;
import co.dtechsystem.carefer.R;
import co.dtechsystem.carefer.Utils.AppConfig;

public class ShopsImagesPagerAdapter extends PagerAdapter {

    private final Activity mActivity;
    private final LayoutInflater mLayoutInflater;
    private final List<ShopsDetailsModel.ShopsImagessRecord> _ShopsImagesDetails;
    private final String mShopID;
//    private boolean clicked;
//    private final int getPosition;

    @SuppressWarnings("unused")
    public ShopsImagesPagerAdapter(Activity mActivity, List<ShopsDetailsModel.ShopsImagessRecord> _ShopsImagesDetails, String ShopID) {
        this.mActivity = mActivity;
        this._ShopsImagesDetails = _ShopsImagesDetails;
        this.mShopID = ShopID;
//        this.clicked = true;
//        this.getPosition = getPosition;
        mLayoutInflater = (LayoutInflater) mActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return _ShopsImagesDetails.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View itemView = mLayoutInflater.inflate(R.layout.pager_item_sliding_images, container, false);
        final ImageView iv_full_image = (ImageView) itemView.findViewById(R.id.image);
//        if (clicked && getPosition < _ShopsImagesDetails.size()) {
//            final String Url = AppConfig.BaseUrlImages + "shop-" + mShopID + "/";
//            Glide.with(mActivity).load(Url + _ShopsImagesDetails.get(getPosition)
//                    .getImageName()).into(iv_full_image);
//            clicked = false;
//
//        } else {
        final String Url = AppConfig.BaseUrlImages + "shop-" + mShopID + "/";
        Glide.with(mActivity).load(Url + _ShopsImagesDetails.get(position)
                .getImageName()).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                iv_full_image.setOnTouchListener(new ImageMatrixTouchHandler(mActivity));

                return false;
            }
        }).into(iv_full_image);

//        }

        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((FrameLayout) object);
    }
}