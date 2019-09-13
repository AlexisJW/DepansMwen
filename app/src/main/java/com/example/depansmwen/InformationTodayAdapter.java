package com.example.depansmwen;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class InformationTodayAdapter extends RecyclerView.Adapter<InformationTodayAdapter.InformationTodayViewHolder> {

    private Context MonContext;
    private ArrayList<InformationToday> listInformationToday;
    private ArrayList<InformationToday> monArrayList;



    public class InformationTodayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView montantViewHolder;
        public TextView montant;
        public TextView categorieViewHolder;
        public TextView categorie;
        public TextView deviseViewHolder;
        public TextView devise;
        public TextView noteViewHolder;
        public TextView note;

        public InformationTodayViewHolder(@NonNull View itemView) {
            super(itemView);
            montantViewHolder = itemView.findViewById(R.id.tvMontant);
            montant = itemView.findViewById(R.id.idmont);
            categorieViewHolder = itemView.findViewById(R.id.tvCategorie);
            categorie = itemView.findViewById(R.id.idCat);
            deviseViewHolder = itemView.findViewById(R.id.tvDevise);
            devise = itemView.findViewById(R.id.idDevise);
            noteViewHolder = itemView.findViewById(R.id.tvNote);
            note = itemView.findViewById(R.id.idNote);
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION){
                //Toast.makeText(MonContext , "li klike!!!", Toast.LENGTH_SHORT).show();

                //InformationToday informationToday = listInformationToday.get(position);

                Intent intent = new Intent(MonContext, inscription.class);
                //intent.putExtra(Tweet.class.getSimpleName(), Parcels.wrap(tweet));
                MonContext.startActivity(intent);
            }
        }
    }

    public InformationTodayAdapter(Context monContext, ArrayList<InformationToday> listInformationToday) {
        MonContext = monContext;
        this.listInformationToday = listInformationToday;
        this.monArrayList = listInformationToday;
    }

    @NonNull
    @Override
    public InformationTodayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(MonContext).inflate(R.layout.posttoday, viewGroup, false);
        return new InformationTodayViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InformationTodayViewHolder Holder, int position) {
     final InformationToday informationsToday = listInformationToday.get(position);

      Holder.montantViewHolder.setText(informationsToday.getMontant());
      Holder.montant.setText("MONTANT: ");
      Holder.categorieViewHolder.setText(informationsToday.getCategorie());
      Holder.categorie.setText("CATEGORIE: ");
      Holder.deviseViewHolder.setText(informationsToday.getDevise());
      Holder.devise.setText("DEVISE: ");
      Holder.note.setText("NOTE: ");
      Holder.noteViewHolder.setText(informationsToday.getNote());
    }

    @Override
    public int getItemCount() {
        return listInformationToday.size();
    }
}
