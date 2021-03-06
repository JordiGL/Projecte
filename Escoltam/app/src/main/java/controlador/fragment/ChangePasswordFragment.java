package controlador.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import controlador.gestor.OnFragmentInteractionSettingsListener;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe del fragment que conté el canvi de clau de la pantalla de configuració.
 * @see Fragment
 * @author Jordi Gómez Lozano.
 */
public class ChangePasswordFragment extends Fragment {

    public static final String ERROR = "Error";
    OnFragmentInteractionSettingsListener mListener;


    public ChangePasswordFragment() {
    }

    public static ChangePasswordFragment newInstance(){
        return new ChangePasswordFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =  inflater.inflate(R.layout.fragment_change_password, container, false);
        final EditText previousPasswordEntered = rootView.findViewById(R.id.inputPreviousPasswordChangePassword);
        final EditText newPassword = rootView.findViewById(R.id.inputPasswordChangePassword);
        final EditText conformNewPassword = rootView.findViewById(R.id.inputConformPasswordChangePassword);
        final Button btnChangePassword = rootView.findViewById(R.id.btnChangePassword);

        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonPressed(
                        previousPasswordEntered,
                        newPassword,
                        conformNewPassword);

                previousPasswordEntered.getText().clear();
                newPassword.getText().clear();
                conformNewPassword.getText().clear();
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionSettingsListener) {
            mListener = (OnFragmentInteractionSettingsListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + ERROR);
        }
    }
}