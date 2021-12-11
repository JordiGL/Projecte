package controlador.gestor;

import android.widget.EditText;
import android.widget.ImageButton;

/**
 * Interface Listener dels botons del panell del comunicador
 * @author Jordi Gómez Lozano
 */
public interface OnFragmentInteractionPanellListener {
    void onPanellButtonPressed(ImageButton button, EditText titleEditText, int idPanell);
}
