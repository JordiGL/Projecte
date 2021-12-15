package controlador.gestor;

import android.view.View;

/**
 * Interficie callback per a passar informació del recyclerview de les icones
 * @author Jordi Gómez Lozano
 */
public interface OnIconInteractionListener {
    void onIconClicked(String iconText);
    void onIconLongClicked(View v, String id);
}
