package controlador.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.Loader;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import controlador.fragment.AdminToolbarFragment;
import controlador.gestor.GestorSharedPreferences;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Activitat de l'administrador.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano
 */
public class AdministratorActivity extends AppCompatActivity{

    AdminToolbarFragment adminToolbarFragment;
    FragmentTransaction fragmentTransaction;
    FragmentManager fragmentManager;
    EditText capsaDeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_administrator);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        TextView correuAdministrador = findViewById(R.id.textCorreuAdministrador);
        Button btnLogout = findViewById(R.id.btn_LogoutAdministrador);
        ImageButton btnSearch = findViewById(R.id.searchButton);
        capsaDeText = findViewById(R.id.editTextBuscador);

        Intent intent = getIntent();
        correuAdministrador.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));

        ImageView settings = findViewById(R.id.imageMore);
        Spinner spinner = findViewById(R.id.spinner_object);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(AdministratorActivity.this,
                R.array.search_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String spinnerSelection = spinner.getSelectedItem().toString();

            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(AdministratorActivity.this, AdminSettingsActivity.class);
                startActivity(intent);
            }
        });

        btnLogout.setOnClickListener(view -> {
            //Borrar token
            GestorSharedPreferences gestorSharedPreferences = new GestorSharedPreferences(this);
            gestorSharedPreferences.deleteData();

            Intent intent1 = new Intent(AdministratorActivity.this, LoginActivity.class);
            startActivity(intent1);
            finish();
        });
    }

//    @Override
//    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//        switch(position){
//            case 0:
//                capsaDeText.setText("Tot");
//                break;
//            case 1:
//                capsaDeText.setText("Email");
//                break;
//            default:
//                capsaDeText.setText(parent.getSelectedItem().toString());
//                break;
//        }
//    }
//
//    @Override
//    public void onNothingSelected(AdapterView<?> parent) {
//    }
}