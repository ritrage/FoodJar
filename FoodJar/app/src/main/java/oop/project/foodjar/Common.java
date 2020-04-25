package oop.project.foodjar;

import oop.project.foodjar.Database.DataSource.CartRepository;
import oop.project.foodjar.Database.Local.CartDatabase;
import oop.project.foodjar.Database.Model.Cart;

public class Common {
    public static CartDatabase cartDatabase;
    public static CartRepository cartRespository;

    public static float total;//total bill for the food items
    
    public static String parcel_size;
    public static double extraCharges;
    public static String rest_name;
    public static String last_rest="null";
    public static String oldRest;
    public static Boolean added=false;
    public static double latitude;
    public static double longitude;
    public static int old=-1,newOne;
}
