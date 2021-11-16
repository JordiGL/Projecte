package controlador.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe del fragment que conté el control de la pantalla de l'usuari.
 * @author Jordi Gómez Lozano.
 */
public class UserControlFragment extends Fragment {

    public UserControlFragment() {
    }

    public static UserControlFragment newInstance(){
        return new UserControlFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_control, container, false);
    }
}