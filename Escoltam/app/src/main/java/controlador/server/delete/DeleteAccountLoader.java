package controlador.server.delete;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class DeleteAccountLoader extends AsyncTaskLoader<Bundle> {
    private int idIcona;
    private String token;
    private String username;

    public DeleteAccountLoader(@NonNull Context context, String username, String token) {
        super(context);
        this.username = username;
        this.token = token;
    }


    @Nullable
    @Override
    public Bundle loadInBackground() {
        return NetworkUtils.deleteAccount(username, token);
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
