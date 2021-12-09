package controlador.activity;

import android.util.Log;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import java.util.ArrayList;
import java.util.List;

import controlador.fragment.PanellFragment;
import model.Icona;

/**
 * A simple pager adapter that represents 5 ScreenSlidePageFragment objects, in
 * sequence.
 */
public class ScreenSlidePagerAdapter extends FragmentStateAdapter {
    /**
     * The number of pages (wizard steps) to show in this demo.
     */
    private static final int NUM_PAGES = 5;

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
        Log.i("Info", "createFragment");
        Log.i("Info", String.valueOf(position));
        return PanellFragment.newInstance(position
        );
    }

    @Override
    public int getItemCount() {
        return NUM_PAGES;
    }
}
