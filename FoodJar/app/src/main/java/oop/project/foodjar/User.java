package oop.project.foodjar;

//Constructor Class for every New User
public class User {

    public String name, email, city, role, phone;
    public int  order;
    boolean Prime;

    public User(String name, String email, String phone, String city, String role){
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.role = role;
        this.city = city;
        order = 0 ;
        Prime = false;
    }
    public void setPrimeTrue(){
        Prime = true;
    }
}
