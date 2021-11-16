package controlador.server.post;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;
import model.Usuari;

public class SignUpLoader extends AsyncTaskLoader<Bundle> {
    private Usuari usuari;

    public SignUpLoader(@NonNull Context context, Usuari usuari) {
        super(context);
        this.usuari = usuari;
    }

    /**
     * Designem l'operació a fer.
     * @return l'int amb el codi de resposta del servidor.
     */
    @Nullable
    @Override
    public Bundle loadInBackground() {
        return NetworkUtils.addNewUser(usuari);
    }

    /**
     * El sistema crida aquest metode quan iniciem el loader per a fer
     * l'operació del loadInBackground.
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
