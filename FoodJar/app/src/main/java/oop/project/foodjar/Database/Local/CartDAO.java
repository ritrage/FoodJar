package oop.project.foodjar.Database.Local;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import oop.project.foodjar.Database.Model.Cart;

@Dao
public interface CartDAO {
    @Query("SELECT* FROM Cart")
        Flowable<List<Cart>> getCartItem();

    @Query("SELECT* FROM Cart WHERE id=:cartItemId")
    Flowable<List<Cart>> getCartItemById(int cartItemId);

    @Query("SELECT COUNT(*) from Cart")
    int countCartItem();

    @Query("SELECT SUM(price) AS value from Cart")
    float sumPrice();

    @Query("DELETE FROM Cart")
    void emptyCart();
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertToCart(Cart... carts);
    @Update
    void UpdateCart(Cart... carts);
    @Delete
    void deleteCartItem(Cart cart);
}
