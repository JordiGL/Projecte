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
    private OnFragmentInteractionPanellListener mListener;
    private static List<Icona> mIcones;
    private RecyclerView mRecyclerView;
    private PanellRecyclerAdapter mAdapter;
    private int spanCount = 3;
    private static int position;
    private EditText panelTitle;
    private ImageButton optionsButton;

    public PanellFragment() {

    }

    public static PanellFragment newInstance(int panelPosition) {
        PanellFragment fragment = new PanellFragment();
        position = panelPosition;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_panell, container, false);
        panelTitle = rootView.findViewById(R.id.titolPanell);
        optionsButton = rootView.findViewById(R.id.optionsPanell);
        optionsButton.setTag(R.drawable.ic_action_settings);


        panelTitle.setText(GestorUser.getPanells().get(position).getNom());

        mIcones = GestorUser.getPanells().get(position).getIcones();

        //RecyclerView i adapter.
        mRecyclerView = rootView.findViewById(R.id.recyclerViewPanell);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        mAdapter = new PanellRecyclerAdapter(mIcones, rootView.getContext());
        mRecyclerView.setAdapter(mAdapter);

        panelTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {
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

                if(optionsButton.getTag() != null) {

                    if(position >= 0){
                        mListener.onPanellButtonPressed(
                                optionsButton,
                                panelTitle,
                                GestorUser.getPanells().get(position).getId()
                        );
                    }
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

}