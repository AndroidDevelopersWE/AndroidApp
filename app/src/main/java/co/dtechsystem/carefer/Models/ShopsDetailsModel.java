package co.dtechsystem.carefer.Models;

import java.util.ArrayList;
import java.util.List;


public class ShopsDetailsModel {
    private List<ShopsDetailsRecord> shopsDetail;
    private List<ShopsImagessRecord> shopImages;

    public ShopsDetailsModel() {
        shopsDetail = new ArrayList<ShopsDetailsRecord>();
        shopImages = new ArrayList<ShopsImagessRecord>();
    }

    public List<ShopsDetailsRecord> getShopsDetail() {
        return shopsDetail;
    }

    public void setShopsDetail(List<ShopsDetailsRecord> shopsDetail) {
        this.shopsDetail = shopsDetail;
    }

    public List<ShopsImagessRecord> getShopImages() {
        return shopImages;
    }

    public void setShopImages(List<ShopsImagessRecord> shopImages) {
        this.shopImages = shopImages;
    }

    public class ShopsDetailsRecord {

        private String ID = "";
        private String shopName = "";
        private String shopDescription = "";
        private String serviceType = "";
        private String shopRating = "";
        private String shopType = "";
        private String brands = "";
        private String latitude = "";
        private String longitude = "";

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

        public String getShopType() {
            return shopType;
        }

        public void setShopType(String shopType) {
            this.shopType = shopType;
        }

        public String getBrands() {
            return brands;
        }

        public void setBrands(String brands) {
            this.brands = brands;
        }
        public String getLatitude() {
            return latitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public String getLongitude() {
            return longitude;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

    }

    public class ShopsImagessRecord {

        private String ID = "";
        private String imageName = "";


        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }

        public String getImageName() {
            return imageName;
        }

        public void setImageName(String imageName) {
            this.imageName = imageName;
        }


    }
}
