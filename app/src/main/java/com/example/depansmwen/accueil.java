package com.example.depansmwen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
    private TextInputLayout etPrix;
    Spinner spinnerDevise;
    private TextInputLayout etNote;
    private static AccesLocal accesLocal;
    static MainActivity user;
    SimpleDateFormat sdf;
    String currentDateandTime;
    View view;
    Tab_aujourdhui tab_cat;
    Tab_semaine tab_sem;
    Tab_mois tab_m;
    Integer allDepense = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil1);
        getSupportActionBar().setTitle("Vue d'ensemble ...");
        //Toast.makeText(this," "+String.valueOf(user.userName()),Toast.LENGTH_LONG).show();
        accesLocal = new AccesLocal(accueil.this);

        //getSupportActionBar().hide();
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        sdf = new SimpleDateFormat("yyyy.MM.dd");
        currentDateandTime = sdf.format(new Date());
    }

public void ab(){
    Toast.makeText(accueil.this , "li klike!!!", Toast.LENGTH_SHORT).show();
}

    public String getCurrentDateandTime() {
        return currentDateandTime;
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
        return true;
    }

    private void loadSpinnerData() {
        List<String> labels = accesLocal.getAllSpinners();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        //ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, labels);

        view=getLayoutInflater().inflate(R.layout.depense,null);

        spinnerCategorie = (Spinner) view.findViewById(R.id.spinnerCategorie);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerCategorie.setAdapter(dataAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.adddepense) {
            Toast.makeText(this,"Ajouter Depense",Toast.LENGTH_SHORT).show();
            final AlertDialog.Builder depCategorie = new AlertDialog.Builder(accueil.this);
            depCategorie.setTitle("Ajouter une Depense");

            loadSpinnerData();
//            View view=getLayoutInflater().inflate(R.layout.depense,null);
//
//            spinnerCategorie = (Spinner) view.findViewById(R.id.spinnerCategorie);
            etPrix = (TextInputLayout) view.findViewById(R.id.etPrix);
            spinnerDevise = (Spinner) view.findViewById(R.id.spinnerDevise);
            etNote = (TextInputLayout) view.findViewById(R.id.etNote);

            depCategorie.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String textCategorie = String.valueOf(spinnerCategorie.getSelectedItem());
                    String textDevise = String.valueOf(spinnerDevise.getSelectedItem());
                     String prix= etPrix.getEditText().getText().toString();
                     String note= etNote.getEditText().getText().toString();

                    if (textCategorie.equals("") ){
                        Toast.makeText(accueil.this, "Tous les champs sont obligatoires!!!"+textCategorie+" et "+prix, Toast.LENGTH_SHORT).show();
                    }else{
                            Boolean insert = accesLocal.AddCategorie(textCategorie, prix, textDevise, note, currentDateandTime, String.valueOf(user.userName()));
                            if (insert == true){
                                Toast.makeText(accueil.this, "enregistrement Depense avec succes!!!", Toast.LENGTH_SHORT).show();
                                tab_cat.refreshTabAujourdhui();
                                tab_cat.AlldepenseToday();
                                tab_sem.refreshTabSemaine();
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
            return true;
        }

        if (id == R.id.apropos) {

//            Toast.makeText(this,"A Propos",Toast.LENGTH_SHORT).show();

                    final AlertDialog.Builder apropos = new AlertDialog.Builder(this);
                 apropos.setTitle("A Propos");
                 apropos.setMessage("Cette Application a été developpé par 6 Developpeurs de l’université INUKA.");
//                    final EditText input=new EditText(this);
//                    input.setInputType(InputType.TYPE_CLASS_TEXT);
//                    newFolderDialog.setView(input);


            apropos.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
//                apropos.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.cancel();
//                        }
//                    });
            apropos.show();




            return true;

        }

        if (id == R.id.parametre) {

            Toast.makeText(this,"Parametre",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this,Parametre.class));
            return true;

        }

        return super.onOptionsItemSelected(item);
    }

}
