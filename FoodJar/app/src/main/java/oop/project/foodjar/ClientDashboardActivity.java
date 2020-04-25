package oop.project.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.core.Tag;

public class ClientDashboardActivity extends AppCompatActivity {

    TextView tvTitleOrderNumber, tvProfileName, tvProfileLoginTime, tvLocation, tvOrder;
    String name, orderCount, loginTime, location ;
    Button btnPlaceOrder ;
    private static final String TAG = ClientDashboardActivity.class.getName();
    @SuppressLint({"SourceLockedOrientationActivity", "ClickableViewAccessibility"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_dashboard);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        tvTitleOrderNumber = (TextView) findViewById(R.id.tvTitleOrderNumber);
        tvProfileName = (TextView) findViewById(R.id.tvProfileName);
        tvProfileLoginTime = (TextView) findViewById(R.id.tvProfileLoginTime);
        tvLocation = (TextView) findViewById(R.id.tvLocation);
        tvOrder = (TextView) findViewById(R.id.tvOrder);
        btnPlaceOrder=(Button)findViewById(R.id.btnPlaceOrder);
       /* FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId  = user.getUid();

        FirebaseDatabase firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference rootNode = firebaseDatabaseInstance.getReference("User");
        rootNode.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name = dataSnapshot.child("name").getValue().toString();
                orderCount  = dataSnapshot.child("order").getValue().toString();
                location = dataSnapshot.child("city").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        //Retrieved from Database
        tvProfileName.setText(name);
        tvOrder.setText(orderCount + "\n Delivered");
        tvTitleOrderNumber.setText(orderCount);
        tvLocation.setText(location);

*/
       //Constant Fields Defined
       tvTitleOrderNumber.setText("1");
       tvProfileName.setText("User");
       tvLocation.setText("Hyderabad");
       tvOrder.setText("1");

        //Place Order
        btnPlaceOrder.setOnTouchListener(new View.OnTouchListener() {
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

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ClientDashboardActivity.this, RestaurantList.class);
                startActivity(intent);
            }
        });

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


}