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
    private DrawerLayout mDrawer;
    private Toolbar toolbar;
    private NavigationView nvDrawer;
    private ActionBarDrawerToggle drawerToggle;

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
        // Create a new fragment and specify the fragment to show based on nav item clicked
        Fragment fragment = null;
        Class fragmentClass = null;
        switch(menuItem.getItemId()) {
            case R.id.parametre:
                startActivity(new Intent(this,Parametre.class));
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
        setTitle(menuItem.getTitle());
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        if (id == R.id.home) {
            mDrawer.openDrawer(GravityCompat.START);
            return true;
        }

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
            startActivity(new Intent(this,Parametre.class));
            return true;
        }

        if (id == R.id.logout) {
            startActivity(new Intent(accueil.this,MainActivity.class));
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
