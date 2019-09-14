package com.example.depansmwen;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;
import java.util.List;


public class Parametre extends AppCompatActivity   {
    AccesLocal accesLocal;
    static MainActivity user;
    ListView list_cat = null;
    ListView list_param = null;
    ListView list_compte=null;
    CheckBox chk;
    ListView param = null;
    View viewModifierCat;
    View viewDeleteCat;
    View  viewvc;
    View viewvc1;
    Spinner spinnerModifierCat,spinnercompte;
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
//        list_param= findViewById(R.id.list_param1);
//        param= findViewById(R.id.param);
        list_compte=findViewById(R.id.list_compte);
        spinnercompte = (Spinner) findViewById(R.id.spinnercompte);

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

    @Override
    protected void onResume() {
        viewDeleteCat=getLayoutInflater().inflate(R.layout.activity_spinner_cat_supress,null);
        super.onResume();
    }

    private void Ajouter_compte() {
        final View view=getLayoutInflater().inflate(R.layout.creer_compte,null);
        final AlertDialog.Builder ajoutcat = new AlertDialog.Builder(Parametre.this);
        ajoutcat.setTitle("Ajouter un Compte");
        ajoutcat.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                EditText editbank =(EditText) view.findViewById(R.id.editbank);
                EditText editnocompte = (EditText) view.findViewById(R.id.editnocompte);
                Spinner spinnertypedecompte = (Spinner)view.findViewById(R.id.spinnertypedecompte);

                if(editbank.getText().toString().equals("") || editnocompte.getText().toString().equals("")){
                    Toast.makeText(Parametre.this,"Vide remplir les champs",Toast.LENGTH_SHORT).show();
                }
                else{
                    String nom=String.valueOf(user.userName());
                    String bank = editbank.getText().toString();
                    String nocompte=editnocompte.getText().toString();
                    String typedecompte=String.valueOf(spinnertypedecompte.getSelectedItem());
                    String etat="1";

                    Boolean insert = accesLocal.EnregistreCompte(nom,bank,nocompte,typedecompte,etat);
                    if (insert == true){
                        Toast.makeText(Parametre.this, "enregistrement Compte  succes!!!", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(Parametre.this, "enregistrement echoue!!!", Toast.LENGTH_SHORT).show();
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
//      Toast.makeText(Parametre.this," "+etat,Toast.LENGTH_SHORT).show();
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

        final AlertDialog.Builder dialogcompte = new AlertDialog.Builder(Parametre.this);


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
                 final AlertDialog.Builder visual_cpte = new AlertDialog.Builder(Parametre.this);
                 visual_cpte.setTitle("Visualiser / Modifier");
//                    Toast.makeText(Parametre.this," sa se "+nombank,Toast.LENGTH_SHORT).show();
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

        if(chkavant.equals("1") && chktext.equalsIgnoreCase("Compte inActif"))
        {

                          // do something
            boolean trouve=accesLocal.UpdateEtat("0",String.valueOf(user.userName()),spinnercompte.getSelectedItem().toString());
            if(trouve == true){
                Toast.makeText(Parametre.this,"Etat du compte est Inactif",Toast.LENGTH_SHORT).show();

            }
        }
        if(chkavant.equals("0") && chktext.equalsIgnoreCase("Compte Actif"))
        {
            Toast.makeText(Parametre.this,"Etat du compte change",Toast.LENGTH_SHORT).show();

            // do something
            boolean trouve=accesLocal.UpdateEtat("1",String.valueOf(user.userName()),spinnercompte.getSelectedItem().toString());
            if(trouve == true){
                Toast.makeText(Parametre.this,"Etat du compte est Actif",Toast.LENGTH_SHORT).show();

            }
        }
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

    private void Supprimer_categorie() {
        loadSpinnerDataForDelete();
        final AlertDialog.Builder msupressCategorie = new AlertDialog.Builder(Parametre.this);
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

        spinnerDeleteCat.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(!(spinnerDeleteCat.getSelectedItem().toString().equalsIgnoreCase("Liste des categories"))) {

                    final AlertDialog.Builder msupression = new AlertDialog.Builder(Parametre.this);

                    msupression.setTitle("Voulez-vous supprimer la categorie "+spinnerDeleteCat.getSelectedItem().toString()+" ?");

                    msupression.setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog1, int which) {
                            Boolean delete = accesLocal.deleteEnregistreCategorie(spinnerDeleteCat.getSelectedItem().toString(),String.valueOf(user.userName()));
                            if (delete == true){
                                Toast.makeText(Parametre.this, "Suppression Categorie avec succes!!!", Toast.LENGTH_SHORT).show();
                                loadSpinnerDataForDelete();
                            }else{
                                Toast.makeText(Parametre.this, "Operation echouee!!!", Toast.LENGTH_SHORT).show();
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
                if(!(spinnerModifierCat.getSelectedItem().toString().equalsIgnoreCase("Liste des categories"))) {
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
