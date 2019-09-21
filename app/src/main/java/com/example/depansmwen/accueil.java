package com.example.depansmwen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.TextViewCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
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
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

    View viewModifierCat;
    View viewDeleteCat;
    View  viewvc;
    View viewvc1;
    Spinner spinnerModifierCat,spinnercompte;
    Spinner spinnerDeleteCat;
    ListView list_cat = null;
    ListView list_compte=null;
    CheckBox chk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil1);

        // Set a Toolbar to replace the ActionBar.
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // This will display an Up icon (<-), we will replace it with hamburger later
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Find our drawer view
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerToggle = setupDrawerToggle();

        // Setup toggle to display hamburger icon with nice animation
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerToggle.syncState();

        // Tie DrawerLayout events to the ActionBarToggle
        mDrawer.addDrawerListener(drawerToggle);
        // Find our drawer view
        nvDrawer = (NavigationView) findViewById(R.id.nvView);
        // Setup drawer view
        setupDrawerContent(nvDrawer);

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

        list_cat= findViewById(R.id.list_cat);
        list_compte=findViewById(R.id.list_compte);
        spinnercompte = (Spinner) findViewById(R.id.spinnercompte);
        loadSpinnerDataPara();
    }

    private ActionBarDrawerToggle setupDrawerToggle() {
        // NOTE: Make sure you pass in a valid toolbar reference.  ActionBarDrawToggle() does not require it
        // and will not render the hamburger icon without it.
        return new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open,  R.string.drawer_close);
    }

    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        selectDrawerItem(menuItem);
                        return true;
                    }
                });
    }

    // `onPostCreate` called when activity start-up is complete after `onStart()`
    // NOTE 1: Make sure to override the method with only a single `Bundle` argument
    // Note 2: Make sure you implement the correct `onPostCreate(Bundle savedInstanceState)` method.
    // There are 2 signatures and only `onPostCreate(Bundle state)` shows the hamburger icon.
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggles
        drawerToggle.onConfigurationChanged(newConfig);
    }

    public void selectDrawerItem(MenuItem menuItem) {
        switch(menuItem.getItemId()) {
                case R.id.addCategorie:
                    Ajouter_categorie();
                break;
                case R.id.modifierCategorie:
                    Modifier_categorie();
                break;
                case R.id.supCat:
                    Supprimer_categorie();
                break;
                case R.id.addCompte:
                    Ajouter_compte();
                break;
                case R.id.modifierCompte:
                    Visualiser_compte();
                break;
            case R.id.apropos:
                    final AlertDialog.Builder apropos = new AlertDialog.Builder(this);
                    apropos.setTitle("A Propos");
                    apropos.setMessage("Cette Application a été developpé par 6 Developpeurs de l’université INUKA.");

                    apropos.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    apropos.show();
                break;

            case R.id.logout:
                startActivity(new Intent(accueil.this,MainActivity.class));
                finish();
                break;
            default:
                //fragmentClass = FirstFragment.class;
        }

        // Highlight the selected item has been done by NavigationView
        menuItem.setChecked(true);
        // Set action bar title
        //setTitle(menuItem.getTitle());
        // Close the navigation drawer
        mDrawer.closeDrawers();
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
        getMenuInflater().inflate(R.menu.menu_recherche, menu);

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

    private void loadSpinnerDataPara() {
        List<String> labels = accesLocal.getAllSpinners();

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        //ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, labels);

        viewModifierCat=getLayoutInflater().inflate(R.layout.activity_spinner_cat,null);

        spinnerModifierCat=viewModifierCat.findViewById(R.id.spinner);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinnerModifierCat.setAdapter(dataAdapter);
    }

    public void Ajouter_categorie(){
        final AlertDialog.Builder ajoutCategorie = new AlertDialog.Builder(accueil.this);
        ajoutCategorie.setTitle("Ajouter une Categorie");
        final EditText input=new EditText(accueil.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Saisir le nom...");
        ajoutCategorie.setView(input);
        ajoutCategorie.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textCategorie = String.valueOf(input.getText());
                if (textCategorie.equals("") ){
                    Toast.makeText(accueil.this, "Le champs est obligatoire!!!", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean insert = accesLocal.EnregistreCategorie(textCategorie, String.valueOf(user.userName()));
                    if (insert == true){
                        Toast.makeText(accueil.this, "enregistrement Categorie avec succes!!!", Toast.LENGTH_SHORT).show();
                        loadSpinnerData();
                    }else{
                        Toast.makeText(accueil.this, "enregistrement echouee!!!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        ajoutCategorie.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        ajoutCategorie.show();
    }

    private void Modifier_categorie() {
        final AlertDialog.Builder modifCategorie = new AlertDialog.Builder(accueil.this);
        modifCategorie.setTitle("Modifier une  Categorie");
        loadSpinnerData();
        loadSpinnerDataPara();

        final EditText edit=viewModifierCat.findViewById(R.id.edit);

        modifCategorie.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = String.valueOf(edit.getText());
                Boolean modification = accesLocal.updateEnregistreCategorie(spinnerModifierCat.getSelectedItem().toString(), String.valueOf(edit.getText()), String.valueOf(user.userName()));
                if (modification == true){
                    Toast.makeText(accueil.this, "Modification Categorie avec succes!!! "+a, Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(accueil.this, "echouee!!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        modifCategorie.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


        spinnerModifierCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!(spinnerModifierCat.getSelectedItem().toString().equalsIgnoreCase("Liste des categories"))) {
                    edit.setText(spinnerModifierCat.getSelectedItem().toString());
                }
                else {
                    Toast.makeText(accueil.this, "Selectionnez une categorie !", Toast.LENGTH_SHORT).show();
                    edit.setText("");
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        modifCategorie.setView(viewModifierCat);
        AlertDialog dialog= modifCategorie.create();
        dialog.show();
    }

    private void Supprimer_categorie() {
        loadSpinnerDataForDelete();
        final AlertDialog.Builder msupressCategorie = new AlertDialog.Builder(accueil.this);
        msupressCategorie.setTitle("Supprimer une  Categorie");

        msupressCategorie.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        msupressCategorie.setView(viewDeleteCat);
        final AlertDialog dialog=msupressCategorie.create();
        dialog.show();
        spinnerDeleteCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!(spinnerDeleteCat.getSelectedItem().toString().equalsIgnoreCase("Liste des categories"))) {
                    final AlertDialog.Builder msupression = new AlertDialog.Builder(accueil.this);
                    msupression.setTitle("Voulez-vous supprimer la categorie "+spinnerDeleteCat.getSelectedItem().toString()+" ?");

                    msupression.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            Boolean delete = accesLocal.deleteEnregistreCategorie(spinnerDeleteCat.getSelectedItem().toString(),String.valueOf(user.userName()));
                            if (delete == true){
                                Toast.makeText(accueil.this, "Suppression Categorie avec succes!!!", Toast.LENGTH_SHORT).show();
                                loadSpinnerDataForDelete();
                            }else{
                                Toast.makeText(accueil.this, "Operation echouee!!!", Toast.LENGTH_SHORT).show();
                            }
                            dialog.cancel();
                        }
                    });
                    msupression.setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });
                    msupression.show();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void Ajouter_compte() {
        final View view=getLayoutInflater().inflate(R.layout.creer_compte,null);
        final AlertDialog.Builder ajoutcat = new AlertDialog.Builder(accueil.this);
        ajoutcat.setTitle("Ajouter un Compte");
        ajoutcat.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editbank =(EditText) view.findViewById(R.id.editbank);
                EditText editnocompte = (EditText) view.findViewById(R.id.editnocompte);
                Spinner spinnertypedecompte = (Spinner)view.findViewById(R.id.spinnertypedecompte);

                if(editbank.getText().toString().equals("") || editnocompte.getText().toString().equals("")){
                    Toast.makeText(accueil.this,"Vide remplir les champs",Toast.LENGTH_SHORT).show();
                }
                else{
                    String nom=String.valueOf(user.userName());
                    String bank = editbank.getText().toString();
                    String nocompte=editnocompte.getText().toString();
                    String typedecompte=String.valueOf(spinnertypedecompte.getSelectedItem());
                    String etat="1";

                    Boolean insert = accesLocal.EnregistreCompte(nom,bank,nocompte,typedecompte,etat);
                    if (insert == true){
                        Toast.makeText(accueil.this, "enregistrement Compte  succes!!!", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(accueil.this, "enregistrement echoue!!!", Toast.LENGTH_SHORT).show();
                    }

                }

            }
        });
        ajoutcat.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        ajoutcat.setView(view);
        AlertDialog dialog= ajoutcat.create();
        dialog.show();
    }

    public void visual(String nombank){
        EditText editkont = viewvc1.findViewById(R.id.editbankV);
        EditText editnocompteV = viewvc1.findViewById(R.id.editnocompteV);
        Spinner spinnertypedecompte = viewvc1.findViewById(R.id.spinnertypedecompte);
        CheckBox checketat=viewvc1.findViewById(R.id.checketat);

        editkont.setText(nombank);
        editnocompteV.setText(accesLocal.getnocompte(nombank,String.valueOf(user.userName())));
        String spinne=accesLocal.gettypecompte(nombank,String.valueOf(user.userName()));
        if(spinne.equalsIgnoreCase("Courant"))
            spinnertypedecompte.setSelection(0);
        else
            spinnertypedecompte.setSelection(1);

        String etat =accesLocal.getetat(nombank,String.valueOf(user.userName()));
        if(etat.equals("1")) {
            checketat.setChecked(true);
            checketat.setText("Compte Actif");
        }
        else {
            checketat.setChecked(false);
            checketat.setText("Compte inActif");
        }
    }

    private void Visualiser_compte() {
        loadSpinnerDataForCompte();
        final AlertDialog.Builder dialogcompte = new AlertDialog.Builder(accueil.this);
        dialogcompte.setTitle("Modifier/visualiser un compte");

        dialogcompte.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        dialogcompte.setView(viewvc);
        final AlertDialog dialog=dialogcompte.create();
        dialog.show();

        spinnercompte.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if(!(spinnercompte.getSelectedItem().toString().equalsIgnoreCase("Liste des Comptes"))) {
                    final AlertDialog.Builder visual_cpte = new AlertDialog.Builder(accueil.this);
                    visual_cpte.setTitle("Visualiser / Modifier");
                    visual_cpte.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        // Retourner les infos Concernant le compte selectionner

                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            updateEtat();
                        }
                    });
                    visual_cpte.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.cancel();
                        }
                    });

                    viewvc1=getLayoutInflater().inflate(R.layout.visualiser_compte,null);

                    visual_cpte.setView(viewvc1);
                    AlertDialog dialog= visual_cpte.create();
                    dialog.show();
                    // rampli bank lan
                    visual(""+spinnercompte.getSelectedItem().toString());

                    chk=viewvc1.findViewById(R.id.checketat);
                    chk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                            if(chk.isChecked()){
                                chk.setText("compte actif");
                            }else chk.setText("compte Inactif");
                        }
                    });

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void updateEtat() {
        String chktext = chk.getText().toString();
        String chkavant = accesLocal.getetat(spinnercompte.getSelectedItem().toString(),String.valueOf(user.userName()));

        if(chkavant.equals("1") && chktext.equalsIgnoreCase("Compte inActif")) {
            // do something
            boolean trouve=accesLocal.UpdateEtat("0",String.valueOf(user.userName()),spinnercompte.getSelectedItem().toString());
            if(trouve == true){
                Toast.makeText(accueil.this,"Etat du compte est Inactif",Toast.LENGTH_SHORT).show();
            }
        }
        if(chkavant.equals("0") && chktext.equalsIgnoreCase("Compte Actif")) {
            Toast.makeText(accueil.this,"Etat du compte change",Toast.LENGTH_SHORT).show();
            // do something
            boolean trouve=accesLocal.UpdateEtat("1",String.valueOf(user.userName()),spinnercompte.getSelectedItem().toString());
            if(trouve == true){
                Toast.makeText(accueil.this,"Etat du compte est Actif",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadSpinnerDataForDelete() {
        List<String> labels=accesLocal.getAllSpinners();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        //ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, labels);
        viewDeleteCat=getLayoutInflater().inflate(R.layout.activity_spinner_cat_supress,null);
        spinnerDeleteCat=viewDeleteCat.findViewById(R.id.spinner);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnerDeleteCat.setAdapter(dataAdapter);
    }

    private void loadSpinnerDataForCompte() {
        List<String> labels=accesLocal.getAllSpinnerscompte();
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, labels);
        //ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, labels);
        viewvc=getLayoutInflater().inflate(R.layout.spinnervisualcompte,null);
        spinnercompte=viewvc.findViewById(R.id.spinnercompte);
        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // attaching data adapter to spinner
        spinnercompte.setAdapter(dataAdapter);
    }

    @Override
    protected void onResume() {
        viewDeleteCat=getLayoutInflater().inflate(R.layout.activity_spinner_cat_supress,null);
        super.onResume();
    }
}
