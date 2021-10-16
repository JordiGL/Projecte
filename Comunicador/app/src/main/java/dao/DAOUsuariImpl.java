package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

import gestor.GestorException;
import interfaces.DAOUsuari;
import model.Usuari;

/**
 * Classe que gestiona allò de la base de dades que fa referència a l'usuari.
 * @author Jordi Gómez Lozano.
 */
public class DAOUsuariImpl extends Database implements DAOUsuari {

    private Context context;

    public DAOUsuariImpl(@Nullable Context context){
        super(context);
        this.context = context;
    }

    /**
     * Afegeix un usuari a la base de dades.
     * @param usuari a afegir.
     * @return true si s'ha afegit l'usuari a la base de dades, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean insertar(Usuari usuari) throws GestorException {
        long insert = 0;

        try{
            Database database = new Database(context);
            SQLiteDatabase db = database.getWritableDatabase();

            ContentValues values = new ContentValues();

            values.put("email", usuari.getEmail());
            values.put("is_administrator", usuari.isAdministrator());
            values.put("voice", usuari.getVoice());
            values.put("name_surname", usuari.getNameSurname());
            values.put("password", usuari.getPassword());
            values.put("phone", usuari.getPhone());

            insert = db.insert(TABLE_USUARIS, null, values);

        }catch(Exception ex){
            throw new GestorException("Error en insertar un usuari a la base de dades: "+
            ex.getMessage());
        }

        return insert > 0;
    }

    /**
     * Cerca un usuari a la base de dades.
     * @param email de l'usuari a cercar.
     * @return true si ha trobat l'usuari a la base de dades, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean comprovar(String email){
        Cursor cursor = null;

        try {
            Database database = new Database(context);
            SQLiteDatabase db = database.getWritableDatabase();

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

    /**
     * Cerca un usuari a la base de dades.
     * @param email de l'usuari a cercar.
     * @return Usuari si ha trobat l'usuari a la base de dades, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public Usuari obtenir(String email){
        Cursor cursor = null;
        Usuari usuari = null;

        try {
            Database database = new Database(context);
            SQLiteDatabase db = database.getWritableDatabase();

            cursor = db.rawQuery("SELECT * FROM " + TABLE_USUARIS + " WHERE email= '" + email + "' LIMIT 1", null);

            if (cursor.moveToFirst()) {

                  usuari = new Usuari();
                  usuari.setEmail(cursor.getString(0));
                  usuari.setAdministrator((cursor.getInt(1)>0));
                  usuari.setVoice(cursor.getString(2));
                  usuari.setNameSurname(cursor.getString(3));
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
