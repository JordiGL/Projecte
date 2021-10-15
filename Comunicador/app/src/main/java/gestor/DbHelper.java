package gestor;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NOMBRE = "comunicador.db";
    public static final String TABLE_USUARIS = "usuaris";


    public DbHelper(@Nullable Context context) {
        super(context, DATABASE_NOMBRE, null, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE IF NOT EXISTS " + TABLE_USUARIS+ "("+
                "email TEXT PRIMARY KEY," +
                "enabled BOOLEAN," +
                "gender TEXT," +
                "nom TEXT," +
                "password TEXT," +
                "phone TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE " + TABLE_USUARIS);
        onCreate(sqLiteDatabase);
    }
}
