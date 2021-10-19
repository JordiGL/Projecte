package dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

/**
 * Base de dades.
 * @see SQLiteOpenHelper
 * @author Jordi Gómez Lozano
 */
public class Database extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 14;
    private static final String DATABASE_NOMBRE = "comunicador.db";
    public static final String TABLE_USUARIS = "usuaris";
    public static final String TABLE_ROLES = "roles";
    public static final String TABLE_USUARIS_ROLES = "usuaris_roles";


    public Database(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USUARIS+ "("+
                "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                "username TEXT UNIQUE," +
                "voice TEXT," +
                "enabled BOOLEAN DEFAULT 0," +
                "password TEXT)");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_ROLES+ "(" +
                "id SERIAL PRIMARY KEY," +
                "name TEXT UNIQUE)");

        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USUARIS_ROLES+ "(" +
                "usuari_id INT, " +
                "role_id INT, " +
                "FOREIGN KEY (usuari_id) REFERENCES usuaris (id)," +
                "FOREIGN KEY (role_id)REFERENCES roles (id))");

        sqLiteDatabase.execSQL("INSERT INTO roles (id, name) VALUES (1, \"ROLE_USER\")");
        sqLiteDatabase.execSQL("INSERT INTO roles (id, name) VALUES (2, \"ROLE_ADMIN\")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_USUARIS);
        onCreate(sqLiteDatabase);
    }
}
