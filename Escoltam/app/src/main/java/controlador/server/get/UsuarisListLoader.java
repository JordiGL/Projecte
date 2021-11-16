package controlador.server.get;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

/**
 * Classe per a crear el fil per a connectar amb el servidor.
 * @author Jordi Gómez Lozano.
 */
public class UsuarisListLoader extends AsyncTaskLoader<String> {
    private String token;
    private String opcio;

    public UsuarisListLoader(@NonNull Context context,String token, String opcio) {
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
        return NetworkUtils.getUsuarisData(token, opcio);
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
