package com.boardgames.jaipur.utils;

import android.os.Parcel;
import android.os.Parcelable;

public class GoodsDetailsForARound implements Parcelable {

    private String diamondGoodsDetail;
    private String goldGoodsDetail;
    private String silverGoodsDetail;
    private String clothGoodsDetail;
    private String spiceGoodsDetail;
    private String leatherGoodsDetail;
    private String threeTokenDetail;
    private String fourTokenDetail;
    private String fiveTokenDetail;
    private String camelTokenDetail;

    public String getDiamondGoodsDetail() {
        return diamondGoodsDetail;
    }

    public void setDiamondGoodsDetail(String diamondGoodsDetail) {
        this.diamondGoodsDetail = diamondGoodsDetail;
    }

    public String getGoldGoodsDetail() {
        return goldGoodsDetail;
    }

    public void setGoldGoodsDetail(String goldGoodsDetail) {
        this.goldGoodsDetail = goldGoodsDetail;
    }

    public String getSilverGoodsDetail() {
        return silverGoodsDetail;
    }

    public void setSilverGoodsDetail(String silverGoodsDetail) {
        this.silverGoodsDetail = silverGoodsDetail;
    }

    public String getClothGoodsDetail() {
        return clothGoodsDetail;
    }

    public void setClothGoodsDetail(String clothGoodsDetail) {
        this.clothGoodsDetail = clothGoodsDetail;
    }

    public String getSpiceGoodsDetail() {
        return spiceGoodsDetail;
    }

    public void setSpiceGoodsDetail(String spiceGoodsDetail) {
        this.spiceGoodsDetail = spiceGoodsDetail;
    }

    public String getLeatherGoodsDetail() {
        return leatherGoodsDetail;
    }

    public void setLeatherGoodsDetail(String leatherGoodsDetail) {
        this.leatherGoodsDetail = leatherGoodsDetail;
    }

    public String getThreeTokenDetail() {
        return threeTokenDetail;
    }

    public void setThreeTokenDetail(String threeTokenDetail) {
        this.threeTokenDetail = threeTokenDetail;
    }

    public String getFourTokenDetail() {
        return fourTokenDetail;
    }

    public void setFourTokenDetail(String fourTokenDetail) {
        this.fourTokenDetail = fourTokenDetail;
    }

    public String getFiveTokenDetail() {
        return fiveTokenDetail;
    }

    public void setFiveTokenDetail(String fiveTokenDetail) {
        this.fiveTokenDetail = fiveTokenDetail;
    }

    public String getCamelTokenDetail() {
        return camelTokenDetail;
    }

    public void setCamelTokenDetail(String camelTokenDetail) {
        this.camelTokenDetail = camelTokenDetail;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.diamondGoodsDetail);
        dest.writeString(this.goldGoodsDetail);
        dest.writeString(this.silverGoodsDetail);
        dest.writeString(this.clothGoodsDetail);
        dest.writeString(this.spiceGoodsDetail);
        dest.writeString(this.leatherGoodsDetail);
        dest.writeString(this.threeTokenDetail);
        dest.writeString(this.fourTokenDetail);
        dest.writeString(this.fiveTokenDetail);
        dest.writeString(this.camelTokenDetail);
    }

    public GoodsDetailsForARound() {
    }

    protected GoodsDetailsForARound(Parcel in) {
        this.diamondGoodsDetail = in.readString();
        this.goldGoodsDetail = in.readString();
        this.silverGoodsDetail = in.readString();
        this.clothGoodsDetail = in.readString();
        this.spiceGoodsDetail = in.readString();
        this.leatherGoodsDetail = in.readString();
        this.threeTokenDetail = in.readString();
        this.fourTokenDetail = in.readString();
        this.fiveTokenDetail = in.readString();
        this.camelTokenDetail = in.readString();
    }

    public static final Parcelable.Creator<GoodsDetailsForARound> CREATOR = new Parcelable.Creator<GoodsDetailsForARound>() {
        @Override
        public GoodsDetailsForARound createFromParcel(Parcel source) {
            return new GoodsDetailsForARound(source);
        }

        @Override
        public GoodsDetailsForARound[] newArray(int size) {
            return new GoodsDetailsForARound[size];
        }
    };
}
