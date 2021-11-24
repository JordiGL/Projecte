package controlador.server.test.post;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class TestLoginLoader extends AsyncTaskLoader<Integer> {
    private String username;
    private String password;

    public TestLoginLoader(@NonNull Context context, String username, String password) {
        super(context);
        this.username = username;
        this.password = password;
    }


    /**
     * Designem l'operació a fer.
     * @return l'int amb el codi de resposta del servidor.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    @Override
    public Integer loadInBackground() {
        return NetworkUtils.testRequestToken(username, password);
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
