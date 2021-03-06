package controlador.server.post;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

/**
 * Classe loader per a crear un fil i connectar amb servei de traductor de text
 * @see androidx.loader.content.AsyncTaskLoader
 * @author Jordi Gómez Lozano
 */
public class TranslatorLoader extends AsyncTaskLoader<Bundle> {
    private String subscriptionKey;
    private String location;
    private String text;
    public TranslatorLoader(@NonNull Context context, String text, String subscriptionKey, String location) {
        super(context);
        this.subscriptionKey = subscriptionKey;
        this.location = location;
        this.text = text;
    }

    /**
     * Designem l'operació a fer.
     * @return Un Bundle amb el codi de resposta del servidor, i les dades de retorn.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    @Override
    public Bundle loadInBackground() {
        return NetworkUtils.requestTranslator(text, subscriptionKey, location);
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
