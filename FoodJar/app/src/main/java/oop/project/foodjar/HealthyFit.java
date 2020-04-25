package oop.project.foodjar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import oop.project.foodjar.Database.DataSource.CartRepository;
import oop.project.foodjar.Database.Local.CartDataSource;
import oop.project.foodjar.Database.Local.CartDatabase;

public class HealthyFit extends AppCompatActivity {


        ArrayList<MenuList> menuList;
        private RecyclerView myrecyclerView;
        private MenuAdapter myAdapter;
        private RecyclerView.LayoutManager myLayoutManager;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_healthy_fit);

            createMenuList();
            menuRecyclerView();
            initDB();



            FloatingActionButton fab=findViewById(R.id.cartButton);

            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(getApplicationContext(), CartActivity.class);
                    startActivity(intent);
                }
            });


        }//end of onCreate

        public void createMenuList() {
            menuList = new ArrayList<>();
            menuList.add(new MenuList(R.drawable.dal, R.drawable.veg, "Dal Tadka","75","Indian Dal"));
            menuList.add(new MenuList(R.drawable.chicken, R.drawable.nonveg, "Kadhai Chicken","200","Chicken dish"));
            menuList.add(new MenuList(R.drawable.chicken2, R.drawable.nonveg, "Chicken Roti Combo","299","Thali"));
            menuList.add(new MenuList(R.drawable.gajar, R.drawable.veg, "Gajar ka Halwa","65","Carrot Sweet Dish"));
            menuList.add(new MenuList(R.drawable.thali, R.drawable.veg, "Indian thali","120","Rice, Dal, Vegetable & Curd"));
            menuList.add(new MenuList(R.drawable.pakoda, R.drawable.veg, "Aloo Pakoda ","50","Fried potato"));
            menuList.add(new MenuList(R.drawable.roti, R.drawable.veg, "Roti","15","Indian bread"));
            menuList.add(new MenuList(R.drawable.khichdi, R.drawable.veg, "Khichdi","120","Rice based item"));

            //Bottom Navigation
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
            bottomNavigationView.setSelectedItemId(R.id.dashboard);
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.dashboard:
                            startActivity(new Intent(getApplicationContext(),
                                    ClientDashboardActivity.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.vendors:
                            startActivity(new Intent(getApplicationContext(),
                                    RestaurantList.class));
                            return true;
                        case R.id.logout:
                            AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                            builder.setTitle("Confirmation!").
                                    setMessage("You sure, that you want to logout?");
                            builder.setPositiveButton("Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            Intent i = new Intent(getApplicationContext(),
                                                    MainActivity.class);
                                            startActivity(i);
                                        }
                                    });
                            builder.setNegativeButton("No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                            AlertDialog alert11 = builder.create();
                            alert11.show();
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.prime:
                            startActivity(new Intent(getApplicationContext(),
                                    PrimePlus.class));
                            overridePendingTransition(0, 0);
                            return true;
                        case R.id.contact:
                            startActivity(new Intent(getApplicationContext(),
                                    ContactUs.class));
                            overridePendingTransition(0, 0);
                            return true;
                    }
                    return false;
                }
            });//end of bottom navigation

        }

        public void menuRecyclerView() {
            myrecyclerView = findViewById(R.id.recyclerView_hf);
            myrecyclerView.setHasFixedSize(true);
            myLayoutManager = new LinearLayoutManager(this);
            myAdapter = new MenuAdapter(menuList);
            myrecyclerView.setLayoutManager(myLayoutManager);
            myrecyclerView.setAdapter(myAdapter);

        }

        private void initDB(){
            Common.cartDatabase= CartDatabase.getInstance(this);
            Common.cartRespository= CartRepository.getInstance(CartDataSource.getInstance(Common.cartDatabase.cartDAO()));
        }
    }

