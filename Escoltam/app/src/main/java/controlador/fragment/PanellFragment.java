package controlador.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import controlador.activity.AdministratorActivity;
import controlador.activity.PanellRecyclerAdapter;
import controlador.activity.UsuariAdapter;
import controlador.gestor.GestorUser;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PanellFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PanellFragment extends Fragment {
    private static List<Icona> mIcones;
    private RecyclerView mRecyclerView;
    private PanellRecyclerAdapter mAdapter;
    private int spanCount = 3;
    private static int position;
    private TextView panelTitle;

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
        panelTitle.setText(GestorUser.mPanells.get(position).getNom());

        mIcones = GestorUser.mPanells.get(position).getIcones();

        //RecyclerView i adapter.
        mRecyclerView = rootView.findViewById(R.id.recyclerViewPanell);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), spanCount));
        mAdapter = new PanellRecyclerAdapter(mIcones, rootView.getContext());
        mRecyclerView.setAdapter(mAdapter);

        return rootView;
    }
}