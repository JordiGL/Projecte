package controlador.gestor;

import android.widget.EditText;
import android.widget.LinearLayout;

/**
 * Interficie callback per a passar informacio d'un fragment a la activity
 * @author Jordi Gómez Lozano
 */
public interface OnFragmentInteractionListener {
    void onButtonPressed(EditText previousPasswordEntered, EditText newPassword, EditText conformPassword);
    void onButtonPressed(EditText passwordEntered, String choice, LinearLayout radioGroupLayout);
}
