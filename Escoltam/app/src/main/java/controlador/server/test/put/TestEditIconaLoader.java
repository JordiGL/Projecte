package controlador.server.test.put;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class TestEditIconaLoader extends AsyncTaskLoader<Integer> {
    private String name;
    private int position;
    private int idIcona;
    private String fileName;
    private String token;
    private Context context;

    public TestEditIconaLoader(@NonNull Context context, int idIcona, String name, int position,
                           String fileName, String token) {
        super(context);
        this.context = context;
        this.idIcona = idIcona;
        this.name = name;
        this.position = position;
        this.fileName = fileName;
        this.token = token;
    }

    /**
     * Designem l'operació a fer.
     *
     * @return l'int amb el codi de resposta del servidor.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    @Override
    public Integer loadInBackground() {
        return NetworkUtils.testEditIcona(context, idIcona, name, position, fileName, token);
    }

    /**
     * El sistema crida aquest metode quan iniciem el loader per a fer
     * l'operació del loadInBackground.
     *
     * @author Jordi Gómez Lozano.
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
