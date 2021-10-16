package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import dao.Database;
import dao.DAOUsuariImpl;
import gestor.GestorException;
import gestor.GestorLogin;
import interfaces.DAOUsuari;
import io.github.muddz.styleabletoast.StyleableToast;
import model.Usuari;

/**
 * Classe que permet iniciar sessió.
 * @author Jordi Gómez Lozano.
 * @see AppCompatActivity
 */
public class LoginActivity extends AppCompatActivity {
    public final static String EXTRA_MESSAGE = "jordigomez.ioc.cat.comunicador.MESSAGE";
    private EditText email, password;
    private GestorLogin gestorLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView btnAlta = findViewById(R.id.textAlta);
        Button btnIniciSessio = findViewById(R.id.btnLogin);
        TextView btnClauPerduda = findViewById(R.id.clauPerduda);
        email = findViewById(R.id.inputEmailLogin);
        password = findViewById(R.id.inputPasswordLogin);



        btnAlta.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnIniciSessio.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                DAOUsuari dao;
                Intent intent;
                Usuari usuari;

                if(emailAndPasswordChecker()){
                    dao = new DAOUsuariImpl(LoginActivity.this);
                    usuari = dao.obtenir(email.getText().toString());

                    if(usuari.getPassword().equals(password.getText().toString())){

                        if(usuari.isAdministrator()){

                            intent = new Intent(LoginActivity.this, AdministratorActivity.class);
                        }else{

                            intent = new Intent(LoginActivity.this, ClientActivity.class);
                        }
                        intent.putExtra(EXTRA_MESSAGE, usuari.getEmail());
                        startActivity(intent);

                    } else {
                        email.setBackgroundResource(R.drawable.bg_edittext_error);
                        password.setBackgroundResource(R.drawable.bg_edittext_error);
                        email.setError("Usuari o clau incorrectes");
                        password.setError("Usuari o clau incorrectes");
                        StyleableToast.makeText(LoginActivity.this, getResources().getString(R.string.errorEmailOClau), Toast.LENGTH_SHORT, R.style.toastError).show();
                    }
                }
            }
        });

        btnClauPerduda.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private boolean emailAndPasswordChecker(){

        boolean correcte = true;

        gestorLogin = new GestorLogin(email.getText().toString(), password.getText().toString());


        if (!gestorLogin.emailChecker()) {

            email.setBackgroundResource(R.drawable.bg_edittext_error);
            email.setError(gestorLogin.getError());

            correcte = false;

        } else {

            email.setBackgroundResource(R.drawable.bg_edittext);
        }

        if (!gestorLogin.passwordChecker()) {

            password.setBackgroundResource(R.drawable.bg_edittext_error);
            password.setError(gestorLogin.getError());

            correcte = false;

        } else {

            email.setBackgroundResource(R.drawable.bg_edittext);
        }

        return correcte;
    }

    private void crearBaseDeDades(){
        try {
            Database db = new Database(LoginActivity.this);
            SQLiteDatabase database = db.getWritableDatabase();
            if (database != null) {
                Toast.makeText(LoginActivity.this, "Base de dades creada", Toast.LENGTH_LONG).show();
                DAOUsuariImpl DAOUsuariImpl = new DAOUsuariImpl(LoginActivity.this);
                DAOUsuariImpl.insertar(new Usuari("gemmarica94@gmail.com", true,
                        "FEMALE", "Gemma", "gemma", "600000001"));
                DAOUsuariImpl.insertar(new Usuari("jonatanchaler@gmail.com", true,
                        "MALE", "Jonatan", "jonatan", "600000002"));
                DAOUsuariImpl.insertar(new Usuari("jogomloz@gmail.com", true,
                        "MALE", "Jordi", "jordi", "600000003"));
                DAOUsuariImpl.insertar(new Usuari("mariaprova@gmail.com", false,
                        "FEMALE", "Maria", "maria", "600000004"));
            } else {
                Toast.makeText(LoginActivity.this, "ERROR EN CREAR LA BASE DE DADES", Toast.LENGTH_LONG).show();
            }
        }catch(GestorException ex){
            Log.w("Error", "Error en registrar l'usuari", ex);
        }
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}