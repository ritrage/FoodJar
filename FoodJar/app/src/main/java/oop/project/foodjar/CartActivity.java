package oop.project.foodjar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import oop.project.foodjar.Database.Model.Cart;

import static android.view.View.GONE;
import static oop.project.foodjar.Common.latitude;
import static oop.project.foodjar.Common.longitude;

public class CartActivity extends AppCompatActivity {


    RecyclerView recycler_cart;
    Button btn_placeOrder,final_order_button,editAddress;
    CompositeDisposable compositeDisposable;
    TextView totalAmount,delivery_chrg,grand;
    public static double distance,delChrge;
    public static StringBuffer user_address;
    public static String locality;
    public static String landmark;
    public static String subAddress;
    public static String city;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        compositeDisposable = new CompositeDisposable();
        recycler_cart = findViewById(R.id.recyclerView2);
        recycler_cart.setHasFixedSize(true);
        recycler_cart.setLayoutManager(new LinearLayoutManager(this));
        grand = findViewById(R.id.total);
        totalAmount=findViewById(R.id.food_amount1);
        delivery_chrg=findViewById(R.id.delivery_ch1);
        btn_placeOrder=findViewById(R.id.order_button);
        final_order_button = findViewById(R.id.final_order_button);
        final_order_button.setVisibility(GONE);
        editAddress=findViewById(R.id.editAddress);
        editAddress.setVisibility(GONE);

        getAddress(MainActivity.user_lat,MainActivity.user_lon);

        final_order_button.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == MotionEvent.ACTION_DOWN){
                    v.getBackground().setAlpha(150);
                }
                else if(event.getAction() == MotionEvent.ACTION_UP){
                    v.getBackground().setAlpha(255);
                }
                return false;
            }
        });

        final_order_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             Intent intent = new Intent(CartActivity.this, PhoneNumberPage.class);
             startActivity(intent);
             Toast.makeText(getApplicationContext(), "Verify your Phone Number", Toast.LENGTH_SHORT).show();
            }
        });

        btn_placeOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog(v);

            }
        });
        editAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog(v);
            }
        });


        loadCartItem();

        distance=getDistance(latitude, longitude,MainActivity.user_lat, MainActivity.user_lon);
        if(distance<=20000)
            delChrge=25.00;
        else delChrge=30.00;


    }
    //end of OnCreate

    private void getDialog(View v) {

        AlertDialog.Builder builder = new AlertDialog.Builder(v.getRootView().getContext());
        final View itemView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.location_dialog, null);
        builder.setTitle("Enter address");
        EditText address1 = itemView.findViewById(R.id.landmark);
        EditText address2 = itemView.findViewById(R.id.locality);
        EditText address3 = itemView.findViewById(R.id.sub_address);
        EditText address4 = itemView.findViewById(R.id.city);


        address1.setText(landmark);
        address2.setText(locality);
        address3.setText(subAddress);
        address4.setText(city);

        builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(address1.length()!=0 && address2.length()!=0 && address3.length()!=0 && address4.length()!=0) {
                    StringBuffer str=new StringBuffer();
                    user_address =str.append( address1.getText() + "\n");
                    str.append(address2.getText() + "\n");
                    str.append(address3.getText() + "\n");
                    str.append(address4.getText() + "\n");

                    landmark=address1.getText().toString();
                    locality=address2.getText().toString();
                    subAddress=address3.getText().toString();
                    city=address4.getText().toString();

                    dialog.dismiss();

                    btn_placeOrder.setVisibility(View.GONE);
                    final_order_button.setVisibility(View.VISIBLE);
                    editAddress.setVisibility(View.VISIBLE);

                }
                else {
                    dialog.dismiss();
                    btn_placeOrder.setVisibility(View.VISIBLE);
                    final_order_button.setVisibility(GONE);
                    editAddress.setVisibility(GONE);
                }
            }

        }).setView(itemView);

        builder.show();
    }

    public void getAddress(double lat, double lon) {
        Geocoder geocoder=new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                Address fetchedAddress = addresses.get(0);

                landmark=fetchedAddress.getFeatureName();
                locality=fetchedAddress.getLocality();
                subAddress=fetchedAddress.getSubAdminArea();
                city=fetchedAddress.getAdminArea();

                Log.d("LOCATION","Location"+ landmark + locality + subAddress + city);


            } else {
                Log.d("LOCATION", "searching for location");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private double getDistance(double latitude, double longitude, double user_lat, double user_lon) {
        Location locationRest=new Location("pointA");
        locationRest.setLatitude(latitude);
        locationRest.setLongitude(longitude);

        Location locationUser=new Location("pointB");
        locationUser.setLatitude(user_lat);
        locationUser.setLongitude(user_lon);

        return locationRest.distanceTo(locationUser);
    }


    private void loadCartItem() {
        compositeDisposable.add(
                Common.cartRespository.getCartItem()
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribeOn(Schedulers.io())
                        .subscribe(new Consumer<List<Cart>>() {
                            @Override
                            public void accept(List<Cart> carts) throws Exception {
                                displayCartItem(carts);
                            }
                        })
        );
    }

    private void displayCartItem(List<Cart> carts) {
        CartAdapter cartAdapter = new CartAdapter(this, carts);
        recycler_cart.setAdapter(cartAdapter);
        if (cartAdapter.getItemCount() == 0) {
            finish();
            Common.total = 0;
            Common.added = false;
            Common.old=-1;
        }
        else {
            totalAmount.setText(String.valueOf(Common.total));
            delivery_chrg.setText(String.valueOf(delChrge));
            grand.setText(String.valueOf(Common.total+delChrge));

        }
    }

    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();

    }

    protected void onStop() {
        compositeDisposable.clear();
        super.onStop();
    }
}
