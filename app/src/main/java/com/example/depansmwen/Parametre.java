package com.example.depansmwen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;


public class Parametre extends AppCompatActivity {
    AccesLocal accesLocal;
    static MainActivity user;
    ListView list_cat = null;
    ListView list_param = null;
    ListView list_compte=null;
    ListView param = null;
    View viewModifierCat;
    View viewDeleteCat;
    Spinner spinnerModifierCat;
    Spinner spinnerDeleteCat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_parametre);
        getSupportActionBar().setTitle("Parametre");
        accesLocal = new AccesLocal(Parametre.this);

        List<String> list = new ArrayList<>();
        List<String> list1 = new ArrayList<>();
        list.add("Rappel en fin de journee");
        list1.add("Devise");


        list_cat= findViewById(R.id.list_cat);
        list_param= findViewById(R.id.list_param1);
        param= findViewById(R.id.param);
        list_compte=findViewById(R.id.list_compte);

        // ArrayAdapter na = new ArrayAdapter(Parametre.this, R.layout.param_gene, list);
//        ArrayAdapter na = new ArrayAdapter(Parametre.this,android.R.layout.simple_list_item_checked,list);
//        ArrayAdapter na1 = new ArrayAdapter(Parametre.this,android.R.layout.simple_list_item_1,list1);
//        list_param.setAdapter(na1);
//        param.setAdapter(na);

        list_compte.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:{
                        Ajouter_compte();
                    }
                    break;
                    case 1:{
                        Visualiser_compte();
                    }
                    break;

                }
            }


        });

        list_cat.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:{
                        Ajouter_categorie();
                    }
                    break;
                    case 1:{
                        Modifier_categorie();
                    }
                    break;
                    case 2:{
                        Supprimer_categorie();
                    }
                    break;
                }
            }
        });
        loadSpinnerData();
        //loadSpinnerDataForDelete();
    }

    private void Ajouter_compte() {
        View view=getLayoutInflater().inflate(R.layout.creer_compte,null);
        Toast.makeText(this,"Ajouter Depense",Toast.LENGTH_SHORT).show();
        final AlertDialog.Builder depCategorie = new AlertDialog.Builder(Parametre.this);
        depCategorie.setTitle("Ajouter une Depense");
        depCategorie.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

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

    private void Visualiser_compte() {

    }


    private void loadSpinnerData() {
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

    private void loadSpinnerDataForDelete() {
        List<String> labels = accesLocal.getAllSpinners();

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

    private void Supprimer_categorie() {

        final AlertDialog.Builder msupressCategorie = new AlertDialog.Builder(Parametre.this);
        msupressCategorie.setTitle("Supprimer une  Categorie");
        loadSpinnerDataForDelete();
//        View view=getLayoutInflater().inflate(R.layout.activity_spinner_cat_supress,null);
//
//        final  Spinner spinner=view.findViewById(R.id.spinner);
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Parametre.this,android.R.layout.simple_spinner_item,
//                getResources().getStringArray(R.array.compte));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinner.setAdapter(adapter);

        msupressCategorie.setNegativeButton("Annuler", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        msupressCategorie.setView(viewDeleteCat);
        final AlertDialog dialog=msupressCategorie.create();
        dialog.show();

        spinnerDeleteCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!(spinnerDeleteCat.getSelectedItem().toString().equalsIgnoreCase("Select une Categorie"))) {

                    final AlertDialog.Builder msupression = new AlertDialog.Builder(Parametre.this);
                    msupression.setTitle("Voulez-vous supprimer la categorie "+spinnerDeleteCat.getSelectedItem().toString()+" ?");

                    msupression.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            Boolean delete = accesLocal.deleteEnregistreCategorie(spinnerDeleteCat.getSelectedItem().toString(), String.valueOf(user.userName()));
                            if (delete == true){
                                Toast.makeText(Parametre.this, "Suppression Categorie avec succes!!!", Toast.LENGTH_SHORT).show();
                                loadSpinnerDataForDelete();
                            }else{
                                Toast.makeText(Parametre.this, "echouee!!!", Toast.LENGTH_SHORT).show();
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

    private void Modifier_categorie() {

        final AlertDialog.Builder modifCategorie = new AlertDialog.Builder(Parametre.this);
        modifCategorie.setTitle("Modifier une  Categorie");
        loadSpinnerData();
//        View viewModifierCat=getLayoutInflater().inflate(R.layout.activity_spinner_cat,null);
//
//        final  Spinner spinnerModifierCat=viewModifierCat.findViewById(R.id.spinner);
//        ArrayAdapter<String> adapter=new ArrayAdapter<String>(Parametre.this,android.R.layout.simple_spinner_item,
//                getResources().getStringArray(R.array.compte));
//        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//        spinnerModifierCat.setAdapter(adapter);

        final EditText edit=viewModifierCat.findViewById(R.id.edit);

        modifCategorie.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String a = String.valueOf(edit.getText());
                Boolean modification = accesLocal.updateEnregistreCategorie(spinnerModifierCat.getSelectedItem().toString(), String.valueOf(edit.getText()), String.valueOf(user.userName()));
                if (modification == true){
                    Toast.makeText(Parametre.this, "Modification Categorie avec succes!!! "+a, Toast.LENGTH_SHORT).show();
                    //loadSpinnerDataForDelete();
                }else{
                    Toast.makeText(Parametre.this, "echouee!!!", Toast.LENGTH_SHORT).show();
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
                if(!(spinnerModifierCat.getSelectedItem().toString().equalsIgnoreCase("Select une Categorie"))) {
                    edit.setText(spinnerModifierCat.getSelectedItem().toString());
                }
                else {
                    Toast.makeText(Parametre.this, "Selectionnez une categorie !", Toast.LENGTH_SHORT).show();
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



    public void Ajouter_categorie(){

        final AlertDialog.Builder ajoutCategorie = new AlertDialog.Builder(Parametre.this);
        ajoutCategorie.setTitle("Ajouter une Categorie");
        final EditText input=new EditText(Parametre.this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setHint("Saisir le nom...");
        ajoutCategorie.setView(input);
        ajoutCategorie.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String textCategorie = String.valueOf(input.getText());
                if (textCategorie.equals("") ){
                    Toast.makeText(Parametre.this, "Le champs est obligatoire!!!", Toast.LENGTH_SHORT).show();
                }else{
                    Boolean insert = accesLocal.EnregistreCategorie(textCategorie, String.valueOf(user.userName()));
                    if (insert == true){
                        Toast.makeText(Parametre.this, "enregistrement Categorie avec succes!!!", Toast.LENGTH_SHORT).show();
                        loadSpinnerData();
                    }else{
                        Toast.makeText(Parametre.this, "enregistrement echouee!!!", Toast.LENGTH_SHORT).show();
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




}
