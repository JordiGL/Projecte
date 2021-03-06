package controlador.server.get;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

/**
 * Classe per a crear el fil per a connectar amb el servidor.
 * @see AsyncTaskLoader
 * @author Jordi Gómez Lozano.
 */
public class UsuarisListLoader extends AsyncTaskLoader<String> {
    private String token;
    private String opcio;

    public UsuarisListLoader(@NonNull Context context,String opcio, String token) {
        super(context);
        this.token = token;
        this.opcio = opcio;
    }

    /**
     * Designem l'operació a fer.
     * @return l'String amb la llista d'usuaris.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    @Override
    public String loadInBackground() {
        return NetworkUtils.getUsuarisData(opcio, token);
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
