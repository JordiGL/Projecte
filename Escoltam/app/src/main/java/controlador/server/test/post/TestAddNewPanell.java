package controlador.server.test.post;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class TestAddNewPanell extends AsyncTaskLoader<Integer> {
    private String panell;
    private String username;
    private String token;

    public TestAddNewPanell(@NonNull Context context, String panell, String username, String token) {
        super(context);
        this.panell = panell;
        this.username = username;
        this.token = token;
    }


    @Nullable
    @Override
    public Integer loadInBackground() {
        return NetworkUtils.TestAddNewPanell(panell, username, token);
    }

    /**
     * El sistema crida aquest metode quan iniciem el loader per a fer
     * l'operació del loadInBackground.
     * @author Jordi Gómez Lozano.
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
