package gestor;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import java.util.ArrayList;

import model.Usuari;

public class DbUsuaris extends DbHelper{

    Context context;

    public DbUsuaris(@Nullable Context context){
        super(context);
        this.context = context;
    }

    public long insertarUsuaris(String email, boolean esAdministrador, String genere, String nom, String clau, String telefon){
        long insert = 0;

        try{
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("email", email);
            values.put("enabled", esAdministrador);
            values.put("gender", genere);
            values.put("nom", nom);
            values.put("password", clau);
            values.put("phone", telefon);

            insert = db.insert(TABLE_USUARIS, null, values);

        }catch(Exception ex){
            email.toString();
        }

        return insert;
    }

    public boolean comprovarContacte(String email){
        Cursor cursor = null;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT email FROM " + TABLE_USUARIS + " WHERE email= '" + email + "' LIMIT 1", null);

            if (cursor.moveToFirst()) {
                return true;

            } else {
                return false;
            }

        } catch (Exception e) {
            e.getMessage();

        }finally{

            if (cursor != null) {
                cursor.close();
            }
        }

        return false;
    }

    public Usuari obtenirContacte(String email){
        Cursor cursor = null;
        Usuari usuari = null;

        try {
            DbHelper dbHelper = new DbHelper(context);
            SQLiteDatabase db = dbHelper.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIS + " WHERE email= '" + email + "' LIMIT 1", null);

            if (cursor.moveToFirst()) {

                  usuari = new Usuari();
                  usuari.setEmail(cursor.getString(0));
                  usuari.setEnabled((cursor.getInt(1)>0));
                  usuari.setGender(cursor.getString(2));
                  usuari.setNom(cursor.getString(3));
                  usuari.setPassword(cursor.getString(4));
                  usuari.setPhone(cursor.getString(5));
            }

        } catch (Exception e) {
            e.toString();

        }finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return usuari;
    }

}
