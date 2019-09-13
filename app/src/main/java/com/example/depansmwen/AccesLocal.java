package com.example.depansmwen;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.depansmwen.Database.MySqlLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AccesLocal {
    private String nomBase = "bdDepansMwen.sqlite";
    private Integer versionBase = 4;
    private MySqlLiteOpenHelper accesBd;
    private SQLiteDatabase bd;
    String currentDateandTime;
    SimpleDateFormat sdf;
    static MainActivity user;

    public AccesLocal(Context context) {
        accesBd = new MySqlLiteOpenHelper(context, nomBase, null, versionBase);
        sdf = new SimpleDateFormat("yyyy.MM.dd");
        currentDateandTime = sdf.format(new Date());
    }


    public boolean signup(String nom, String prenom, String username, String password){
        bd = accesBd.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("nom", nom);
        contentValues.put("prenom", prenom);
        contentValues.put("username", username);
        contentValues.put("password", password);

        long ins = bd.insert("TableUtilisateur4", null, contentValues);
        if(ins ==- 1) return false;
        else return true;
    }

    public  boolean checkUsername(String username){
        bd = accesBd.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select * from TableUtilisateur4 where username=?", new String[]{username});
        if (cursor.getCount() > 0) return false;
        else return true;
    }

    public  boolean login(String username, String password){
        bd = accesBd.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Select * from TableUtilisateur4 where username=? and password=?", new String[]{username, password});
        if (cursor.getCount() > 0) return true;
        else return false;
    }

    public boolean AddCategorie(String categorie, String prix, String devise, String note, String date, String user){
        bd = accesBd.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categorie", categorie);
        contentValues.put("prix", prix);
        contentValues.put("devise", devise);
        contentValues.put("note", note);
        contentValues.put("date", date);
        contentValues.put("user", user);

        long ins = bd.insert("TableCategorie4", null, contentValues);
        if(ins ==- 1) return false;
        else return true;
    }

    public boolean EnregistreCategorie(String categorie,  String user){
        bd = accesBd.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("categorie", categorie);
        contentValues.put("user", user);

        long ins = bd.insert("TableEnregistreCategorie4", null, contentValues);
        if(ins ==- 1) return false;
        else return true;
    }

    public boolean updateEnregistreCategorie(String oldCategorie,String categorie,  String user){
        bd = accesBd.getWritableDatabase();
//        Cursor cursor = bd.rawQuery("Update TableEnregistreCategorie4 set categorie=?, user=? where categorie=? and user=?", new String[]{categorie, user});
//        if (cursor.getCount() > 0) return true;
//        else return false;

        ContentValues values = new ContentValues();
        values.put("categorie", categorie);
        String CAT="categorie";
        String USER="user";
        long ins = bd.update("TableEnregistreCategorie4", values, ""+CAT+"= '"+ oldCategorie+"' AND "+USER+"='"+user+"'" , null);
        if(ins ==- 1) return false;
        else return true;
    }

    public boolean deleteEnregistreCategorie(String categorie,  String user){
        bd = accesBd.getWritableDatabase();
        Cursor cursor = bd.rawQuery("Delete from TableEnregistreCategorie4 where categorie=? and user=?", new String[]{categorie, user});
        if (cursor.getCount() < 0) return false;
        else return true;
    }

    public ArrayList<InformationToday> ListInformationFromBd(){
        bd = accesBd.getReadableDatabase();
        String sql = "select * from TableCategorie4 where date = '"+currentDateandTime+"' and user = '"+String.valueOf(user.userName())+"' ";
        ArrayList<InformationToday> list = new ArrayList<>();
        Cursor cursor = bd.rawQuery(sql, null);
        if(cursor.moveToLast()){
            do {
                String categorieBase = cursor.getString(0);
                String montantBase = cursor.getString(1);
                String deviseBase = cursor.getString(2);
                String noteBase = cursor.getString(3);
                list.add(new InformationToday(categorieBase, montantBase, deviseBase, noteBase));
            }while (cursor.moveToPrevious());
        }
        cursor.close();
        return list;
    }

    public ArrayList<InformationToday> ListInformationSemaineFromBd(){
        bd = accesBd.getReadableDatabase();
        String sql = "select * from TableCategorie4 where user = '"+String.valueOf(user.userName())+"' ";
        ArrayList<InformationToday> list = new ArrayList<>();
        Cursor cursor = bd.rawQuery(sql, null);
        if(cursor.moveToLast()){
            do {
                String categorieBase = cursor.getString(0);
                String montantBase = cursor.getString(1);
                String deviseBase = cursor.getString(2);
                String noteBase = cursor.getString(3);
                list.add(new InformationToday(categorieBase, montantBase, deviseBase, noteBase));
            }while (cursor.moveToPrevious());
        }
        cursor.close();
        return list;
    }

    public List<String> getAllSpinners(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM TableEnregistreCategorie4";

        bd = accesBd.getReadableDatabase();
        Cursor cursor = bd.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding first column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        bd.close();
        // returning lables
        return list;
    }

    public Integer AllDepenseToday() {
        Integer allDepense = 0;

        bd = accesBd.getReadableDatabase();
        String sql = "select * from TableCategorie4 where date = '"+currentDateandTime+"' and user = '"+String.valueOf(user.userName())+"' ";
        ArrayList<InformationToday> list = new ArrayList<>();
        Cursor cursor = bd.rawQuery(sql, null);
        if(cursor.moveToLast()){
            do {
                String montantBase = cursor.getString(1);
                allDepense += Integer.parseInt(montantBase);
            }while (cursor.moveToPrevious());
        }
        cursor.close();
        return allDepense;
    }
}