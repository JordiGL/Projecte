package controlador.server.put;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;

import controlador.server.NetworkUtils;

public class ChangeVoiceLoader extends AsyncTaskLoader<Integer> {
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
     */
    @Nullable
    @Override
    public Integer loadInBackground() {
        return NetworkUtils.sendVoice(password, voice, email, token);
    }

    /**
     * El sistema crida aquest metode quan iniciem el loader per a fer
     * l'operació del loadInBackground.
     */
    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad();
    }
}
