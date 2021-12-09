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

import controlador.fragment.PanellFragment;
import controlador.fragment.UserControlFragment;
import controlador.fragment.UserToolbarFragment;
import controlador.gestor.GestorUser;
import jordigomez.ioc.cat.escoltam.R;

/**
 * Activitat del client.
 * @see AppCompatActivity
 * @author Jordi Gómez Lozano
 */
public class UserActivity extends FragmentActivity{

    private static final int NUM_PANELLS = GestorUser.mPanells.size();
    private ViewPager2 viewPager;
    private FragmentStateAdapter pagerAdapter;
    private UserToolbarFragment toolbarFragment;
//    private UserFavoritesFragment favoritesFragment;
    private UserControlFragment controlFragment;
    private FragmentTransaction fragmentTransaction;
    private FragmentManager fragmentManager;
    private String role;

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
            return PanellFragment.newInstance(position);
        }

        @Override
        public int getItemCount() {
            return NUM_PANELLS;
        }
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

}