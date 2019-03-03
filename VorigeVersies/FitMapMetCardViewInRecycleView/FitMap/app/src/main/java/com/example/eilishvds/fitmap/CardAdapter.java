package com.example.eilishvds.fitmap;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.MyViewHolder> {

    private Context mContext;
    private List<Prestatie> prestatieLijst;

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public TextView aantalKilometer, tijd, naam;

        public  MyViewHolder (View view){
            super(view);

            aantalKilometer = (TextView)view.findViewById(R.id.card_aantalKilometer);
            tijd = (TextView)view.findViewById(R.id.card_tijd);
            naam = (TextView)view.findViewById(R.id.card_naam);
        }
    }

    public CardAdapter(Context mContext, List<Prestatie> prestatieLijst){
        this.mContext = mContext;
        this.prestatieLijst = prestatieLijst;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.prestatie_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position){
        Prestatie prestatie = prestatieLijst.get(position);
        holder.naam.setText(prestatie.getNaam());
        holder.aantalKilometer.setText(prestatie.getAantalKilometer());
        holder.tijd.setText(prestatie.getTijd());
    }

    @Override
    public int getItemCount() {
        return prestatieLijst.size();
    }
}
