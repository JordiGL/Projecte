package controlador.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import controlador.activity.AdminSettingsActivity;
import controlador.activity.UserSettingsActivity;
import jordigomez.ioc.cat.escoltam.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserToolbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserToolbarFragment extends Fragment {

    public UserToolbarFragment() {
        // Required empty public constructor
    }

    public static UserToolbarFragment newInstance(){
        return new UserToolbarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView =  inflater.inflate(R.layout.fragment_user_toolbar, container, false);
        final EditText editText = rootView.findViewById(R.id.appCompatEditText);
        final ImageButton settings = (ImageButton) rootView.findViewById(R.id.user_button_top_right);

        editText.setShowSoftInputOnFocus(false);
        editText.setInputType(InputType.TYPE_NULL);
        editText.setFocusable(false);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(rootView.getContext(), UserSettingsActivity.class);
                rootView.getContext().startActivity(intent);
            }
        });

        return rootView;
    }
}