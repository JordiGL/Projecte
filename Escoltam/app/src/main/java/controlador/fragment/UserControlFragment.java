package controlador.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import java.util.ArrayList;
import java.util.List;

import controlador.gestor.OnFragmentInteractionUserControlListener;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe del fragment que conté el control de la pantalla de l'usuari.
 * @see Fragment
 * @author Jordi Gómez Lozano.
 */
public class UserControlFragment extends Fragment {
    private static final String ERROR = "Error";
    private OnFragmentInteractionUserControlListener mListener;

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
        final View rootView =  inflater.inflate(R.layout.fragment_user_control, container, false);
        final ImageButton btnPlay = rootView.findViewById(R.id.button_play);
        final ImageButton btnPause = rootView.findViewById(R.id.button_pause);
        final ImageButton btnStop = rootView.findViewById(R.id.button_stop);

        List<ImageButton> buttons = new ArrayList<>();
        buttons.add(btnPlay);
        buttons.add(btnPause);
        buttons.add(btnStop);

        for(ImageButton imageButton: buttons){
            imageButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onControlButtonPressed((ImageButton) v);
                }
            });
        }
        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionUserControlListener) {
            mListener = (OnFragmentInteractionUserControlListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + ERROR);
        }
    }
}