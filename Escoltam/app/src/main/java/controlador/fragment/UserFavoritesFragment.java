package controlador.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe del fragment que conte els favorits.
 * @see Fragment
 * @author Jordi Gómez Lozano.
 */
public class UserFavoritesFragment extends Fragment {

    public UserFavoritesFragment() {
    }

    public static UserFavoritesFragment newInstance(){
        return new UserFavoritesFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_favorites, container, false);
    }
}