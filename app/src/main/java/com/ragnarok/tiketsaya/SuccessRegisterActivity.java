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

public class SuccessRegisterActivity extends AppCompatActivity {

    Animation app_splash, btt, ttb;
    Button explore;
    TextView textView2, textView3;
    ImageView succesRegis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_register);

        succesRegis = findViewById(R.id.succes_register);
        textView2 = findViewById(R.id.textView2);
        textView3 = findViewById(R.id.textView3);
        explore = findViewById(R.id.btn_explore);

        app_splash = AnimationUtils.loadAnimation(this, R.anim.app_splash);
        btt = AnimationUtils.loadAnimation(this, R.anim.btt);
        ttb = AnimationUtils.loadAnimation(this, R.anim.ttb);

        succesRegis.startAnimation(app_splash);
        textView2.startAnimation(ttb);
        textView3.startAnimation(ttb);
        explore.startAnimation(btt);

        explore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent explore = new Intent(SuccessRegisterActivity.this, HomeActivity.class);
                startActivity(explore);
            }
        });
    }
}
