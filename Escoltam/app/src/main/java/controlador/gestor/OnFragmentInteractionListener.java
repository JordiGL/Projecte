package controlador.gestor;

import android.widget.EditText;
import android.widget.LinearLayout;

public interface OnFragmentInteractionListener {
    void onButtonPressed(EditText previousPasswordEntered, EditText newPassword, EditText conformPassword);
    void onButtonPressed(EditText passwordEntered, String choice, LinearLayout radioGroupLayout);
}
