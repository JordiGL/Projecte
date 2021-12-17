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
import android.widget.Toast;

import java.util.List;

import controlador.gestor.GestorText;
import controlador.gestor.GestorUser;
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
    private static final String CHOICE = "choice";
    private OnFragmentInteractionPanellListener mListener;
    private static List<Icona> mIcones;
    private RecyclerView mRecyclerView;
    private PanellRecyclerAdapter mAdapter;
    private Panell panell;
    private View rootView;
    private int idIcona;

    public PanellFragment(Panell panell) {
        this.panell = panell;
    }

    public PanellFragment() {
    }

    public static PanellFragment newInstance(Panell panell) {
        PanellFragment fragment = new PanellFragment();

        Bundle arguments = new Bundle();
        arguments.putSerializable(CHOICE, panell);
        fragment.setArguments(arguments);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_panell, container, false);

        panell = (Panell) getArguments().getSerializable(CHOICE);

        mIcones = panell.getIcones();

        //RecyclerView i adapter.
        mRecyclerView = rootView.findViewById(R.id.recyclerViewPanell);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), GestorUser.getFileIcons()));
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

    @Override
    public void onIconClicked(String iconText) {

        GestorText.getList().add(iconText + " ");
        GestorText.refreshEditText();
    }

    @Override
    public void onIconLongClicked(View v, String id) {
        idIcona = Integer.parseInt(id);
        openMoreMenuOptions(v);
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
}