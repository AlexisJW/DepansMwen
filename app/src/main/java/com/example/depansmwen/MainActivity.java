package com.example.depansmwen;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
 import android.view.View;
import android.widget.*;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    private TextInputLayout txtInputUsername;
    private TextInputLayout txtInputPassword;
    Button btn_connect;
    private static AccesLocal accesLocal;
    static String username;


    public static AccesLocal getAccesLocal() {
        return accesLocal;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txtInputUsername = (TextInputLayout) findViewById(R.id.txtInputUsername);
        txtInputPassword = (TextInputLayout) findViewById(R.id.txtInputPassword);

        btn_connect=findViewById(R.id.btn_connect);
        accesLocal = new AccesLocal(MainActivity.this);

       // getActionBar().hide();
        getSupportActionBar().hide();
       //getSupportActionBar(); //.setTitle("Anrejistre");

        TextView  tx_creer_un_compte =findViewById(R.id.tx_creer_un_compte);

        btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 username = txtInputUsername.getEditText().getText().toString();
                String password = txtInputPassword.getEditText().getText().toString();
                if (username.equals("") || password.equals("")){
                    Toast.makeText(MainActivity.this, "Tous les champs sont obligatoires!!!", Toast.LENGTH_SHORT).show();
                }else {
                       Boolean login = accesLocal.login(username, password);
                       if (login == true){
                           userName();
                           startActivity(new Intent(MainActivity.this,accueil.class));
                           finish();
                       }else {
                           Toast.makeText(MainActivity.this, "Pseudo ou mot de passe incorrect!!!", Toast.LENGTH_SHORT).show();
                       }
                }
            }
        });

        tx_creer_un_compte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                startActivity(new Intent(getApplicationContext(), inscription.class));
            }
        });
    }

    public static String userName() {
        return username;
    }
}
