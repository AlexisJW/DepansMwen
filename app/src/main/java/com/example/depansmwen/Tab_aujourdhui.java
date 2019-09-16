package com.example.depansmwen;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Tab_aujourdhui extends Fragment {
    private View.OnClickListener listener;
    View v;
    AccesLocal accesLocal;

    private ArrayList<InformationToday> allInformationsToday = new ArrayList<>();
    private ArrayList<InformationToday> asdf = new ArrayList<>();
    private InformationTodayAdapter monAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    TextView tvAllDepense;
    String recherche ="";
    String montant ="";

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
        v   =inflater.inflate(R.layout.tab_aujourdhui,container,false);

        refreshTabAujourdhui();
        return v;
    }

    public void testMontant(int position, String montant){

    }

    public void refreshTabAujourdhui(){
        accesLocal = new AccesLocal(this.getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.IdRecycleView);
        tvAllDepense = (TextView) v.findViewById(R.id.tvAllDepense);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        //recyclerView.setHasFixedSize(true);
        allInformationsToday = accesLocal.ListInformationFromBd();
        if (allInformationsToday.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            monAdapter = new InformationTodayAdapter(this.getContext(), allInformationsToday);
            recyclerView.setAdapter(monAdapter);
            AlldepenseToday();
            monAdapter.SetOnItemClickListener(new InformationTodayAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(int position) {
//                    allInformationsToday.remove(position);
//                    monAdapter.notifyItemChanged(position);
                }
            });
            AlldepenseToday();
        }else{
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this.getContext(), "Il n'a pas d'enregistrement de depense dans la base de donnees pour aujourd'hui!", Toast.LENGTH_LONG).show();
        }
    }

    public void AlldepenseToday(){
        Integer all = accesLocal.AllDepenseToday();
        tvAllDepense.setText("Depense d'aujourd'hui est: "+all);
    }

    public void GestionRecherche(){
        if (monAdapter != null){
           monAdapter.getFilter().filter(recherche);
        }
    }

    public String textRecherche(String txtRecherche){
        recherche = txtRecherche;
        return txtRecherche;
    }

//    public String textMontant(String txtMontant){
//        return montant= txtMontant;
//    }

}
