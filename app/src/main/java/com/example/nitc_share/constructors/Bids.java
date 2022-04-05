package com.example.nitc_share.constructors;

import java.util.IntSummaryStatistics;

public class Bids {

    String bidderid;
    Integer bidCount;
    String Price;

    public Bids(String bidderid, Integer bidCount, String Price) {
        this.bidderid = bidderid;
        this.bidCount = bidCount;
        this.Price = Price;
    }

    public Bids() {

    }

    public Integer getBidCount() {
        return bidCount;
    }

    public void setBidCount(Integer bidCount) {
        this.bidCount = bidCount;
    }

    public String getBidderid() {
        return bidderid;
    }

    public void setBidderid(String bidderid) {
        this.bidderid = bidderid;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getPrice() {
        return Price;
    }
}
