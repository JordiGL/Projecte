package controlador.server.test.post;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class TestTranslatorLoader extends AsyncTaskLoader<Integer> {
    private String subscriptionKey;
    private String location;
    private String text;
    public TestTranslatorLoader(@NonNull Context context, String text, String subscriptionKey, String location) {
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
    public Integer loadInBackground() {
        return NetworkUtils.testRequestTranslator(text, subscriptionKey, location);
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
