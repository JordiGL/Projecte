package controlador.server.test.delete;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class TestDeletePanellLoader extends AsyncTaskLoader<Integer> {
    private int idPanell;
    private String username;
    private String token;

    public TestDeletePanellLoader(@NonNull Context context, int idPanell, String username, String token) {
        super(context);
        this.idPanell = idPanell;
        this.username = username;
        this.token = token;
    }


    @Nullable
    @Override
    public Integer loadInBackground() {
        return NetworkUtils.testDeletePanell(idPanell, username, token);
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
