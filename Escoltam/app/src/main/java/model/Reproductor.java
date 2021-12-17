package model;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.util.Log;

import com.microsoft.cognitiveservices.speech.AudioDataStream;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import java.io.File;

import controlador.activity.MainActivity;
import controlador.gestor.GestorException;

public class Reproductor {
    private static final String LANGUAGE_CATALA = "ca-ES";
    private static final String VOICE_FEMALE = "FEMALE";
    private static final String VOICE_MALE = "MALE";
    private static final String VOICE_FEMALE_CATALA = "AlbaNeural";
    private static final String VOICE_MALE_CATALA = "EnricNeural";
    private static final String SERVICE_LOCATION = "francecentral";
    private static final String SERVICE_KEY = "d45fc56967c9409a9695c6c06bceed9f";
    private static final String ERROR_LANGUAGE = "Idioma no disponible.";
    private static final String ERROR_LANGUAGE_SELECTED = "S'ha produit un error en la selecció de l'idioma";
    private static final String ERROR_VOICE = "Veu no disponible";
    private static final String ERROR_VOICE_SELECTED = "S'ha produit un error en la selecció de la veu";
    private static final String ERROR_SYNTHESIZER = "S'ha produit un error a l'hora de configurar el sintetitzador del reproductor";
    private static final String ERROR_FAILED_TO_PLAY = "S'ha produit un error a l'hora d'efectuar la reproducció";
    private String subscriptionKey;
    private String location;
    private String idioma;
    private String veu;
    private String toDeVeu;
    private static SpeechSynthesizer synthesizer;
    private Context context;

    public Reproductor(String subscriptionKey, String location, String idioma, String toDeVeu) throws GestorException{
        this.subscriptionKey = subscriptionKey;
        this.location = location;
        this.toDeVeu = toDeVeu;
        setIdioma(idioma);
        setVeu(idioma, toDeVeu);
        synthesizer = setSynthesizer();
    }

    public Reproductor(String toDeVeu, Context context) throws GestorException{
        this.context = context;
        this.subscriptionKey = SERVICE_KEY;
        this.location = SERVICE_LOCATION;
        this.toDeVeu = toDeVeu;
        setIdioma(LANGUAGE_CATALA);
        setVeu(idioma, toDeVeu);
        synthesizer = setSynthesizer();
    }

    public void setSubscriptionKey(String subscriptionKey) {
        this.subscriptionKey = subscriptionKey;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getToDeVeu() {
        return toDeVeu;
    }

    public void setToDeVeu(String toDeVeu) {
        this.toDeVeu = toDeVeu;
    }


    private void setIdioma(String idioma) throws GestorException{

        try{

            if(idioma.equalsIgnoreCase(LANGUAGE_CATALA)){
                this.idioma = LANGUAGE_CATALA;
            }else{
                throw new GestorException(ERROR_LANGUAGE);
            }

        }catch(Exception e){
            throw new GestorException(ERROR_LANGUAGE_SELECTED + e);
        }
    }

    public String getIdioma() {
        return idioma;
    }

    private void setVeu(String idioma, String toDeVeu) throws GestorException{

        try{

            if(toDeVeu.equalsIgnoreCase(VOICE_FEMALE) || toDeVeu.equalsIgnoreCase(VOICE_MALE)){

                if(idioma.equalsIgnoreCase(LANGUAGE_CATALA)){

                    if (toDeVeu.equalsIgnoreCase(VOICE_FEMALE)){
                        veu = VOICE_FEMALE_CATALA;
                    } else if(toDeVeu.equalsIgnoreCase(VOICE_MALE)){
                        veu = VOICE_MALE_CATALA;
                    }
                }

            }else{
                throw new GestorException(ERROR_VOICE);
            }

        }catch(Exception e){
            throw new GestorException(ERROR_VOICE_SELECTED + e);
        }
    }

    public String getVeu() {
        return veu;
    }

    private SpeechSynthesizer setSynthesizer() throws GestorException{

        try{

            SpeechConfig speechConfig = SpeechConfig.fromSubscription(
                    subscriptionKey,
                    location
            );

            speechConfig.setSpeechSynthesisVoiceName(
                    "Microsoft Server Speech Text to Speech Voice ("
                            + idioma +", "
                            + veu +")"
            );

            synthesizer = new SpeechSynthesizer(speechConfig, null);

            return synthesizer;

        }catch(Exception e){
            throw new GestorException(ERROR_SYNTHESIZER + e);
        }
    }

    public void getAudio(String text) throws GestorException{

        try{

            SpeechSynthesisResult result = synthesizer.SpeakText(text);
            AudioDataStream stream = AudioDataStream.fromResult(result);
            stream.saveToWavFile(context.getFilesDir()+"audio.wav");

//            mediaPlayer = new MediaPlayer();
//            mediaPlayer.setDataSource(context.getFilesDir()+"audio.wav");
//            mediaPlayer.prepareAsync();
//            mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
//                @Override
//                public void onPrepared(MediaPlayer mp) {
//                    mediaPlayer.start();
//                }
//            });
//
//            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
//                public void onCompletion(MediaPlayer mp) {
//                    synthesizer.StopSpeakingAsync();
//                }
//            });

        }catch(Exception e){
            throw new GestorException(ERROR_FAILED_TO_PLAY + e);
        }
    }

    public void stop() throws GestorException{

        try{
            synthesizer.StopSpeakingAsync();
//            mediaPlayer.stop();

        }catch(Exception e){
            throw new GestorException(ERROR_FAILED_TO_PLAY + e);
        }
    }

    public void pause() throws GestorException{

        try{

//            mediaPlayer.pause();

        }catch(Exception e){
            throw new GestorException(ERROR_FAILED_TO_PLAY + e);
        }
    }
}
