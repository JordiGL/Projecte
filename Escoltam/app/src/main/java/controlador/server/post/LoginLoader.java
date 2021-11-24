package controlador.server.post;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class LoginLoader extends AsyncTaskLoader<Bundle> {
    private String username;
    private String password;

    public LoginLoader(@NonNull Context context, String username, String password) {
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
    public Bundle loadInBackground() {
        return NetworkUtils.requestToken(username, password);
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
