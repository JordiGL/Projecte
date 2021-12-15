package controlador.gestor;

import android.view.View;
import android.widget.TextView;

public interface OnIconInteractionListener {
    public void onIconClicked(String iconText);
    public void onIconLongClicked(View v, String id);
}
