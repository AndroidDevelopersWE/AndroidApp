package co.dtechsystem.carefer.Models;

import java.util.ArrayList;
import java.util.List;


public class FavouriteShopsModel {
    private List<FavouriteShopsRecord> favouriteShops;

    public FavouriteShopsModel() {
        favouriteShops = new ArrayList<FavouriteShopsRecord>();
    }

    public List<FavouriteShopsRecord> getFavouriteShops() {
        return favouriteShops;
    }

    public void setFavouriteShops(List<FavouriteShopsRecord> favouriteShops) {
        this.favouriteShops = favouriteShops;
    }

    public class FavouriteShopsRecord {

        private String shopName = "";


        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }


    }
}
