package com.ragnarok.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.github.florent37.shapeofview.shapes.CircleView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {

    LinearLayout btn_ticket_pisa, btn_ticket_torri, btn_ticket_pagoda,btn_ticket_temple, btn_ticket_sphinx, btn_ticket_monas;
    CircleView btn_profile;
    TextView user_balance, nama_lengkap, bio;
    ImageView user_home_photo;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        getUsernameLocal();

        btn_ticket_pisa = findViewById(R.id.btn_ticket_pisa);
        btn_ticket_torri = findViewById(R.id.btn_ticket_torri);
        btn_ticket_pagoda = findViewById(R.id.btn_ticket_pagoda);
        btn_ticket_temple = findViewById(R.id.btn_ticket_temple);
        btn_ticket_sphinx = findViewById(R.id.btn_ticket_sphinx);
        btn_ticket_monas= findViewById(R.id.btn_ticket_monas);
        btn_profile = findViewById(R.id.btn_profile);
        user_home_photo = findViewById(R.id.user_home_photo);
        user_balance = findViewById(R.id.user_balance);
        nama_lengkap = findViewById(R.id.home_nama_lengkap);
        bio = findViewById(R.id.home_bio);

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                user_balance.setText("$ " + dataSnapshot.child("user_balance").getValue().toString());

                Picasso.with(HomeActivity.this)
                        .load(dataSnapshot.child("url_photo_profile")
                                .getValue().toString()).centerCrop().fit().into(user_home_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btn_ticket_pisa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailPisa = new Intent(HomeActivity.this, TicketDetailActivity.class);
                detailPisa.putExtra("Jenis_Tiket","Pisa");
                startActivity(detailPisa);
            }
        });

        btn_ticket_torri.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailTorri = new Intent(HomeActivity.this, TicketDetailActivity.class);
                detailTorri.putExtra("Jenis_Tiket","Torri");
                startActivity(detailTorri);
                finish();
            }
        });

        btn_ticket_pagoda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailPagoda = new Intent(HomeActivity.this, TicketDetailActivity.class);
                detailPagoda.putExtra("Jenis_Tiket","Pagoda");
                startActivity(detailPagoda);
                finish();
            }
        });

        btn_ticket_temple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailTemple = new Intent(HomeActivity.this, TicketDetailActivity.class);
                detailTemple.putExtra("Jenis_Tiket","Temple");
                startActivity(detailTemple);
                finish();
            }
        });

        btn_ticket_sphinx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailSphinx = new Intent(HomeActivity.this, TicketDetailActivity.class);
                detailSphinx.putExtra("Jenis_Tiket","Sphinx");
                startActivity(detailSphinx);
                finish();
            }
        });

        btn_ticket_monas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent detailMonas = new Intent(HomeActivity.this, TicketDetailActivity.class);
                detailMonas.putExtra("Jenis_Tiket","Monas");
                startActivity(detailMonas);
                finish();
            }
        });

        btn_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userProfile = new Intent(HomeActivity.this, UserProfileActivity.class);
                startActivity(userProfile);
                finish();
            }
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
