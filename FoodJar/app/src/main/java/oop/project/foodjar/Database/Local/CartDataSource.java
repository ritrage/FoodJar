package oop.project.foodjar.Database.Local;

import java.util.List;

import io.reactivex.Flowable;
import oop.project.foodjar.Database.DataSource.ICartDataSource;
import oop.project.foodjar.Database.Model.Cart;

public class CartDataSource implements ICartDataSource {

    private CartDAO cartDAO;
    private static CartDataSource instance;

    public CartDataSource (CartDAO cartDAO){
        this.cartDAO=cartDAO;
    }

    public static CartDataSource getInstance(CartDAO cartDAO){
        if(instance==null)
            instance=new CartDataSource(cartDAO);
        return instance;
    }
    @Override
    public Flowable<List<Cart>> getCartItem() {
        return cartDAO.getCartItem();
    }

    @Override
    public Flowable<List<Cart>> getCartItemById(int cartItemId) {
        return cartDAO.getCartItemById(cartItemId);
    }

    @Override
    public int countCartItem() {
        return cartDAO.countCartItem();
    }

    @Override
    public void emptyCart() {
        cartDAO.emptyCart();
    }

    @Override
    public float sumPrice() {
        return cartDAO.sumPrice();
    }

    @Override
    public void insertToCart(Cart... carts) {
        cartDAO.insertToCart(carts);
    }

    @Override
    public void UpdateCart(Cart... carts) {
        cartDAO.UpdateCart(carts);
    }

    @Override
    public void deleteCartItem(Cart cart) {
        cartDAO.deleteCartItem(cart);
    }
}
