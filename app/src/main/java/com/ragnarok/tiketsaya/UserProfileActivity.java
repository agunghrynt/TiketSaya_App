package com.ragnarok.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    LinearLayout item_ticket;
    RecyclerView user_ticket_place;
    Button editProfile, signOut, btn_back_home;
    TextView nama_lengkap, bio, nama_wisata, lokasi, jumlah_tiket;
    ImageView user_profile_photo;

    DatabaseReference reference, reference2;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    ArrayList<UserTicket> list;
    TicketAdapter ticketAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        getUsernameLocal();

        item_ticket = findViewById(R.id.item_ticket);
        editProfile = findViewById(R.id.btn_edit_profile);
        signOut = findViewById(R.id.btn_signOut);
        btn_back_home = findViewById(R.id.btn_back_home);
        nama_lengkap = findViewById(R.id.nama_lengkap);
        bio = findViewById(R.id.bio);
        user_profile_photo =findViewById(R.id.user_profile_photo);
        user_ticket_place =findViewById(R.id.user_ticket_place);
        user_ticket_place.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<UserTicket>();

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                nama_lengkap.setText(dataSnapshot.child("nama_lengkap").getValue().toString());
                bio.setText(dataSnapshot.child("bio").getValue().toString());
                Picasso.with(UserProfileActivity.this)
                        .load(dataSnapshot.child("url_photo_profile").getValue().toString())
                        .centerCrop().fit().into(user_profile_photo);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

//        item_ticket.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent ticketInfo = new Intent(UserProfileActivity.this, UserTicketDetailActivity.class);
//                startActivity(ticketInfo);
//                finish();
//            }
//        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent editInfo = new Intent(UserProfileActivity.this, EditProfileActivity.class);
                startActivity(editInfo);
            }
        });

        btn_back_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gotoHome = new Intent(UserProfileActivity.this, HomeActivity.class);
                startActivity(gotoHome);
            }
        });

        reference2 = FirebaseDatabase.getInstance().getReference()
                .child("User_Ticket").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1: dataSnapshot.getChildren()){
                    UserTicket p = dataSnapshot1.getValue(UserTicket.class);
                    list.add(p);
                }
                ticketAdapter = new TicketAdapter(UserProfileActivity.this, list);
                user_ticket_place.setAdapter(ticketAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        signOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // menyimpan username ke local storage
                SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString(username_key, null);
                editor.apply();

                Intent signOut = new Intent(UserProfileActivity.this, GetStartedActivity.class);
                startActivity(signOut);
                finish();
                isDestroyed();
            }
        });
    }

    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }

}
