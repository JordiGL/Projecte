package controlador.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import controlador.gestor.GestorText;
import controlador.gestor.OnFragmentInteractionPanellListener;
import controlador.gestor.OnIconInteractionListener;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;
import model.Panell;


/**
 * Fragment pels panells
 * @see Fragment
 * @author Jordi Gómez Lozano
 */
public class PanellFragment extends Fragment implements OnIconInteractionListener, PopupMenu.OnMenuItemClickListener{
    private static final String ERROR = "Error";
    private static final String NAME_NEW_PANELL = "Nou panell";
    private OnFragmentInteractionPanellListener mListener;
    private static List<Icona> mIcones;
    private RecyclerView mRecyclerView;
    private PanellRecyclerAdapter mAdapter;
    private int spanCount = 3;
    private Panell panell;
    private EditText editText;
    private View rootView;
    private int idIcona;

    public PanellFragment(Panell panell, EditText editText) {
        this.panell = panell;
        this.editText = editText;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_panell, container, false);

        mIcones = panell.getIcones();

        //RecyclerView i adapter.
        mRecyclerView = rootView.findViewById(R.id.recyclerViewPanell);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        mAdapter = new PanellRecyclerAdapter(mIcones, rootView.getContext(), this);
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }


    /**
     * PopupMenu per a mostrar les diferents opcions del botó.
     * @param view del component.
     * @author Jordi Gómez Lozano.
     */
    public void openMoreMenuOptions(View view) {

        PopupMenu popup = new PopupMenu(rootView.getContext(), view);
        popup.setOnMenuItemClickListener(this);
        MenuInflater inflater = popup.getMenuInflater();

        inflater.inflate(R.menu.menu_icon_context, popup.getMenu());
        popup.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        mListener.onIconMenuItemPressed(item, idIcona);
        return super.onContextItemSelected(item);
    }

    /**
     * Mostra informació per pantalla.
     * @param message missatge que es mostrara per pantalla.
     * @author Jordi Gómez Lozano.
     */
    public void displayToast(String message) {
        Toast.makeText(getContext(), message,
                Toast.LENGTH_LONG).show();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionPanellListener) {
            mListener = (OnFragmentInteractionPanellListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + ERROR);
        }
    }

    @Override
    public void onIconClicked(String iconText) {

        GestorText.getTextList().add(iconText + " ");
        GestorText.refreshEditText();
    }

    @Override
    public void onIconLongClicked(View v, String id) {
        idIcona = Integer.parseInt(id);
        openMoreMenuOptions(v);
    }
}