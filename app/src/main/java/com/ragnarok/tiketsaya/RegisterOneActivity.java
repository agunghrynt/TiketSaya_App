package com.ragnarok.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterOneActivity extends AppCompatActivity {

    LinearLayout btn_back;
    Button btn_continue;
    EditText Username, Password, EmailAddress;
    DatabaseReference reference, username_reference;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_one);

        Username = findViewById(R.id.username);
        Password = findViewById(R.id.passowrd);
        EmailAddress = findViewById(R.id.email_address);
        btn_back = findViewById(R.id.btn_back);
        btn_continue = findViewById(R.id.btn_continue);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(RegisterOneActivity.this,SignInActivity.class);
                startActivity(back);
                finish();
            }
        });

        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btn_continue.setEnabled(false);
                btn_continue.setText("LOADING...");

                username_reference = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(Username.getText().toString());
                username_reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            btn_continue.setEnabled(true);
                            btn_continue.setText("CONTINUE");

                            Toast.makeText(getApplicationContext(), "Username sudah dipakai!", Toast.LENGTH_SHORT).show();
                        } else {
                            // menyimpan username ke local storage
                            SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(username_key, Username.getText().toString());
                            editor.apply();

                            // menyimpan username ke database
                            reference = FirebaseDatabase.getInstance().getReference().child("Users")
                                    .child(Username.getText().toString());
                            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    dataSnapshot.getRef().child("username").setValue(Username.getText().toString());
                                    dataSnapshot.getRef().child("password").setValue(Password.getText().toString());
                                    dataSnapshot.getRef().child("email_address").setValue(EmailAddress.getText().toString());
                                    dataSnapshot.getRef().child("user_balance").setValue(1000);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            Intent next = new Intent(RegisterOneActivity.this, RegisterTwoActivity.class);
                            startActivity(next);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });
    }
}
