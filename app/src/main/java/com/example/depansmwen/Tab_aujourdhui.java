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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class Tab_aujourdhui extends Fragment {
      private View.OnClickListener listener;
    View v;
    AccesLocal accesLocal;

    private ArrayList<InformationToday> allInformationsToday = new ArrayList<>();
    private InformationTodayAdapter monAdapter;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,Bundle savedInstanceState) {
         v   =inflater.inflate(R.layout.tab_aujourdhui,container,false);

        accesLocal = new AccesLocal(this.getContext());
        recyclerView = (RecyclerView) v.findViewById(R.id.IdRecycleView);
        linearLayoutManager = new LinearLayoutManager(this.getContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        allInformationsToday = accesLocal.ListInformationFromBd();
        if (allInformationsToday.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            monAdapter = new InformationTodayAdapter(this.getContext(), allInformationsToday);
            recyclerView.setAdapter(monAdapter);
        }else{
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this.getContext(), "Il n'a pas d'enregistrement de depense dans la base de donnees pour aujourd'hui!", Toast.LENGTH_LONG).show();
        }

         //Load();
        return v;
    }

    public  void refreshTabAujourdhui(){
        allInformationsToday = accesLocal.ListInformationFromBd();
        if (allInformationsToday.size() > 0){
            recyclerView.setVisibility(View.VISIBLE);
            monAdapter = new InformationTodayAdapter(this.getContext(), allInformationsToday);
            recyclerView.setAdapter(monAdapter);
        }else{
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this.getContext(), "Il n'a pas d'enregistrement de depense dans la base de donnees pour aujourd'hui!", Toast.LENGTH_LONG).show();
        }
    }

}
