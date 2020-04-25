package oop.project.foodjar;

public class CardItem {
    ///for restaurant list
    private int mImageResource;
    private String mText1;
    private String mText2;
    private double latitude,longitude;

    public CardItem(int imageResource, String text1, String text2, double latitude,double longitude){
        mText1=text1;
        mText2=text2;
        mImageResource=imageResource;
        this.longitude=longitude;
        this.latitude=latitude;

    }

    public int getImageResource(){
        return mImageResource;
    }

    public String getText1(){
        return mText1;
    }
    public String getText2(){
        return mText2;
    }
    public double getLatitude(){
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
