package controlador.server.post;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class NewIconLoader  extends AsyncTaskLoader<Bundle> {
    private Context context;
    private int idPanell;
    private String name;
    private int position;
    private String fileName;
    private String token;

    public NewIconLoader(@NonNull Context context, int idPanell, String name, int position,
                         String fileName, String token) {
        super(context);
        this.context = context;
        this.idPanell = idPanell;
        this.name = name;
        this.position = position;
        this.fileName = fileName;
        this.token = token;
    }

    /**
     * Designem l'operació a fer.
     * @return Un Bundle amb el codi de resposta del servidor, i les dades de retorn.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    @Override
    public Bundle loadInBackground() {

        return NetworkUtils.addNewIcon(context, idPanell, name, position, fileName, token);
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
