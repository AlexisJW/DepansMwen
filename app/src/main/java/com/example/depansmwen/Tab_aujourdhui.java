package com.example.depansmwen;



import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
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
    Integer depenseSuprim = 0;
    Integer id, aa;

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
                public void onItemClick(final int position) {
                    AlertDialog.Builder alertDeleteSupprimer = new AlertDialog.Builder(getContext());
                    alertDeleteSupprimer.setTitle("Voulez vous supprimer cette Depense?");
                    alertDeleteSupprimer.setIcon(R.drawable.delete);
                    alertDeleteSupprimer.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Boolean delete = accesLocal.deleteDepense(allInformationsToday.get(position).getId());
                            if (delete == true){
                                AlldepenseToday();
                                allInformationsToday.remove(position);
                                monAdapter.notifyItemRemoved(position);
                            }
                        }
                    });
                    alertDeleteSupprimer.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    alertDeleteSupprimer.show();
                }
            });
            AlldepenseToday();
        }else{
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this.getContext(), "N'oubliez pas d'ajouter vos depenses quotidiennes!!", Toast.LENGTH_LONG).show();
        }
    }

    public void AlldepenseToday(){
        Integer all = accesLocal.AllDepenseToday();
        tvAllDepense.setText("Depense d'aujourd'hui est: "+all +" Gourde(s)");
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
}
