package controlador.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

import controlador.activity.PanellRecyclerAdapter;
import controlador.gestor.GestorUser;
import controlador.gestor.OnFragmentInteractionPanellListener;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;


/**
 * Fragment pels panells
 * @see Fragment
 * @author Jordi Gómez Lozano
 */
public class PanellFragment extends Fragment{
    private static final String ERROR = "Error";
    private static final String NAME_NEW_PANELL = "Nou panell";
    private OnFragmentInteractionPanellListener mListener;
    private static List<Icona> mIcones;
    private RecyclerView mRecyclerView;
    private PanellRecyclerAdapter mAdapter;
    private int spanCount = 3;
    private int position;
    private EditText panellTitle;
    private ImageButton optionsButton;

    public PanellFragment(int position) {
        this.position = position;
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
        final View rootView = inflater.inflate(R.layout.fragment_panell, container, false);
        panellTitle = rootView.findViewById(R.id.titolPanell);
        optionsButton = rootView.findViewById(R.id.optionsPanell);
        optionsButton.setTag(R.drawable.ic_action_settings);

        try{
            panellTitle.setText(GestorUser.getPanells().get(position).getNom());

            setEditTextFocusable(false);

            mIcones = GestorUser.getPanells().get(position).getIcones();

            //RecyclerView i adapter.
            mRecyclerView = rootView.findViewById(R.id.recyclerViewPanell);
            mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
            mAdapter = new PanellRecyclerAdapter(mIcones, rootView.getContext());
            mRecyclerView.setAdapter(mAdapter);

        }catch (IndexOutOfBoundsException e){
            Log.i("Error", e.toString());
        }

        panellTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    optionsButton.setImageResource(R.drawable.ic_action_check);
                    optionsButton.setTag(R.drawable.ic_action_check);
                } else {
                    optionsButton.setImageResource(R.drawable.ic_action_settings);
                    optionsButton.setTag(R.drawable.ic_action_settings);
                }
            }
        });

        optionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(optionsButton.getTag() != null) {
                        Log.i("Error", "posicio: "+GestorUser.getPanells().get(position).getId());
                        if(position >= 0){
                            mListener.onPanellButtonPressed(
                                    optionsButton,
                                    panellTitle,
                                    GestorUser.getPanells().get(position).getId()
                            );
                        }
                    }
                }catch (IndexOutOfBoundsException e){
                    Log.i("Error", e.toString());
                }
            }
        });

        return rootView;
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

    private void setEditTextFocusable(boolean focusable){
        panellTitle.setFocusableInTouchMode(focusable);
        panellTitle.setFocusable(focusable);
    }
}