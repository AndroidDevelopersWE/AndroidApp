package co.dtechsystem.carefer.Models;

import java.util.ArrayList;
import java.util.List;


public class ShopsListModel {
    private List<ShopslistRecord> shopsList;

    public ShopsListModel() {
        shopsList = new ArrayList<ShopslistRecord>();
    }

    public List<ShopslistRecord> getShopsList() {
        return shopsList;
    }

    public void setShopsList(List<ShopslistRecord> shopsList) {
        this.shopsList = shopsList;
    }

    public class ShopslistRecord {

        private String ID = "";
        private String shopName = "";
        private String shopDescription = "";
        private String serviceType = "";
        private String shopRating = "";


        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }


        public String getShopDescription() {
            return shopDescription;
        }

        public void setShopDescription(String shopDescription) {
            this.shopDescription = shopDescription;
        }

        public String getServiceType() {
            return serviceType;
        }

        public void setServiceType(String serviceType) {
            this.serviceType = serviceType;
        }


        public String getShopRating() {
            return shopRating;
        }

        public void setShopRating(String shopRating) {
            this.shopRating = shopRating;
        }

    }
}
