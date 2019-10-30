package com.ragnarok.tiketsaya;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class SuccesBuyActivity extends AppCompatActivity {

    Animation app_splash, btt, ttb;
    Button myDashboard, btnviewTicket;
    ImageView mSucces;
    TextView mTextview, mTextview2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_succes_buy);

        mSucces = findViewById(R.id.succes_buy);
        mTextview = findViewById(R.id.textView);
        mTextview2 = findViewById(R.id.textView2);
        myDashboard = findViewById(R.id.btn_myDashboard);
        btnviewTicket = findViewById(R.id.btn_viewTicket);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        mSucces.startAnimation(app_splash);
        mTextview.startAnimation(ttb);
        mTextview2.startAnimation(ttb);
        myDashboard.startAnimation(btt);
        btnviewTicket.startAnimation(btt);

        myDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent dashboard = new Intent(SuccesBuyActivity.this, HomeActivity.class);
                startActivity(dashboard);
            }
        });

        btnviewTicket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myTicket = new Intent(SuccesBuyActivity.this, UserProfileActivity.class);
                startActivity(myTicket);
            }
        });
    }
}
