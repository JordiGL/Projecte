package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class ClientActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView textEmailClient = findViewById(R.id.textEmailClient);
        Button btnLogout = findViewById(R.id.btn_tancarClient);

        Intent intent = getIntent();
        textEmailClient.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));

//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if(user != null){
//            textEmailClient.setText(user.getEmail());
//        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(ClientActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}