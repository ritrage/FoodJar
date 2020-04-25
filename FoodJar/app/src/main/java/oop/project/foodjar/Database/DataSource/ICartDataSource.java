package oop.project.foodjar.Database.DataSource;

import java.util.List;

import io.reactivex.Flowable;
import oop.project.foodjar.Database.Model.Cart;

public interface ICartDataSource {
    Flowable<List<Cart>> getCartItem();
    Flowable<List<Cart>> getCartItemById(int cartItemId);
    int countCartItem();
    void emptyCart();
    float sumPrice();
    void insertToCart(Cart... carts);
    void UpdateCart(Cart... carts);
    void deleteCartItem(Cart cart);
}
