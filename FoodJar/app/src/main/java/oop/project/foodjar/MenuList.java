package oop.project.foodjar;


public class MenuList {
    private int foodImage;
    private int typeImage;
    private String dish;
    private String dishDescription;
    private String foodPrice;
    public String restaurantName;

    public MenuList(int foodImg, int type, String dishName,String price,String dishDes ){
        dish=dishName;
        foodImage=foodImg;
        typeImage=type;
        foodPrice=price;
        dishDescription=dishDes;

    }

    public int getFoodImage(){
        return foodImage ;
    }
    public String getPrice(){
        return foodPrice;
    }

    public int getTypeImage(){
        return typeImage;
    }
    public String getDish(){
        return dish;
    }
    public String getDishDescription(){
        return dishDescription;
    }
}

