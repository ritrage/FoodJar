package oop.project.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class RestaurantList extends AppCompatActivity {
    ArrayList<CardItem> restList;
    private RecyclerView recyclerView;
    private Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restaurant_list);

        createExampleList();
        buildRecyclerView();
        //Bottom Navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.home);

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
                        Common.oldRest=Common.last_rest;
                        startActivity(new Intent(getApplicationContext(),
                                RestaurantList.class));
                        return true;
                    case R.id.logout:
                        AlertDialog.Builder builder = new AlertDialog.Builder(RestaurantList.this);
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
                }
                return false;
            }
        });
    }

    public void createExampleList(){
        restList=new ArrayList<>();

        restList.add(new CardItem(R.drawable.thali, "Toppers Tiffin ", "Tiffin Service",25.6076138,85.5647559));
        restList.add(new CardItem(R.drawable.menu_bg, "Asha's Kitchen", "HomeMade delicious food",25.6075238,85.5248559));
        restList.add(new CardItem(R.drawable.dosa_combo, "Healthy Fit", "Healthy homemade food",25.6075140,82.5247550));


    }

    public void buildRecyclerView() {
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new Adapter(restList);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(new Adapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position,View v) {

                Log.d("last Restaurant","Last Restaurant: "+Common.last_rest);

                if(position==0){
                    Common.newOne=position;
                    Intent intent=new Intent(RestaurantList.this, DominosMenu.class);

                    if(Common.old!=Common.newOne && Common.added==true){
                        dialog(intent,v,position);
                    }
                    else if(Common.old!=Common.newOne && Common.added==false){
                        CardItem current = restList.get(position);
                        Common.latitude=current.getLatitude();
                        Common.longitude=current.getLongitude();
                        startActivity(intent);
                    }
                    else startActivity(intent);

                }


                if(position==1){
                    Common.newOne=position;
                    Intent intent=new Intent(RestaurantList.this, AshaKitchen.class);

                    if(Common.old!=Common.newOne && Common.added==true) {
                        Log.d("last Restaurant","Last Restaurant: "+Common.last_rest);
                        dialog(intent,v,position);
                    }
                    else if(Common.old!=Common.newOne && Common.added==false){
                        CardItem current = restList.get(position);
                        Common.latitude=current.getLatitude();
                        Common.longitude=current.getLongitude();
                        startActivity(intent);
                    }

                    else startActivity(intent);
                }

                if(position==2){
                    Common.newOne=position;
                    Intent intent=new Intent(RestaurantList.this, HealthyFit.class);

                    if(Common.old!=Common.newOne && Common.added==true) {
                        dialog(intent,v,position);
                    }
                    else if(Common.old!=Common.newOne && Common.added==false){
                        CardItem current = restList.get(position);
                        Common.latitude=current.getLatitude();
                        Common.longitude=current.getLongitude();
                        startActivity(intent);
                    }
                    else startActivity(intent);

                }
            }
        });

    }
    private void dialog(final Intent intent,View v,int position) {
        AlertDialog.Builder builder=new AlertDialog.Builder(v.getRootView().getContext());
        final View itemView = LayoutInflater.from(v.getRootView().getContext()).inflate(R.layout.replace_item, null);
        final TextView replaceTitle = itemView.findViewById(R.id.replace_title);
        final TextView replaceMsg= itemView.findViewById(R.id.replace_msg);
        builder.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Common.cartRespository.emptyCart();
                Common.added=false;
                CardItem current = restList.get(position);
                Common.latitude=current.getLatitude();
                Common.longitude=current.getLongitude();

                startActivity(intent);

                dialog.dismiss();

            }
        }).setView(itemView);

        builder.show();
    }
}
