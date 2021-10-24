package testservidor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import controlador.LoginActivity;
import controlador.gestor.GestorLogin;
import controlador.gestor.GestorRequest;
import controlador.gestor.GestorSharedPreferences;
import io.github.muddz.styleabletoast.StyleableToast;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe per a fer proves amb el servidor.
 * @author Jordi Gómez Lozano
 */
public class ServerTestsActivity extends AppCompatActivity {
    public static final String ERROR_SERVIDOR = "Problemes amb el servidor";
    public static final int BAD_REQUEST = 400;
    public static final String BAD_CREDENTIALS = "Bad credentials";
    public static final int OK = 200;
    public static final String CORRECTE = "Ok";
    private static final int TIMEOUT_MILLS = 3000;
    TextView textResponse;
    EditText email;
    EditText clau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_tests);
        email = findViewById(R.id.emailTest);
        clau = findViewById(R.id.clauTest);
        textResponse = findViewById(R.id.textResponse);
        Button button = findViewById(R.id.buttonTest);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textResponse.setText("");
                long time = System.currentTimeMillis();

                Thread thread = new Thread(new Runnable() {

                    @Override
                    public void run() {
                        GestorRequest gestorRequest = new GestorRequest(ServerTestsActivity.this);


                        //Faig la petició i obtinc el token i el codi de resposta
                        int responseCode = gestorRequest.requestToken(email.getText().toString(), clau.getText().toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                if((System.currentTimeMillis() - time) >= TIMEOUT_MILLS){

                                    textResponse.setText(ERROR_SERVIDOR);
                                } else {

                                    if(responseCode == OK){

                                        textResponse.setText(CORRECTE);

                                    }else if(responseCode == BAD_REQUEST){

                                        textResponse.setText(BAD_CREDENTIALS);
                                    }
                                }
                            }
                        });
                    }
                });
                thread.start();
























//                GestorTestServidor gestorTestServidor = new GestorTestServidor();
//
//                gestorTestServidor.TestServidor(email.getText().toString(), clau.getText().toString(),
//                        new ServerResponseCallBack() {
//                    @Override
//                    public void onCallBack(String response) {
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                if(response.equals(BAD_REQUEST)){
//                                    textResponse.setText(BAD_CREDENTIALS);
//                                }else if(response.equals(OK)){
//                                    textResponse.setText(CORRECTE);
//                                }else{
//                                    textResponse.setText(ERROR_SERVIDOR);
//                                }
//
//                            }
//                        });
//
//                    }
//                });
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ServerTestsActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}