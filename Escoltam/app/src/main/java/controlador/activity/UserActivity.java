package controlador.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import controlador.fragment.PanellFragment;
import controlador.fragment.UserControlFragment;
import controlador.fragment.UserFavoritesFragment;
import controlador.fragment.UserToolbarFragment;
import controlador.gestor.GestorSharedPreferences;
import jordigomez.ioc.cat.escoltam.R;
import model.Icona;
import model.Panell;

/**
 * Activitat del client.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano
 */
public class UserActivity extends FragmentActivity {

    public static final String ROLE_USER = "ROLE_USER";
    private UserToolbarFragment toolbarFragment;
//    private UserFavoritesFragment favoritesFragment;
    private UserControlFragment controlFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String role;


    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

    /**
     * The pager widget, which handles animation and allows swiping horizontally to access previous
     * and next wizard steps.
     */
    private ViewPager2 viewPager;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private FragmentStateAdapter pagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        //Amagar barra superior de la info del dispositiu.
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

//        TextView textInfo = findViewById(R.id.textMostrarRol);

        Intent intent = getIntent();
//        textInfo.setText(intent.getStringExtra(LoginActivity.EXTRA_MESSAGE));
        role = intent.getStringExtra(LoginActivity.EXTRA_MESSAGE);

        //Fragments
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction =  fragmentManager.beginTransaction();

        toolbarFragment = UserToolbarFragment.newInstance(role);
//        favoritesFragment = UserFavoritesFragment.newInstance();
        controlFragment = UserControlFragment.newInstance();

        fragmentTransaction.add(R.id.toolbar_fragment_container, toolbarFragment);
//        fragmentTransaction.add(R.id.favorites_fragment_container, favoritesFragment);
        fragmentTransaction.add(R.id.control_fragment_container, controlFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        // Instantiate a ViewPager2 and a PagerAdapter.
        viewPager = findViewById(R.id.pager);
        viewPager.setPageTransformer(new ZoomOutPageTransformer());
        pagerAdapter = new UserActivity.ScreenSlidePagerAdapter(this);
        viewPager.setAdapter(pagerAdapter);

    }

    @Override
    public void onBackPressed() {
        if (viewPager.getCurrentItem() == 0) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            viewPager.setCurrentItem(viewPager.getCurrentItem() - 1);
        }
    }

    /**
     * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
     * sequence.
     */
    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fa) {
            super(fa);
        }

        @Override
        public Fragment createFragment(int position) {
            List<Icona> icones = new ArrayList<>();
            icones.add(new Icona("hola", 1));
            icones.add(new Icona("mon", 2));
            icones.add(new Icona("hola", 3));
            icones.add(new Icona("mon", 4));
            icones.add(new Icona("hola", 5));
            icones.add(new Icona("mon", 6));
            icones.add(new Icona("hola", 7));
            icones.add(new Icona("mon", 8));
            icones.add(new Icona("hola", 9));
            icones.add(new Icona("mon", 10));
            icones.add(new Icona("hola", 11));
            icones.add(new Icona("mon", 12));
            icones.add(new Icona("hola", 13));
            icones.add(new Icona("mon", 14));
            icones.add(new Icona("hola", 15));
            icones.add(new Icona("mon", 16));
            icones.add(new Icona("hola", 17));
            icones.add(new Icona("mon", 18));
            icones.add(new Icona("hola", 19));
            icones.add(new Icona("mon", 20));
            icones.add(new Icona("hola", 21));
            icones.add(new Icona("mon", 22));
            icones.add(new Icona("hola", 23));
            icones.add(new Icona("mon", 24));
            icones.add(new Icona("hola", 25));
            icones.add(new Icona("mon", 26));

            return PanellFragment.newInstance(icones);
        }

        @Override
        public int getItemCount() {
            return NUM_PAGES;
        }
    }

}