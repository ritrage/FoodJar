package oop.project.foodjar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.goodiebag.pinview.Pinview;
import android.content.Intent;
import android.widget.ProgressBar;
import java.util.Random;


public class OrderConfirmation extends AppCompatActivity {

    Pinview pinView;
    String inp_code, veri_code, mobile;
    Button btnVerify;
    TextView topText, tvResendOtp;
    private final static int SEND_SMS_PERMISSION_REQUEST_CODE = 111;
    private ProgressBar progressbar;
    int otp2;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        //initializing
        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        topText = (TextView) findViewById(R.id.topText);
        tvResendOtp = (TextView)findViewById(R.id.tvResendOtp);
        pinView = findViewById(R.id.pinView);
        btnVerify = (Button) findViewById(R.id.btnVerify);
        btnVerify.setEnabled(false);
        //getting mobile number from the previous activity
        Intent intent = getIntent();
        mobile = intent.getStringExtra("Phone Number");
        //creating an otp and sending to user
        final int otp = sendOtp(mobile);

        btnVerify.setOnTouchListener(new View.OnTouchListener() {
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

        btnVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inp_code = pinView.getValue();

                if (inp_code.isEmpty() || inp_code.length() < 6) {
                    topText.setText("Please type your OTP!");
                    pinView.setBackgroundColor(Color.RED);
                    topText.setTextColor(Color.RED);
                    pinView.setValue(""); // make the pinView empty
                } else {
                     verifyVerificationCode(inp_code, otp);
                }
            }
        });

        //Resend OTP Process
        SpannableString ss = new SpannableString("New User");
        ss.setSpan(new ForegroundColorSpan(Color.BLUE), 0, 8, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvResendOtp.setText(ss);
        tvResendOtp.setMovementMethod(LinkMovementMethod.getInstance());
        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(OrderConfirmation.this, "Resending OTP", Toast.LENGTH_SHORT).show();
                otp2 = sendOtp(mobile);

                btnVerify.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        inp_code = pinView.getValue();
                        if (inp_code.isEmpty() || inp_code.length() < 6) {
                            topText.setText("Please type your OTP!");
                            pinView.setBackgroundColor(Color.RED);
                            topText.setTextColor(Color.RED);
                            pinView.setValue(""); // make the pinView empty
                        }
                        else {
                            verifyVerificationCode(inp_code, otp2);
                        }
                    }
                });
            }
        });
    }//end of onCreate


        private boolean checkPermission(String sendSms) {
            int checkPermission = ContextCompat.checkSelfPermission(this, sendSms);
            return checkPermission == PackageManager.PERMISSION_GRANTED;
        }

        public void onRequestPermissionResult(int requestCode, String [] permission, int [] grantResults) {
            if (requestCode == SEND_SMS_PERMISSION_REQUEST_CODE) {
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED))
                    btnVerify.setEnabled(true);
            }
        }

        public void verifyVerificationCode(String inp_code, int otp){

            progressbar = (ProgressBar) findViewById(R.id.progressbar);
            topText = (TextView) findViewById(R.id.topText);
            pinView = findViewById(R.id.pinView);
            progressbar.setVisibility(View.GONE);

            if(inp_code.equals(""+ otp)){
                pinView.setBackgroundColor(Color.GREEN);
                topText.setText("Code Verified!");
                topText.setTextColor(Color.GREEN);
                progressbar.setVisibility(View.GONE);
                Intent intent = new Intent(OrderConfirmation.this, TickSplash.class);
                startActivity(intent);
            }
            else{
                topText.setText("Invalid OTP!");
                pinView.setBackgroundColor(Color.RED);
                topText.setTextColor(Color.RED);
                pinView.setValue("");
            }

        }

        public int sendOtp(String mobile){
            btnVerify = (Button) findViewById(R.id.btnVerify);
            //Taking permission to send sms code
            if (checkPermission(Manifest.permission.SEND_SMS)) {
                btnVerify.setEnabled(true);
            } else {
                ActivityCompat.requestPermissions(this, new
                        String[]{Manifest.permission.SEND_SMS}, SEND_SMS_PERMISSION_REQUEST_CODE);
            }
            //Generating OTP and sending it as an sms
            Random rand = new Random();
            final int otp = rand.nextInt(999999);
            veri_code = "Your OTP is " + otp +"\nDo not share this with anyone.";
            if(checkPermission(Manifest.permission.SEND_SMS)) {
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(mobile, null,
                        veri_code, null, null);
                progressbar.setVisibility(View.VISIBLE);
            }
            else {
                Toast.makeText(OrderConfirmation.this,
                        "Unable to send OTP at " + mobile, Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(OrderConfirmation.this,
                        PhoneNumberPage.class);
                startActivity(intent);
                Toast.makeText(OrderConfirmation.this,
                        "Try Again !", Toast.LENGTH_SHORT).show();
            }
            return otp;
        }

    }




