package controlador.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import controlador.activity.AdminSettingsActivity;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe del fragment que conté la barra de la pantalla de l'administrador.
 * @author Jordi Gómez Lozano.
 */
public class AdminToolbarFragment extends Fragment {


    public AdminToolbarFragment() {
    }

    public static AdminToolbarFragment newInstance(){
        return new AdminToolbarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_admin_toolbar, container, false);
        final ImageView settings = rootView.findViewById(R.id.buttonMore);
        final Spinner spinner = rootView.findViewById(R.id.spinner_object);

        //Assignem l'array de l'adapter de l'spinner i l'apliquem.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.search_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(rootView.getContext(), AdminSettingsActivity.class);
                rootView.getContext().startActivity(intent);
            }
        });

        return rootView;
    }
}