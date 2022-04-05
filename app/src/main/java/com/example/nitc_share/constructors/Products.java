package com.example.nitc_share.constructors;

public class Products {

    public Products() {
    }

    private String pname, description, price, image, category, pid, date, time, sellerid, buyerid, sold, sellerrated, buyerrated, deleteOn, baseprice;
    public Products(String pname, String description, String price, String image, String category, String pid, String date, String time, String sellerid, String buyerid, String sold, String sellerrated, String buyerrated, String deleteOn, String baseprice) {
        this.pname = pname;
        this.description = description;
        this.price = price;
        this.image = image;
        this.category = category;
        this.pid = pid;
        this.date = date;
        this.time = time;
        this.sellerid = sellerid;
        this.buyerid = buyerid;
        this.sold = sold;
        this.sellerrated = sellerrated;
        this.buyerrated = buyerrated;
        this.deleteOn = deleteOn;
        this.baseprice = baseprice;
    }

    public String getBaseprice() {
        return baseprice;
    }

    public void setBaseprice(String baseprice) {
        this.baseprice = baseprice;
    }

    public String getDeleteOn() {
        return deleteOn;
    }

    public void setDeleteOn(String deleteOn) {
        this.deleteOn = deleteOn;
    }

    public String getPname() {
        return pname;
    }

    public void setPname(String pname) {
        this.pname = pname;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getBuyerid() {
        return buyerid;
    }

    public void setBuyerid(String buyerid) {
        this.buyerid = buyerid;
    }

    public void setSellerid(String sellerid) {
        this.sellerid = sellerid;
    }

    public String getSellerid() {
        return sellerid;
    }

    public String getSold() {
        return sold;
    }

    public void setSold(String sold) {
        this.sold = sold;
    }

    public String getBuyerrated() {
        return buyerrated;
    }

    public void setBuyerrated(String buyerrated) {
        this.buyerrated = buyerrated;
    }

    public String getSellerrated() {
        return sellerrated;
    }

    public void setSellerrated(String sellerrated) {
        this.sellerrated = sellerrated;
    }

}
