package jordigomez.ioc.cat.comunicador;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

public class AdministratorActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrador);

        //Amagar barra superior del layout.
        getSupportActionBar().hide();
        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView correuAdministrador = findViewById(R.id.textCorreuAdministrador);
        Button btnLogout = findViewById(R.id.btn_tancarAdministrador);

        Intent intent = getIntent();
        correuAdministrador.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));


//        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//
//        if(user != null){
//            correuAdministrador.setText(user.getEmail());
//        }

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(AdministratorActivity.this, LoginActivity.class);
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