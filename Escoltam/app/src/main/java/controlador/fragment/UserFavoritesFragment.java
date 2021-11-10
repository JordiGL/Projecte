package controlador.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jordigomez.ioc.cat.escoltam.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserFavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserFavoritesFragment extends Fragment {

    public UserFavoritesFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_favorites, container, false);
    }
}