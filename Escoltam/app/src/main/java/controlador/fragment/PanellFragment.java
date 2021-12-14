package controlador.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import controlador.gestor.OnFragmentInteractionPanellListener;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;
import model.Panell;


/**
 * Fragment pels panells
 * @see Fragment
 * @author Jordi Gómez Lozano
 */
public class PanellFragment extends Fragment {
    private static final String ERROR = "Error";
    private static final String NAME_NEW_PANELL = "Nou panell";
    private OnFragmentInteractionPanellListener mListener;
    private static List<Icona> mIcones;
    private RecyclerView mRecyclerView;
    private PanellRecyclerAdapter mAdapter;
    private int spanCount = 3;
    private Panell panell;
//    private EditText panellTitle;
//    private ImageButton optionsButton;
    private View rootView;

    public PanellFragment(Panell panell) {
        this.panell = panell;
    }

//    public static PanellFragment newInstance(int panelPosition) {
//        PanellFragment fragment = new PanellFragment(panelPosition);
//        position = panelPosition;
//        return fragment;
//    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_panell, container, false);
//        panellTitle = rootView.findViewById(R.id.titolPanell);
//        optionsButton = rootView.findViewById(R.id.optionsPanell);
//        optionsButton.setTag(R.drawable.ic_action_settings);
//
//        registerForContextMenu(optionsButton);

//        try{

//            setEditTextFocusable(false);

            mIcones = panell.getIcones();

            //RecyclerView i adapter.
            mRecyclerView = rootView.findViewById(R.id.recyclerViewPanell);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
            mAdapter = new PanellRecyclerAdapter(mIcones, rootView.getContext());
            mRecyclerView.setAdapter(mAdapter);

//            panellTitle.setText(name);
//
//        }catch (IndexOutOfBoundsException e){
//            Log.i("Error", e.toString());
//        }
//
//        panellTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//
//                if (hasFocus) {
//
//                    optionsButton.setImageResource(R.drawable.ic_action_check);
//                    optionsButton.setTag(R.drawable.ic_action_check);
//                } else {
//
//                    optionsButton.setImageResource(R.drawable.ic_action_settings);
//                    optionsButton.setTag(R.drawable.ic_action_settings);
//                }
//            }
//        });
//
//        optionsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try{
//                    if(optionsButton.getTag() != null) {
//
//                        mListener.onPanellButtonPressed(
//                                optionsButton,
//                                panellTitle,
//                                panell.getId()
//                        );
//
//                        if((int) optionsButton.getTag() == R.drawable.ic_action_settings){
//                            openMoreMenuOptions(v);
//                        }
//                    }
//                }catch (IndexOutOfBoundsException e){
//                    Log.i("Error", e.toString());
//                }
//            }
//        });

        return rootView;
    }

//    /**
//     * PopupMenu per a mostrar les diferents opcions del botó.
//     * @param view del component.
//     * @author Jordi Gómez Lozano.
//     */
//    public void openMoreMenuOptions(View view) {
//
//        PopupMenu popup = new PopupMenu(rootView.getContext(), view);
//        popup.setOnMenuItemClickListener(this);
//        MenuInflater inflater = popup.getMenuInflater();
//
//        inflater.inflate(R.menu.menu_panell_context, popup.getMenu());
//
//
//        popup.show();
//    }
//
//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//
//        mListener.onPanellButtonPressed(
//                item,
//                panellTitle,
//                panell.getId()
//        );
//
//        return super.onContextItemSelected(item);
//
//    }

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

//    private void setEditTextFocusable(boolean focusable){
//        panellTitle.setFocusableInTouchMode(focusable);
//        panellTitle.setFocusable(focusable);
//    }
}