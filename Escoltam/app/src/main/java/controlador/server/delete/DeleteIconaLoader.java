package controlador.server.delete;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

/**
 * Classe per a crear el fil per a connectar amb el servidor.
 * @see androidx.loader.content.AsyncTaskLoader
 * @author Jordi Gómez Lozano
 */
public class DeleteIconaLoader extends AsyncTaskLoader<Bundle> {
    private int idIcona;
    private String token;

    public DeleteIconaLoader(@NonNull Context context, int idIcona, String token) {
        super(context);
        this.idIcona = idIcona;
        this.token = token;
    }


    @Nullable
    @Override
    public Bundle loadInBackground() {
        return NetworkUtils.deleteIcona(idIcona, token);
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
