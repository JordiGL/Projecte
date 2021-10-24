package dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Locale;

import controlador.gestor.GestorException;
import model.Usuari;

/**
 * Classe que gestiona allò de la base de dades que fa referència a l'usuari.
 * @author Jordi Gómez Lozano.
 */
public class DAOUsuariImpl extends Database implements DAOUsuari {

    private Context context;

    public DAOUsuariImpl(@Nullable Context context) {
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
        long insert;
        int idUsuari;

        try {
            Database database = new Database(context);
            SQLiteDatabase db = database.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put("username", usuari.getEmail().toLowerCase());
            values.put("voice", usuari.getVoice());
            values.put("password", usuari.getPassword());
            values.put("enabled", usuari.isEnabled());

            db.insert(TABLE_USUARIS, null, values);

            idUsuari = obtenirId(usuari.getEmail());

            ContentValues rolesValues = new ContentValues();
            rolesValues.put("usuari_id", idUsuari);
            rolesValues.put("role_id", 1);

            insert = db.insert(TABLE_USUARIS_ROLES, null, rolesValues);
            Log.i("Error", String.valueOf(insert));
        } catch (Exception ex) {
            throw new GestorException("Error en insertar un usuari a la base de dades: " +
                    ex.getMessage());
        }

        return insert > 0;
    }

    /**
     * Comprova que un usuari estigui a la base de dades.
     * @param email de l'usuari a cercar.
     * @return true si ha trobat l'usuari a la base de dades, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public boolean comprovar(String email) {
        Cursor cursor = null;

        try {
            Database database = new Database(context);
            SQLiteDatabase db = database.getWritableDatabase();

            cursor = db.rawQuery("SELECT username FROM " + TABLE_USUARIS + " WHERE username= '" + email + "' LIMIT 1", null);

            return cursor.moveToFirst();

        } catch (Exception ex) {
            ex.getMessage();

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return false;
    }

    /**
     * Obté un usuari de la base de dades.
     * @param email de l'usuari a obtenir.
     * @return Usuari si ha trobat l'usuari a la base de dades, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    public Usuari obtenir(String email) {
        Cursor cursor = null;
        Usuari usuari = null;

        try {
            Database database = new Database(context);
            SQLiteDatabase db = database.getWritableDatabase();

            cursor = db.rawQuery("SELECT u.*, ur.role_id FROM " + TABLE_USUARIS +" u "+"" +
                    "INNER JOIN " +TABLE_USUARIS_ROLES+" ur ON u.id = ur.role_id " +
                    "WHERE username= '" + email + "' LIMIT 1", null);

            if (cursor.moveToFirst()) {

                usuari = new Usuari();
                usuari.setEmail(cursor.getString(1));
                usuari.setVoice(cursor.getString(2));
                usuari.setEnabled(cursor.getInt(3) > 0);
                usuari.setPassword(cursor.getString(4));
                usuari.setAdministrator(cursor.getInt(5));
            }

        } catch (Exception ex) {
            ex.getMessage();

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return usuari;
    }

    /**
     * Cerca un usuari a la base de dades.
     * @param email de l'usuari a cercar.
     * @return true si ha trobat l'usuari a la base de dades, false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private int obtenirId(String email) {
        Cursor cursor = null;
        int idUsuari = 0;

        try {
            Database database = new Database(context);
            SQLiteDatabase db = database.getWritableDatabase();

            cursor = db.rawQuery("SELECT id FROM " + TABLE_USUARIS + " WHERE username= '" + email + "' LIMIT 1", null);

            if (cursor.moveToFirst()) {

                idUsuari = cursor.getInt(0);
                return idUsuari;

            }

        } catch (Exception ex) {
            ex.getMessage();

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }

        return idUsuari;

    }

    /**
     * Actualitza l'atribut d'activitat de l'usuari
     * @param email de l'usuari.
     * @param enabled de l'usuari.
     * @author Jordi Gómez Lozano.
     */
    public void updateEnable(String email, boolean enabled) {
        Cursor cursor = null;

        try {
            Database database = new Database(context);
            SQLiteDatabase db = database.getWritableDatabase();

            ContentValues enableUpdated = new ContentValues();
            enableUpdated.put("enabled", enabled);

            db.update(TABLE_USUARIS, enableUpdated, "username=?", new String[]{email});

        } catch (Exception ex) {
            ex.getMessage();

        } finally {

            if (cursor != null) {
                cursor.close();
            }
        }
    }
}
