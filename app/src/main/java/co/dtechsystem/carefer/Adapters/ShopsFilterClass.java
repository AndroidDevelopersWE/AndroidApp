package co.dtechsystem.carefer.Adapters;

import android.app.Activity;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import co.dtechsystem.carefer.Models.ShopsListModel;
import co.dtechsystem.carefer.R;

/**
 * Created by DELL on 6/12/2017.
 */

public abstract class ShopsFilterClass {
    public static List<ShopsListModel.ShopslistRecord> _ShopslistRecordList;
    public static List<ShopsListModel.ShopslistRecord> _ShopslistRecordListFilter;
    static Activity activity;

    public static List<ShopsListModel.ShopslistRecord>
    filterShopsWithProviders(Activity activity,
                             List<ShopsListModel.ShopslistRecord> _ShopslistRecordList,
                             List<ShopsListModel.ShopslistRecord> _ShopslistOriginal, final String ProvideWarranty, final String ProvideReplacementParts,
                             final String topRated, final String placeType, String Brands, final String Service) {

        ShopsFilterClass._ShopslistRecordList = _ShopslistRecordList;
        _ShopslistRecordListFilter = new ArrayList<ShopsListModel.ShopslistRecord>();

        _ShopslistRecordListFilter.addAll(_ShopslistRecordList);
        ShopsFilterClass.activity = activity;
//        if (_ShopslistRecordListFilter.size() == 0) {
//            _ShopslistRecordListFilter.addAll(_ShopslistOriginal);
//        }
        if (_ShopslistRecordList != null) {
            Locale locale = new Locale("ar");
            ShopsFilterClass._ShopslistRecordList.clear();
            for (int i = 0; i < _ShopslistRecordListFilter.size(); i++) {
                if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }
                else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated)&& _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .equals( Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                }

                else if (placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }

                } else if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .equals(placeType) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .equals(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .equals(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .equals(topRated) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .contains(placeType) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .contains(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .contains(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .contains(topRated) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .contains(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .contains(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .contains(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .contains(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (!placeType.equals("") && !ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && !Brands.equals("") && Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getShopType().toLowerCase(locale)
                            .contains(placeType) && _ShopslistRecordListFilter.get(i).getProvideWarranty().toLowerCase(locale)
                            .contains(ProvideWarranty) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && !ProvideReplacementParts.equals("") && !topRated.equals("") && !Brands.equals("") && !Service.equals("")) {
                    if (_ShopslistRecordListFilter.get(i).getProvideReplaceParts().toLowerCase(locale)
                            .contains(ProvideReplacementParts) && _ShopslistRecordListFilter.get(i).getShopRating().toLowerCase(locale)
                            .contains(topRated) && _ShopslistRecordListFilter.get(i).getBrands().toLowerCase(locale)
                            .contains(Brands) && _ShopslistRecordListFilter.get(i).getServiceType().toLowerCase(locale)
                            .contains(Service)) {
                        ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));
                    }
                } else if (placeType.equals("") && ProvideWarranty.equals("") && ProvideReplacementParts.equals("") && topRated.equals("") && Brands.equals("") && Service.equals("")) {
                    ShopsFilterClass._ShopslistRecordList.add(_ShopslistRecordListFilter.get(i));

                }
            }

            if (ShopsFilterClass._ShopslistRecordList.size() == 0) {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_record_found), Toast.LENGTH_SHORT).show();
            }
        }
//        else {
//            ShopsFilterClass._ShopslistRecordList.clear();
//            ShopsFilterClass._ShopslistRecordList.addAll(_ShopslistRecordListFilter);
//        }
        return ShopsFilterClass._ShopslistRecordList;
    }


}
