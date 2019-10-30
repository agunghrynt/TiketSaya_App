package com.ragnarok.tiketsaya;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TicketAdapter extends RecyclerView.Adapter<TicketAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserTicket> userTickets;

    public TicketAdapter(Context c, ArrayList<UserTicket> p) {
        context = c;
        userTickets = p;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater
                .from(context).inflate(R.layout.item_user_tickets, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.xnama_wisata.setText(userTickets.get(position).getNama_wisata());
        holder.xlokasi.setText(userTickets.get(position).getLokasi());
        holder.xjumlah_tiket.setText(userTickets.get(position).getJumlah_tiket() + " Tickets");

        final String getNamaWisata = userTickets.get(position).getNama_wisata();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent userticketdetail = new Intent(context, UserTicketDetailActivity.class);
                userticketdetail.putExtra("nama_wisata", getNamaWisata);
                context.startActivity(userticketdetail);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userTickets.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView xnama_wisata, xlokasi, xjumlah_tiket;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            xnama_wisata = itemView.findViewById(R.id.xnama_wisata);
            xlokasi = itemView.findViewById(R.id.xlokasi);
            xjumlah_tiket = itemView.findViewById(R.id.xjumlah_tiket);
        }
    }

}
