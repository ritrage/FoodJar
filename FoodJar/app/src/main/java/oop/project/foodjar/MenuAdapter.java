package oop.project.foodjar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;

import java.util.ArrayList;

import oop.project.foodjar.Database.Model.Cart;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MyViewHolder> {
    private ArrayList<MenuList> myExampleList;
    Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView mFoodImgView;
        public TextView mDishView;
        public TextView mDishDes;
        public ImageView mFoodType;
        public TextView mPrice;
        public Button mAdd;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mFoodImgView = itemView.findViewById(R.id.foodImg);
            mDishView = itemView.findViewById(R.id.dishName);
            mDishDes = itemView.findViewById(R.id.foodDes);
            mFoodType = itemView.findViewById(R.id.typeImg);
            mPrice = itemView.findViewById(R.id.price);
            mAdd = itemView.findViewById(R.id.add);
        }
    }

    public MenuAdapter(ArrayList<MenuList> exampleList) {
        myExampleList = exampleList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vi = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_layout, parent, false);
        MyViewHolder mv = new MyViewHolder(vi);
        return mv;
    }

    //View Binder for MenuList
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, final int position) {
        final MenuList currentItem = myExampleList.get(position);
        holder.mFoodImgView.setImageResource(currentItem.getFoodImage());
        holder.mDishView.setText(currentItem.getDish());
        holder.mDishDes.setText(currentItem.getDishDescription());
        holder.mFoodType.setImageResource(currentItem.getTypeImage());
        holder.mPrice.setText(currentItem.getPrice());
        holder.mAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // show dialog box to confirm order
                showDialog(myExampleList.get(position),v);
            }

        });

}
//Confirm dialog box and its features setting
    private void showDialog(final MenuList position, final View v) {
        AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
        final View itemView =LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.confirm_dialog, null);

        //View for dialog box
        final TextView productName = itemView.findViewById(R.id.product_name);
        final TextView productPrice= itemView.findViewById(R.id.product_amount);
        final ElegantNumberButton productQuantity = itemView.findViewById(R.id.product_quantity);
        final ImageView productType = itemView.findViewById(R.id.product_type);
        final RadioButton sSize=itemView.findViewById(R.id.sSize);
        final RadioButton mSize=itemView.findViewById(R.id.mSize);
        final RadioButton lSize=itemView.findViewById(R.id.lSize);
        final RadioGroup sizeButton=itemView.findViewById(R.id.sizeButton);

        //Set View for dialog box
        productName.setText(position.getDish());
        productType.setImageResource(position.getTypeImage());
        productPrice.setText(position.getPrice());
        productQuantity.setNumber("1");
        final double itemPrice=Integer.parseInt(position.getPrice());
        sizeButton.clearCheck();
        ///setting price view as the size is changed

        sizeButton.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(sSize==itemView.findViewById(checkedId)){
                        Common.extraCharges = 0;
                        Common.parcel_size="Small";
                        productPrice.setText(String.valueOf((itemPrice + Common.extraCharges) * Integer.parseInt(productQuantity.getNumber())));
                }
                if(mSize==itemView.findViewById(checkedId)){

                        Common.extraCharges = 30;
                        Common.parcel_size="Medium";
                        productPrice.setText(String.valueOf((itemPrice + Common.extraCharges) * Integer.parseInt(productQuantity.getNumber())));
                    }
                if(lSize==itemView.findViewById(checkedId)){
                        Common.extraCharges = 60;
                        Common.parcel_size="Large";
                        productPrice.setText(String.valueOf((itemPrice + Common.extraCharges) * Integer.parseInt(productQuantity.getNumber())));
                }
            }
        });

        /// change in price view as the item number changes

        productQuantity.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {

                productPrice.setText(String.valueOf((itemPrice+Common.extraCharges)*newValue));

            }
        });


        ///On Confirm button Click
        builder.setNegativeButton("CONFIRM", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                final int quantity = Integer.parseInt(productQuantity.getNumber());
                final double price = (Integer.parseInt(position.getPrice()) + Common.extraCharges) * Integer.parseInt(productQuantity.getNumber());
                final String size = Common.parcel_size;

                    if (quantity != 0) {

                        //Adding Item to Cart
                        Toast.makeText(v.getRootView().getContext(), "Added to cart", Toast.LENGTH_SHORT).show();
                        Cart cartItem = new Cart();

                        cartItem.name = productName.getText().toString();
                        cartItem.amount = quantity;
                        cartItem.price = price;
                        cartItem.img = position.getTypeImage();
                        cartItem.size = size;

                        //saving present restaurant name to avoid ordering from another
                        Common.old= Common.newOne;
                        Common.added=true;

                        //Adding to DB
                        Common.cartRespository.insertToCart(cartItem);
                        Common.total = Common.cartRespository.sumPrice();
                        Log.d("FoodJar_Debug", new Gson().toJson(cartItem));

                        dialog.dismiss();
                    }
                }

        }).setView(itemView);

        builder.show();
        sSize.setChecked(true);
    }



    @Override
    public int getItemCount() {
        return myExampleList.size();
    }

}
