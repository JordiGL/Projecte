package controlador.server.test.put;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class TestEditPanellLoader extends AsyncTaskLoader<Integer> {
    private String nom;
    private int posicio;
    private boolean favorit;
    private int idPanell;
    private String username;
    private String token;

    public TestEditPanellLoader(@NonNull Context context, String nom, int posicio, boolean favorit, int idPanell,
                            String username, String token) {
        super(context);
        this.nom = nom;
        this.posicio = posicio;
        this.token = token;
        this.favorit = favorit;
        this.idPanell = idPanell;
        this.username = username;
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
        return NetworkUtils.testEditPanell(nom, posicio, favorit, idPanell, username, token);
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
