package oop.project.foodjar.Database.Model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(tableName = "Cart",indices = @Index(value={"name","size","restName"},unique = true))
public class Cart {

    @NonNull
    @PrimaryKey(autoGenerate=true)
    @ColumnInfo(name="id")
    public int id;

    @ColumnInfo(name="img")
    public int img;

    @ColumnInfo(name="name")
    public String name;

    @ColumnInfo(name="amount")
    public int amount;

    @ColumnInfo(name="price")
    public double price;

    @ColumnInfo(name="size")
    public String size;

    @ColumnInfo(name="restName")
    public String restName;
}
