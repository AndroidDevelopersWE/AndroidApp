package co.dtechsystem.carefer.Sorting;

import java.util.ArrayList;
import java.util.List;

import co.dtechsystem.carefer.Models.ShopsListModel;

/**
 * Created by DELL on 6/16/2017.
 */

public abstract class ShopsRatingSorting {
    public static List<ShopsListModel.ShopslistRecord> MatchRating(List<ShopsListModel.ShopslistRecord> _ShopslistRecordListFilter, final String sortOrderType) {
        List<ShopsListModel.ShopslistRecord> _ShopslistRecordList = new ArrayList<ShopsListModel.ShopslistRecord>();
        ;
        final List<ShopsListModel.ShopslistRecord> _ShopRating5 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating4_5 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating4 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating3_5 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating3 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating2_5 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating2 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating1_5 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating1 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating0_5 = new ArrayList<ShopsListModel.ShopslistRecord>();
        final List<ShopsListModel.ShopslistRecord> _ShopRating0 = new ArrayList<ShopsListModel.ShopslistRecord>();
        for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
            Float shopRating = Float.parseFloat(_ShopslistRecordListFilter.get(i).getShopRating());
            if (shopRating >= 4.5) {
                _ShopRating5.add(_ShopslistRecordListFilter.get(i));
            } else if (shopRating >= 4) {
                _ShopRating4_5.add(_ShopslistRecordListFilter.get(i));

            } else if (
                    shopRating >= 3.5) {
                _ShopRating4.add(_ShopslistRecordListFilter.get(i));

            } else if (shopRating >= 3) {
                _ShopRating3_5.add(_ShopslistRecordListFilter.get(i));

            } else if (
                    shopRating >= 2.5) {
                _ShopRating3.add(_ShopslistRecordListFilter.get(i));

            } else if (shopRating >= 2) {
                _ShopRating2_5.add(_ShopslistRecordListFilter.get(i));

            } else if (shopRating >= 1.5) {
                _ShopRating2.add(_ShopslistRecordListFilter.get(i));

            } else if (shopRating >= 1) {
                _ShopRating1_5.add(_ShopslistRecordListFilter.get(i));

            } else if (shopRating >= 0.5) {
                _ShopRating1.add(_ShopslistRecordListFilter.get(i));

            } else if (shopRating >= 0) {
                _ShopRating0_5.add(_ShopslistRecordListFilter.get(i));
            } else if (shopRating == 0) {
                _ShopRating0.add(_ShopslistRecordListFilter.get(i));
            }
//            else {
//                _ShopRating0.add(_ShopslistRecordListFilter.get(i));
//            }

        }
        _ShopslistRecordList.addAll(_ShopRating5);
        _ShopslistRecordList.addAll(_ShopRating4_5);
        _ShopslistRecordList.addAll(_ShopRating4);
        _ShopslistRecordList.addAll(_ShopRating3_5);
        _ShopslistRecordList.addAll(_ShopRating3);
        _ShopslistRecordList.addAll(_ShopRating2_5);
        _ShopslistRecordList.addAll(_ShopRating2);
        _ShopslistRecordList.addAll(_ShopRating1_5);
        _ShopslistRecordList.addAll(_ShopRating1);
        _ShopslistRecordList.addAll(_ShopRating0_5);
        _ShopslistRecordList.addAll(_ShopRating0);
        return _ShopslistRecordList;
    }
}
