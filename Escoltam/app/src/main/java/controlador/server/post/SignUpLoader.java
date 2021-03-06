package controlador.server.post;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;
import model.Usuari;

/**
 * Classe per a crear el fil per a connectar amb el servidor.
 * @see AsyncTaskLoader
 * @author Jordi Gómez Lozano.
 */
public class SignUpLoader extends AsyncTaskLoader<Bundle> {
    private Usuari usuari;

    public SignUpLoader(@NonNull Context context, Usuari usuari) {
        super(context);
        this.usuari = usuari;
    }

    /**
     * Designem l'operació a fer.
     * @return un bundle amb el codi de resposta del servidor, i la informació.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    @Override
    public Bundle loadInBackground() {
        return NetworkUtils.addNewUser(usuari);
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
