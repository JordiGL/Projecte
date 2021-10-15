package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import gestor.DbHelper;
import gestor.DbUsuaris;
import io.github.muddz.styleabletoast.StyleableToast;
import model.Usuari;

/**
 * Classe que permet iniciar sessió.
 * @author Jordi Gómez Lozano.
 * @see AppCompatActivity
 */
public class LoginActivity extends AppCompatActivity {
    private EditText email, clau;
    private SQLiteDatabase db;
    private DbUsuaris dbUsuaris;
    private Usuari usuari;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        DbHelper dbHelper = new DbHelper(LoginActivity.this);
        db = dbHelper.getWritableDatabase();
        dbUsuaris = new DbUsuaris(LoginActivity.this);

        TextView btnAlta = findViewById(R.id.textAlta);
        Button btnIniciSessio = findViewById(R.id.btnLogin);
        email = findViewById(R.id.inputEmailLogin);
        clau = findViewById(R.id.inputPasswordLogin);

        btnAlta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnIniciSessio.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                if(!comprovarCamps()){

                    if(usuari.isEnabled()){

                        startActivity(new Intent(LoginActivity.this, AdministratorActivity.class));

                    }else{

                        startActivity(new Intent(LoginActivity.this, ClientActivity.class));
                    }
                }
            }
        });
    }


    /**
     * Obté el valor introduït per l'usuari en els diferents camps del registre i comprova que no hi hagi errors.
     * @return Un booleà: true si ha trobat error, i false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean comprovarCamps(){

        /*En el login no és necessari que un usuari sàpiga si un correu existeix a la base de dades,
        per tant no es mostrarà un error referent a això.*/
        boolean errorEmail = comprovarCampBuit(email, R.string.campEmailBuitLogin, R.drawable.ic_email);
        boolean errorClau = comprovarCampBuit(clau, R.string.campClauBuitLogin, R.drawable.ic_security);

        if(!errorEmail && !errorClau) {

                if (dbUsuaris.comprovarContacte(email.getText().toString())) {

                    usuari = dbUsuaris.obtenirContacte(email.getText().toString());

                    if (usuari.getPassword().equals(clau.getText().toString())) {

                        return false;
                    }
                }

                clau.setBackgroundResource(R.drawable.bg_edittext_error);
                email.setBackgroundResource(R.drawable.bg_edittext_error);

                StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.errorEmailOClau), Toast.LENGTH_SHORT, R.style.toastError).show();
                return true;
        }

        return true;
    }

    /**
     * Comprova si els TextViews estan buits.
     * @param textView a comprovar.
     * @param textError missatge a mostrar.
     * @param iconaEsquerra icona esquerra del camp.
     * @return Un booleà: true si el camp del TextView esta buit, i false en cas contrari.
     * @author Jordi Gómez Lozano.
     */
    private boolean comprovarCampBuit(TextView textView, int textError, int iconaEsquerra){

        boolean error = false;

        if (TextUtils.isEmpty(textView.getText().toString().trim())) {

            textView.setBackgroundResource(R.drawable.bg_edittext_error);
            textView.setError(getResources().getString(textError));

            error = true;

        }else{

            textView.setBackgroundResource(R.drawable.bg_edittext);
        }


        return error;
    }

    private void crearBaseDeDades(){

        if(db != null){
            Toast.makeText(LoginActivity.this, "Base de dades creada", Toast.LENGTH_LONG).show();
            DbUsuaris dbUsuaris = new DbUsuaris(LoginActivity.this);
            dbUsuaris.insertarUsuaris("gemmarica94@gmail.com", true,
                    "FEMALE", "Gemma", "gemma", "600000001");
            dbUsuaris.insertarUsuaris("jonatanchaler@gmail.com", true,
                    "MALE", "Jonatan", "jonatan", "600000002");
            dbUsuaris.insertarUsuaris("jogomloz@gmail.com", true,
                    "MALE", "Jordi", "jordi", "600000003");
            dbUsuaris.insertarUsuaris("mariaprova@gmail.com", false,
                    "FEMALE", "Maria", "maria", "600000004");
        }else{
            Toast.makeText(LoginActivity.this, "ERROR EN CREAR LA BASE DE DADES", Toast.LENGTH_LONG).show();
        }

    }
}