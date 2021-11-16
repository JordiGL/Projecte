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

import controlador.gestor.OnFragmentInteractionListener;
import jordigomez.ioc.cat.escoltam.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends Fragment {

    OnFragmentInteractionListener mListener;


    public ChangePasswordFragment() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
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
            }
        });

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + "Error");
        }
    }
}