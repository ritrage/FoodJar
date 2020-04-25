package oop.project.foodjar;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.android.gms.location.FusedLocationProviderClient;

import java.util.List;

import oop.project.foodjar.Database.Model.Cart;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder>{

    Context context;
    List<Cart> cartList;

    FusedLocationProviderClient fusedLocationClient;


    public CartAdapter(Context context, List<Cart> cartList){
        this.context=context;
        this.cartList=cartList;
    }


    class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView type;
        TextView productName,productPrice,rsTag,size;
        ElegantNumberButton productQuantity;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            type=itemView.findViewById(R.id.type);
            productName=itemView.findViewById(R.id.name);
            productPrice=itemView.findViewById(R.id.amount);
            productQuantity=itemView.findViewById(R.id.quantity);
            rsTag=itemView.findViewById(R.id.rs);
            size=itemView.findViewById(R.id.size);
        }
    }
    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= LayoutInflater.from(context).inflate(R.layout.cart_item,parent,false);
        return new CartViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final CartViewHolder holder, final int position) {

        holder.type.setImageResource(cartList.get(position).img);
        holder.productName.setText(cartList.get(position).name);
        holder.productQuantity.setNumber(String.valueOf(cartList.get(position).amount));
        holder.productPrice.setText(String.valueOf(cartList.get(position).price));
        holder.size.setText(cartList.get(position).size);

        Common.total= Common.cartRespository.sumPrice();

        //Auto Save item when user change amount
        holder.productQuantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                if (newValue != 0) {
                    Cart cart = cartList.get(position);
                    cart.amount = newValue;
                    cart.price = (cart.price * newValue) / oldValue;
                    holder.productPrice.setText(String.valueOf(cart.price));

                    Common.cartRespository.UpdateCart(cart);

                }

                else {
                    Cart cart = cartList.get(position);
                    cartList.remove(position);
                    notifyItemRemoved(position);
                    Toast.makeText(context, "Removed from cart", Toast.LENGTH_SHORT).show();
                    Common.cartRespository.deleteCartItem(cart);
                    Common.cartRespository.UpdateCart(cart);

                }

                Common.total= Common.cartRespository.sumPrice();
            }
        });
    }


    @Override
    public int getItemCount() {
        return cartList.size();
    }

}
