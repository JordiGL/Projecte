package controlador.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import jordigomez.ioc.cat.escoltam.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserControlFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserControlFragment extends Fragment {

    public UserControlFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_control, container, false);
    }
}