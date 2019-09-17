package com.example.depansmwen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class accueil extends AppCompatActivity {

    Spinner spinnerCategorie;
    Spinner spinnerCompte;
    private TextInputLayout etPrix;
    Spinner spinnerDevise;
    private TextInputLayout etNote;
    private static AccesLocal accesLocal;
    static MainActivity user;
    SimpleDateFormat sdf;
    String currentDateandTime;
    View view;
    Tab_aujourdhui tab_cat = new Tab_aujourdhui();
    Tab_semaine tab_sem = new Tab_semaine();
    Tab_mois tab_m = new Tab_mois();
    FloatingActionButton floatingActionButtonAjouterDepense;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil1);
        accesLocal = new AccesLocal(accueil.this);
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        sdf = new SimpleDateFormat("yyyy.MM.dd");
        currentDateandTime = sdf.format(new Date());

        floatingActionButtonAjouterDepense = findViewById(R.id.floatingActionButtonAjouterDepense);
        floatingActionButtonAjouterDepense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder depCategorie = new AlertDialog.Builder(accueil.this);
                depCategorie.setTitle("Ajouter une Depense");

                loadSpinnerData();

                etPrix = (TextInputLayout) view.findViewById(R.id.cd);

                etNote = (TextInputLayout) view.findViewById(R.id.etNote);

                depCategorie.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String textCategorie = String.valueOf(spinnerCategorie.getSelectedItem());
                        String textCompte = String.valueOf(spinnerCompte.getSelectedItem());
                        String prix= etPrix.getEditText().getText().toString();
                        String note= etNote.getEditText().getText().toString();

                        if (prix.equals("") ){
                            Toast.makeText(accueil.this, "Tous les champs sont obligatoires!!!"+textCategorie+" et "+prix, Toast.LENGTH_SHORT).show();
                        }else{
                            Boolean insert = accesLocal.AddCategorie(textCategorie, prix, "HTG", note, currentDateandTime, String.valueOf(user.userName()), textCompte);
                            if (insert == true){
                                Toast.makeText(accueil.this, "enregistrement Depense avec succes!!!", Toast.LENGTH_SHORT).show();
                                tab_cat.refreshTabAujourdhui();
                                tab_cat.AlldepenseToday();
                                tab_sem.refreshTabSemaine();
                                //Alldepense();
                            }else{
                                Toast.makeText(accueil.this, "enregistrement echouee!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
                depCategorie.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                depCategorie.setView(view);
                AlertDialog dialog= depCategorie.create();
                dialog.show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder alertDeconnect = new AlertDialog.Builder(accueil.this);
        alertDeconnect.setTitle("Voulez vous deconnecter?");
        alertDeconnect.setIcon(R.drawable.logo);
        alertDeconnect.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                System.exit(0);
            }
        });
        alertDeconnect.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDeconnect.show();

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(accueil mainActivity, FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch(position){
                case 0:
                    tab_cat = new Tab_aujourdhui();
                    return tab_cat;
                case 1:
                     tab_sem = new Tab_semaine();
                    return tab_sem;
                case 2:
                     tab_m = new Tab_mois();
                    return tab_m;

                default:
                    return null;

            }
        }

        @Override
        public int getCount() {
            return 3;
        }


        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return "AUJOURD'HUI";
                case 1:
                    return "SEMAINE";
                case 2:
                    return "MOIS";

            }
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);

        MenuItem search = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(search);
        search(searchView);

        return true;
    }

    private void search(SearchView searchView) {

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tab_cat.textRecherche(newText);
                tab_cat.GestionRecherche();

                tab_m.textRecherche(newText);
                tab_m.GestionRecherche();

                tab_sem.textRecherche(newText);
                tab_sem.GestionRecherche();
                return true;
            }
        });
    }

    private void loadSpinnerData() {
        //spinnerCompte
        List<String> labels = accesLocal.getAllSpinners1();
        List<String> labels1 = accesLocal.getAllSpinnerscompte1();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels1);
        //ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, labels);
        view=getLayoutInflater().inflate(R.layout.depense,null);
        spinnerCategorie = (Spinner) view.findViewById(R.id.spinnerCategorie);
        spinnerCompte =view.findViewById(R.id.spinnerCompte);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerCategorie.setAdapter(dataAdapter);
        spinnerCompte.setAdapter(dataAdapter1);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.apropos) {
            final AlertDialog.Builder apropos = new AlertDialog.Builder(this);
            apropos.setTitle("A Propos");
            apropos.setMessage("Cette Application a été developpé par 6 Developpeurs de l’université INUKA.");

            apropos.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
            apropos.show();

            return true;
        }

        if (id == R.id.parametre) {
            Toast.makeText(this,"Parametre",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Parametre.class));
            return true;
        }

        if (id == R.id.logout) {
            Toast.makeText(this,"Parametre",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(accueil.this,MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
