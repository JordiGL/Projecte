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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import controlador.gestor.OnFragmentInteractionListener;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Classe del fragment que conté el canvi de veu de la pantalla de configuració.
 * @see Fragment
 * @author Jordi Gómez Lozano.
 */
public class ChangeVoiceFragment extends Fragment {
    private static final String RADIO_BUTTON_COMPARED_TEXT = "Masculina";
    private static final String MALE = "MALE";
    private static final String FEMALE = "FEMALE";
    OnFragmentInteractionListener mListener;
    String voiceUsuari;

    public ChangeVoiceFragment() {
    }

    public static ChangeVoiceFragment newInstance(){
        return new ChangeVoiceFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View rootView =  inflater.inflate(R.layout.fragment_change_voice, container, false);
        final EditText passwordEntered = rootView.findViewById(R.id.inputPreviousPasswordChangeVoice);
        final RadioGroup radioGroupVeu = rootView.findViewById(R.id.rgChangeVoice);
        final LinearLayout radioGroupLayout = rootView.findViewById(R.id.linearLayoutChangeVoice);
        final Button btnChangeVoice = rootView.findViewById(R.id.btnChangeVoice);

        radioGroupVeu.setOnCheckedChangeListener(
                new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton radioButton = radioGroupVeu.findViewById(checkedId);
                        voiceUsuari = radioButton.getText().toString();

                        if(radioButton != null){

                            voiceUsuari = radioButton.getText().toString();
                            voiceUsuari = (voiceUsuari.equals(RADIO_BUTTON_COMPARED_TEXT) ? MALE : FEMALE);
                        }
                    }
                });


        btnChangeVoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onButtonPressed(
                        passwordEntered,
                        voiceUsuari,
                        radioGroupLayout);
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