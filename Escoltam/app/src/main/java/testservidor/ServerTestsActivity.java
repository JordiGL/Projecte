package testservidor;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import controlador.LoginActivity;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe per a fer proves amb el servidor.
 * @author Jordi Gómez Lozano
 */
public class ServerTestsActivity extends AppCompatActivity {
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
                GestorTestServidor gestorTestServidor = new GestorTestServidor();

                gestorTestServidor.TestServidor(email.getText().toString(), clau.getText().toString(),
                        new ServerResponseCallBack() {
                    @Override
                    public void onCallBack(String response) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                textResponse.setText(response);
                            }
                        });

                    }
                });
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