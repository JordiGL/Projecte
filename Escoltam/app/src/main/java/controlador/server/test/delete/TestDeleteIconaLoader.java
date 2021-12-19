package controlador.server.test.delete;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class TestDeleteIconaLoader extends AsyncTaskLoader<Integer> {
    private int idIcona;
    private String token;

    public TestDeleteIconaLoader(@NonNull Context context, int idIcona, String token) {
        super(context);
        this.idIcona = idIcona;
        this.token = token;
    }


    @Nullable
    @Override
    public Integer loadInBackground() {
        return NetworkUtils.testDeleteIcona(idIcona, token);
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
