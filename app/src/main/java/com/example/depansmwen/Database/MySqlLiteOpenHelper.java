package com.example.depansmwen.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MySqlLiteOpenHelper extends SQLiteOpenHelper {

    private static final String TableUtilisateur4 = "create table TableUtilisateur4(nom TEXT, prenom TEXT, username TEXT, password TEXT)";
   // private String TableCategorie = "create table TableCategorie(categorie TEXT, prix TEXT, devise TEXT, note TEXT, date LONG, user TEXT)";
    private String TableCategorie4 = "create table TableCategorie4(categorie TEXT, prix TEXT, devise TEXT, note TEXT, date TEXT, user TEXT)";
    private String TableDepense = "create table TableDepense(categorie TEXT, prix TEXT, devise TEXT, note TEXT, date TEXT, user TEXT, typeCompte TEXT, ID INTEGER PRIMARY KEY AUTOINCREMENT)";
    private String TableEnregistreCategorie4 = "create table TableEnregistreCategorie4(categorie TEXT, user TEXT)";
    private String TableEnregistreCompte9 ="create table TableEnregistreCompte9(user TEXT,bank TEXT,noducompte TEXT,typedecompte TEXT,etat TEXT)";

    public MySqlLiteOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(TableCategorie4);
        db.execSQL(TableDepense);
        db.execSQL(TableEnregistreCategorie4);
        db.execSQL(TableUtilisateur4);
        db.execSQL(TableEnregistreCompte9);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+" TableCategorie4");
        db.execSQL("drop table if exists "+" TableDepense");
        db.execSQL("drop table if exists "+" TableEnregistreCategorie4");
        db.execSQL("drop table if exists "+" TableUtilisateur4");
        db.execSQL("drop table if exists "+" TableEnregistreCompte9");
        onCreate(db);
    }
}
