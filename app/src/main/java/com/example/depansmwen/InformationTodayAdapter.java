package com.example.depansmwen;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class InformationTodayAdapter extends RecyclerView.Adapter<InformationTodayAdapter.InformationTodayViewHolder> {

    private Context MonContext;
    private ArrayList<InformationToday> listInformationToday;
    private ArrayList<InformationToday> monArrayList;
    private OnItemClickListener monOnItemClickListener;


    public InformationTodayAdapter(Context monContext, ArrayList<InformationToday> listInformationToday) {
        MonContext = monContext;
        this.listInformationToday = listInformationToday;
        this.monArrayList = listInformationToday;
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void SetOnItemClickListener(OnItemClickListener listener){
     monOnItemClickListener = listener;
    }

    public class InformationTodayViewHolder extends RecyclerView.ViewHolder {

        public TextView montantViewHolder;
        public TextView montant;
        public TextView categorieViewHolder;
        public TextView categorie;
        public TextView deviseViewHolder;
        public TextView compteViewHolder;
        public TextView devise;
        public TextView noteViewHolder;
        public TextView note;
        public TextView compte;
        public Integer id;

        public InformationTodayViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            montantViewHolder = itemView.findViewById(R.id.tvMontant);
            montant = itemView.findViewById(R.id.idmont);
            categorieViewHolder = itemView.findViewById(R.id.tvCategorie);
            categorie = itemView.findViewById(R.id.idCat);

            deviseViewHolder = itemView.findViewById(R.id.tvDevise);
            devise = itemView.findViewById(R.id.idDevise);
            compteViewHolder = itemView.findViewById(R.id.tvCompte);
            compte = itemView.findViewById(R.id.idCompte);

            noteViewHolder = itemView.findViewById(R.id.tvNote);
            note = itemView.findViewById(R.id.idNote);

            id = 0;

//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (listener != null){
//                        int position = getAdapterPosition();
//                        if (position != RecyclerView.NO_POSITION){
//                          listener.onItemClick(position);
//                        }
//                    }
//                }
//            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                    return false;
                }
            });
        }
    }

    @NonNull
    @Override
    public InformationTodayViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(MonContext).inflate(R.layout.posttoday, viewGroup, false);
        return new InformationTodayViewHolder(view, monOnItemClickListener);
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
      Holder.compteViewHolder.setText(informationsToday.getCompte());
      Holder.compte.setText("COMPTE: ");

      Holder.note.setText("NOTE: ");
      Holder.noteViewHolder.setText(informationsToday.getNote());

      Holder.id = informationsToday.getId();
    }

    public Filter getFilter(){
        return filerRecherche;
    }

    @Override
    public int getItemCount() {
        return listInformationToday.size();
    }

    private Filter filerRecherche = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            String charString = constraint.toString();
            if (charString == null || charString.length() == 0) {
                listInformationToday = monArrayList;
            } else {
                ArrayList<InformationToday> filteredList = new ArrayList<>();

               for (InformationToday informationToday : monArrayList) {
                    if (informationToday.getCategorie().toLowerCase().contains(constraint.toString())) {
                        filteredList.add(informationToday);
                    }
                }
                listInformationToday = filteredList;
               }

            FilterResults results = new FilterResults();
            results.values = listInformationToday;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            listInformationToday = (ArrayList<InformationToday>) results.values;
            notifyDataSetChanged();
        }
    };
}
