package co.dtechsystem.carefer.Models;

import java.util.ArrayList;
import java.util.List;


public class MyOrdersModel {
    private List<MyOrdersRecord> ordersData;

    public MyOrdersModel() {
        ordersData = new ArrayList<>();
    }

    public List<MyOrdersRecord> getOrdersData() {
        return ordersData;
    }

    public void setOrdersData(List<MyOrdersRecord> ordersData) {
        this.ordersData = ordersData;
    }

    public class MyOrdersRecord {

        private String ID = "";
        private String shopID = "";

        private String shopName = "";
        private String serviceType = "";
        private String shopRating = "";
        private String orderNo = "";
        private String orderType = "";
        private String orderStatus = "";
        private String orderDate = "";
        private String isRated="";

        public String getID() {
            return ID;
        }

        public void setID(String ID) {
            this.ID = ID;
        }
        public String getShopID() {
            return shopID;
        }

        public void setShopID(String shopID) {
            this.shopID = shopID;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }


        public String getOrderNo() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo = orderNo;
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

        public String getOrderType() {
            return orderType;
        }

        public void setOrderType(String orderType) {
            this.orderType = orderType;
        }

        public String getOrderStatus() {
            return orderStatus;
        }

        public void setOrderStatus(String orderStatus) {
            this.orderStatus = orderStatus;
        }

        public String getOrderDate() {
            return orderDate;
        }

        public void setOrderDate(String orderDate) {
            this.orderDate = orderDate;
        }
        public String getIsRated() {
            return isRated;
        }

        public void setIsRated(String isRated) {
            this.isRated = isRated;
        }

    }
}
