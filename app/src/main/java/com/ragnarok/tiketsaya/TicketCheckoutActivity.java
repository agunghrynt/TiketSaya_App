package com.ragnarok.tiketsaya;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
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

import java.util.Random;

public class TicketCheckoutActivity extends AppCompatActivity {

    Button btnBuy, btnMin, btnPls;
    TextView totalTicket, myBalance, totalPrice, ticket, ticket_location, short_tos_ticket;
    ImageView warning;
    Integer valTicket = 1;
    Integer balance = 0;
    Integer valTotal = 0;
    Integer valPrice = 0;
    Integer sisa_balance = 0;
    LinearLayout btnBack;

    Integer no_transaksi = new Random().nextInt();

    DatabaseReference reference, reference2, reference3, reference4;

    String USERNAME_KEY = "usernamekey";
    String username_key = "";
    String username_key_new = "";

    String date_wisata = "";
    String time_wisata = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ticket_checkout);

        getUsernameLocal();

        Bundle bundle = getIntent().getExtras();
        final String jenis_tiket_baru = bundle.getString("Jenis_Tiket");

        btnBuy = findViewById(R.id.btn_buy_ticket);
        btnBack = findViewById(R.id.btn_back);
        myBalance = findViewById(R.id.my_balance);
        totalPrice = findViewById(R.id.total_price);
        totalTicket = findViewById(R.id.total_ticket);
        ticket = findViewById(R.id.ticket);
        ticket_location = findViewById(R.id.ticket_location);
        short_tos_ticket = findViewById(R.id.short_tos_ticket);
        warning = findViewById(R.id.not_enought);
        btnMin = findViewById(R.id.btn_min);
        btnPls = findViewById(R.id.btn_pls);

        warning.setVisibility(View.GONE);
        totalTicket.setText(valTicket.toString());

        reference2 = FirebaseDatabase.getInstance().getReference()
                .child("Users").child(username_key_new);
        reference2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                balance = Integer.valueOf(dataSnapshot.child("user_balance").getValue().toString());
                myBalance.setText("$ " + balance +"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference()
                .child("Wisata").child("Wisata").child(jenis_tiket_baru);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ticket.setText(dataSnapshot.child("nama_wisata").getValue().toString());
                ticket_location.setText(dataSnapshot.child("lokasi").getValue().toString());
                short_tos_ticket.setText(dataSnapshot.child("ketentuan").getValue().toString());

                date_wisata = dataSnapshot.child("date_wisata").getValue().toString();
                time_wisata = dataSnapshot.child("time_wisata").getValue().toString();

                valPrice = Integer.valueOf(dataSnapshot.child("harga_tiket").getValue().toString());
                valTotal = valPrice * valTicket;
                totalPrice.setText("$ " + valTotal +"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        btnMin.animate().alpha(0).setDuration(300).start();
        btnMin.setEnabled(false);
        btnMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valTicket-=1;
                totalTicket.setText(valTicket.toString());
                if (valTicket < 2) {
                    btnMin.animate().alpha(0).setDuration(300).start();
                    btnMin.setEnabled(false);
                }
                valTotal = valPrice * valTicket;
                totalPrice.setText("$ " + valTotal +"");
                if (valTotal <= balance) {
                    btnBuy.animate().translationX(0).alpha(1).setDuration(300).start();
                    btnBuy.setEnabled(true);
                    myBalance.setTextColor(Color.parseColor("#203DD1"));
                    warning.setVisibility(View.GONE);
                }
            }
        });

        btnPls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                valTicket+=1;
                totalTicket.setText(valTicket.toString());
                if (valTicket > 1) {
                    btnMin.animate().alpha(1).setDuration(300).start();
                    btnMin.setEnabled(true);
                }
                valTotal = valPrice * valTicket;
                totalPrice.setText("$ " + valTotal +"");
                if (valTotal > balance) {
                    btnBuy.animate().translationX(300).alpha(0).setDuration(300).start();
                    btnBuy.setEnabled(false);
                    myBalance.setTextColor(Color.parseColor("#D1206B"));
                    warning.setVisibility(View.VISIBLE);
                }
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reference3 = FirebaseDatabase.getInstance().getReference()
                        .child("User_Ticket").child(username_key_new).child(ticket.getText().toString() + no_transaksi);
                reference3.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        reference3.getRef().child("id_ticket").setValue(ticket.getText().toString() + no_transaksi);
                        reference3.getRef().child("nama_wisata").setValue(ticket.getText().toString());
                        reference3.getRef().child("lokasi").setValue(ticket_location.getText().toString());
                        reference3.getRef().child("ketentuan").setValue(short_tos_ticket.getText().toString());
                        reference3.getRef().child("date_wisata").setValue(date_wisata);
                        reference3.getRef().child("time_wisata").setValue(time_wisata);
                        reference3.getRef().child("jumlah_tiket").setValue(valTicket.toString());

                        Intent buy = new Intent(TicketCheckoutActivity.this, SuccesBuyActivity.class);
                        startActivity(buy);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                reference4 = FirebaseDatabase.getInstance().getReference()
                        .child("Users").child(username_key_new);

                reference4.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        sisa_balance = balance - valTotal;
                        reference4.getRef().child("user_balance").setValue(sisa_balance);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent back = new Intent(TicketCheckoutActivity.this, TicketDetailActivity.class);
                back.putExtra("Jenis_Tiket",jenis_tiket_baru);
                startActivity(back);
            }
        });

    }


    public void getUsernameLocal() {
        SharedPreferences sharedPreferences = getSharedPreferences(USERNAME_KEY, MODE_PRIVATE);
        username_key_new = sharedPreferences.getString(username_key,"");
    }
}
