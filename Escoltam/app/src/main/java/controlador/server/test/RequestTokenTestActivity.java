package controlador.server.test;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.net.HttpURLConnection;

import controlador.activity.LoginActivity;
import controlador.server.post.RequestToken;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe per a fer proves amb el servidor.
 * @author Jordi Gómez Lozano
 */
public class RequestTokenTestActivity extends AppCompatActivity {
    private static final String ERROR_SERVIDOR = "Problemes amb el servidor";
    private static final String BAD_CREDENTIALS = "Bad credentials";
    private static final String CORRECTE = "Ok";
    private TextView textResponse;
    private EditText email;
    private EditText clau;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request_token_test);
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
                        RequestToken gestorRequest = new RequestToken(RequestTokenTestActivity.this);


                        //Faig la petició i obtinc el codi de resposta
                        int responseCode = gestorRequest.requestToken(email.getText().toString(), clau.getText().toString());

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //Servidor NO iniciat.
                                if(gestorRequest.connectionProblems(time)){

                                    textResponse.setText(ERROR_SERVIDOR);
                                } else {
                                    //Resposta correcta
                                    if(responseCode == HttpURLConnection.HTTP_OK){

                                        textResponse.setText(CORRECTE);

                                    }else{
                                        //Resposta incorrecta
                                        textResponse.setText(BAD_CREDENTIALS);
                                    }
                                }
                            }
                        });
                    }
                });
                thread.start();
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(RequestTokenTestActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}