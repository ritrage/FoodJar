package oop.project.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

public class AccountSignup extends AppCompatActivity {

    private String name, email, city, password, role, phoneText;
    EditText nameEt, phoneEt, emailEt, passwordEt, cityEt;
    public Button submitButton;
    private Spinner roleSp;
    ProgressDialog pdialog;
    private FirebaseAuth auth ;
    ConnectivityDetector connectivityDetector;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_signup);

        nameEt = (EditText)findViewById(R.id.nameEt);
        phoneEt = (EditText)findViewById(R.id.phoneEt);
        emailEt = (EditText)findViewById(R.id.emailEt);
        passwordEt = (EditText)findViewById(R.id.passwordEt);
        cityEt = (EditText)findViewById(R.id.cityEt);
        roleSp = (Spinner)findViewById(R.id.roleSp);
        roleSp.setPrompt("Select your Role");
        auth = FirebaseAuth.getInstance();
        pdialog = new ProgressDialog(this);
        submitButton = (Button)findViewById(R.id.submitButton);

        submitButton.setOnTouchListener(new View.OnTouchListener() {
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

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                name = nameEt.getText().toString();
                email = emailEt.getText().toString().trim();
                password = passwordEt.getText().toString().trim();
                city = cityEt.getText().toString().trim();
                phoneText = phoneEt.getText().toString();

                roleSp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        role = roleSp.getItemAtPosition(position).toString();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Toast.makeText(getApplicationContext(), "Select your Role", Toast.LENGTH_SHORT).show();
                    }
                });

               if (connectivityDetector.checkConnectivityStatus()) {
                    if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || TextUtils.isEmpty(password) || TextUtils.isEmpty(city)
                            || TextUtils.isEmpty(phoneText)) {
                        Toast.makeText(getApplicationContext(), "Fields cannot be left blank", Toast.LENGTH_SHORT).show();
                    } else if (password.length() < 8) {
                        passwordEt.setError("Minimum 8 characters required!");
                        passwordEt.requestFocus();
                      //  Toast.makeText(getApplicationContext(), "Password : Minimum 8 characters required", Toast.LENGTH_SHORT).show();
                    } else {
                        pdialog.setMessage("Registering \nPlease wait ...");
                        pdialog.show();
                        auth = FirebaseAuth.getInstance();
                        auth.createUserWithEmailAndPassword(email, password)
                                .addOnCompleteListener(AccountSignup.this, new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {

                                            User user = new User(name, email, phoneText, city, role);
                                            FirebaseDatabase.getInstance().getReference("User")
                                                    .child(Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser())).getUid())
                                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        pdialog.hide();
                                                        Toast.makeText(getApplicationContext(), "User Registered Successfully", Toast.LENGTH_SHORT).show();
                                                        Intent myIntent = new Intent(AccountSignup.this, LoginActivity.class);
                                                        startActivity(myIntent);
                                                    }
                                                }
                                            });
                                        } else {
                                            Toast.makeText(AccountSignup.this, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                }
               else{
                    connectivityDetector.showAlertDialog(AccountSignup.this, "Login Failed","No internet connection");
                }
            }
            });
    }
}

