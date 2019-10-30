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

public class GetStartedActivity extends AppCompatActivity {

    Animation ttb, btt;
    ImageView emblem;
    TextView intro;
    Button btn_sign_in, btn_new_account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_started);

        emblem = findViewById(R.id.emblem);
        intro = findViewById(R.id.textView);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        btn_new_account = findViewById(R.id.btn_new_account);

        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);

        emblem.startAnimation(ttb);
        intro.startAnimation(ttb);
        btn_sign_in.startAnimation(btt);
        btn_new_account.startAnimation(btt);

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sign_in = new Intent(GetStartedActivity.this, SignInActivity.class);
                startActivity(sign_in);
                finish();
            }
        });

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAccount = new Intent(GetStartedActivity.this, RegisterOneActivity.class);
                startActivity(newAccount);
                finish();
            }
        });
    }
}
