package controlador.server.put;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

/**
 * Classe per a crear el fil per a connectar amb el servidor.
 * @see AsyncTaskLoader
 * @author Jordi Gómez Lozano.
 */
public class ChangeVoiceLoader extends AsyncTaskLoader<Bundle> {
    private String password;
    private String email;
    private String token;
    private String voice;

    public ChangeVoiceLoader(@NonNull Context context, String password, String voice, String email, String token) {
        super(context);
        this.password = password;
        this.email = email;
        this.token = token;
        this.voice = voice;
    }

    /**
     * Designem l'operació a fer.
     * @return l'int amb el codi de resposta del servidor.
     * @author Jordi Gómez Lozano.
     */
    @Nullable
    @Override
    public Bundle loadInBackground() {
        return NetworkUtils.sendVoice(password, voice, email, token);
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
