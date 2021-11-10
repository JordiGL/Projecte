package controlador.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import jordigomez.ioc.cat.escoltam.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AdminToolbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AdminToolbarFragment extends Fragment {


    public AdminToolbarFragment() {
        // Required empty public constructor
    }

    public static AdminToolbarFragment newInstance(){
        return new AdminToolbarFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView =  inflater.inflate(R.layout.fragment_admin_toolbar, container, false);
        final Spinner spinner = rootView.findViewById(R.id.spinner_object);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(rootView.getContext(),
                R.array.search_options, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        return rootView;
    }
}