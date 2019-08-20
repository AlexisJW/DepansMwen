package com.example.depansmwen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.depansmwen.Database.MySqlLiteOpenHelper;

public class AccesLocal {
    private String nomBase = "bdDepansMwen.sqlite";
    private Integer versionBase = 1;
    private MySqlLiteOpenHelper accesBd;
    private SQLiteDatabase bd;

    public AccesLocal(Context context) {
        accesBd = new MySqlLiteOpenHelper(context, nomBase, null, versionBase);
    }

//    public void Signup(){
//         bd = accesBd.getWritableDatabase();
////         String req = " insert into TableUtilisateur(nom, prenom, username, password) " +
////                 "values("+inscription.getTxtInputNom()+
////                 ", "+inscription.getTxtInputPrenom()+
////                 ", "+inscription.getTxtInputUsername()+
////                 ", "+inscription.getTxtInputPassword()+") ";
////         bd.execSQL(req);
//    }

    public boolean signup(String nom, String prenom, String username, String password){
        bd = accesBd.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nom", nom);
        contentValues.put("prenom", prenom);
        contentValues.put("username", username);
        contentValues.put("password", password);

        long ins = bd.insert("TableUtilisateur", null, contentValues);
        if(ins ==- 1) return false;
        else return true;
    }

    public  boolean checkUsername(String username){
        bd = accesBd.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select * from TableUtilisateur where username=?", new String[]{username});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    public  boolean login(String username, String password){
        bd = accesBd.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select * from TableUtilisateur where username=? and password=?", new String[]{username, password});
        if (cursor.getCount() > 0) return true;
        else return false;
    }
}