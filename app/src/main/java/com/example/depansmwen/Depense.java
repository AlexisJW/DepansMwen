package com.example.depansmwen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Depense extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_depense);
        getSupportActionBar().setTitle("Ajouter une depense");
    }
}
