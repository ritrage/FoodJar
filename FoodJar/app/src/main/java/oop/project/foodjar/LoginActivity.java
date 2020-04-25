package oop.project.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
//import android.support.v4.view.ViewPager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    EditText etUserEmail, etPassword;
    RadioGroup rbgUserType;
    Button btnLoginSubmit;
    ProgressDialog pDialog;
    String userEmail, userPassword;
    int checkedUserId;
    ConnectivityDetector connectivityDetector;
    TextView tvNewUser, tvForgotPassword;
    FirebaseAuth myFirebaseAuth;
    String role;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Initialize variable and object
        etUserEmail = (EditText) findViewById(R.id.etLoginUserEmail);
        etPassword = (EditText) findViewById(R.id.etLoginPassword);
        btnLoginSubmit = (Button) findViewById(R.id.btnLoginSubmit);
        rbgUserType = (RadioGroup) findViewById(R.id.rbgLoginUserType);
        tvNewUser = (TextView)findViewById(R.id.tvNewUser);
        tvForgotPassword = (TextView)findViewById(R.id.tvForgotPassword);
        myFirebaseAuth = FirebaseAuth.getInstance();

        // Progress dialog
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        //Change button color when click
        btnLoginSubmit.setOnTouchListener(new View.OnTouchListener() {
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
        //Submit login form
        btnLoginSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userEmail = etUserEmail.getText().toString().trim();
                userPassword = etPassword.getText().toString().trim();

                //Login for validation
                if(userEmail.isEmpty() || userPassword.isEmpty()){
                    Toast.makeText(LoginActivity.this, "Enter email and password", Toast.LENGTH_SHORT).show();
                }
                else{
                    //Check user type
                    checkedUserId = rbgUserType.getCheckedRadioButtonId();
                    connectivityDetector = new ConnectivityDetector(getBaseContext());

                    if(checkedUserId == R.id.rbUserRider){
                        if(connectivityDetector.checkConnectivityStatus()){
                            checkRiderLogin(userEmail, userPassword);
                            Toast.makeText(LoginActivity.this, "Rider", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            connectivityDetector.showAlertDialog(LoginActivity.this, "Login Failed","No internet connection");
                        }

                    }
                    else if(checkedUserId == R.id.rbUserClient){
                        if(connectivityDetector.checkConnectivityStatus()){
                            checkClientLogin(userEmail, userPassword);
                            Toast.makeText(LoginActivity.this, "Client", Toast.LENGTH_SHORT).show();
                        }else{
                            connectivityDetector.showAlertDialog(LoginActivity.this, "Login Failed","No internet connection");
                        }
                    }
                } //End of else
            }// End of onClick
        });

        //New User TextView Spanning
        SpannableString ss = new SpannableString("New User");
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvNewUser.setText(ss);
        tvNewUser.setMovementMethod(LinkMovementMethod.getInstance());

        tvNewUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Welcome to Registration Page!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent( LoginActivity.this, AccountSignup.class);
                startActivity(myIntent);
            }
        });

        //Forget Password TextView Spanning
        SpannableString ss1 = new SpannableString("Forgot Password");
        ss1.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 15, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvForgotPassword.setText(ss1);
        tvForgotPassword.setMovementMethod(LinkMovementMethod.getInstance());
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Reset Password or Google Sign-In!", Toast.LENGTH_SHORT).show();
                Intent myIntent = new Intent( LoginActivity.this, ForgotPassword.class);
                startActivity(myIntent);
            }
        });

    } //End of onCreate

    //Check Rider Login
    public void checkRiderLogin(final String email, final String password){

        pDialog.setMessage("Please Wait....");
        pDialog.setTitle("Processing");
        pDialog.setCancelable(false);
        showDialog();

            myFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideDialog();
                                if(true) {
                                    Intent myIntent = new Intent(LoginActivity.this, PhoneNumberPage.class);
                                    startActivity(myIntent);
                                    Toast.makeText(LoginActivity.this, "Welcome to Food Jar", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "You are not registered as our Delivery Agent", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                hideDialog();
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
     }

    //Check Client Login
    public void checkClientLogin(final String email, final String password) {

        pDialog.setMessage("Please Wait....");
        pDialog.setTitle("Processing");
        pDialog.setCancelable(false);
        showDialog();

            myFirebaseAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                hideDialog();
                                if(true) {
                                    Intent myIntent = new Intent(LoginActivity.this, ClientDashboardActivity.class);
                                    startActivity(myIntent);
                                    Toast.makeText(LoginActivity.this, "Welcome to Food Jar", Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    Toast.makeText(LoginActivity.this, "You are not a Registered Customer", Toast.LENGTH_SHORT).show();
                                }

                            } else {
                                hideDialog();
                                Toast.makeText(LoginActivity.this, "Invalid Credentials", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
     }

    //Show Dialog
    public void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    //Hide Dialog
    public void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    /*private boolean checkDatabase(String roleSelected){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String userId  = user.getUid();
        FirebaseDatabase firebaseDatabaseInstance = FirebaseDatabase.getInstance();
        DatabaseReference rootNode = firebaseDatabaseInstance.getReference("User");
        rootNode.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               role = dataSnapshot.child("role").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Database could not read", Toast.LENGTH_SHORT).show();
            }
        });
        if(role.equals(roleSelected)){
            Toast.makeText(LoginActivity.this, "You have logged in as Client", Toast.LENGTH_SHORT).show();
            return true;
        }
        Toast.makeText(LoginActivity.this, "Database Error detected", Toast.LENGTH_SHORT).show();
        return false;
    }*/

}

