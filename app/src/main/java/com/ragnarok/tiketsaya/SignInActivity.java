package com.ragnarok.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignInActivity extends AppCompatActivity {

    TextView btn_new_account;
    EditText mUsername, mPassword;
    Button btn_sign_in;
    String USERNAME_KEY = "usernamekey";
    String username_key = "";

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        btn_new_account = findViewById(R.id.btn_new_account);
        btn_sign_in = findViewById(R.id.btn_sign_in);
        mUsername = findViewById(R.id.sign_username);
        mPassword = findViewById(R.id.sign_password);

        btn_new_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newAccount = new Intent(SignInActivity.this, RegisterOneActivity.class);
                startActivity(newAccount);
                finish();
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // ubah state jadi loading
                btn_sign_in.setEnabled(false);
                btn_sign_in.setText("LOADING...");

                final String Username = mUsername.getText().toString();
                final String Password = mPassword.getText().toString();

                if (Username.isEmpty()){

                    Toast.makeText(getApplicationContext(),"Username tidak ada!", Toast.LENGTH_SHORT).show();

                    btn_sign_in.setEnabled(true);
                    btn_sign_in.setText("SIGN IN");

                } else {

                    if (Password.isEmpty()){

                        Toast.makeText(getApplicationContext(),"Password tidak ada!", Toast.LENGTH_SHORT).show();

                        btn_sign_in.setEnabled(true);
                        btn_sign_in.setText("SIGN IN");

                    } else {

                        reference = FirebaseDatabase.getInstance().getReference().child("Users").child(Username);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()){

                                    // ambil data dari firebase
                                    String passwordFromFirebase = dataSnapshot.child("password").getValue().toString();

                                    // validasi password dengan firebase
                                    if (Password.equals(passwordFromFirebase)){

                                        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
                                        SharedPreferences.Editor editor = sharedPreferences.edit();
                                        editor.putString(username_key, mUsername.getText().toString());
                                        editor.apply();

                                        Intent signIn = new Intent(SignInActivity.this,HomeActivity.class);
                                        startActivity(signIn);

                                    } else {

                                        btn_sign_in.setEnabled(true);
                                        btn_sign_in.setText("SIGN IN");
                                        Toast.makeText(getApplicationContext(), "Password salah!", Toast.LENGTH_SHORT).show();

                                    }

                                } else {
                                    btn_sign_in.setEnabled(true);
                                    btn_sign_in.setText("SIGN IN");
                                    Toast.makeText(getApplicationContext(),"Username tidak ada!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
        });
    }
}
