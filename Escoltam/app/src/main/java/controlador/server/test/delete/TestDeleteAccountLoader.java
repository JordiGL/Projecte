package controlador.server.test.delete;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class TestDeleteAccountLoader extends AsyncTaskLoader<Integer> {

    private String token;
    private String username;

    public TestDeleteAccountLoader(@NonNull Context context, String username, String token) {
        super(context);
        this.username = username;
        this.token = token;
    }


    @Nullable
    @Override
    public Integer loadInBackground() {
        return NetworkUtils.testDeleteAccount(username, token);
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
