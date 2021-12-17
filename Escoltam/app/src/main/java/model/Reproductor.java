package model;

import android.content.Context;

import androidx.annotation.NonNull;

import com.microsoft.cognitiveservices.speech.AudioDataStream;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesisResult;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;

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
    private static final String LANGUAGE_ENGLISH = "en-GB";
    private static final String VOICE_FEMALE_ENGLISH = "LibbyNeural";
    private static final String VOICE_MALE_ENGLISH = "RyanNeural";
    private static final String FILE_NAME = "audio.wav";
    private String subscriptionKey;
    private String location;
    private String idioma;
    private String systemVoice;
    private String userVoice;
    private static SpeechSynthesizer synthesizer;
    private Context context;

    public Reproductor(String userVoice, Context context) throws GestorException{
        this.context = context;
        this.subscriptionKey = SERVICE_KEY;
        this.location = SERVICE_LOCATION;
        this.userVoice = userVoice;
        this.idioma = LANGUAGE_CATALA;
        setUpSystemVoice(idioma);
        synthesizer = setUpSynthesizer();
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

    public String getUserVoice() {
        return userVoice;
    }

    public void setUserVoice(String userVoice) {
        this.userVoice = userVoice;
    }


    private void setIdioma(String idioma) throws GestorException{

        try{

            if (idioma.equalsIgnoreCase("en")){
                this.idioma = LANGUAGE_ENGLISH;
            }else if(idioma.equalsIgnoreCase("ca")){
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

    public void setSystemVoice(String systemVoice){
        this.systemVoice = systemVoice;
    }

    public String getSystemVoice(String idioma, @NonNull String userVoice) throws GestorException {
        String systemVoice = "";
        if(userVoice.equalsIgnoreCase(VOICE_FEMALE) || userVoice.equalsIgnoreCase(VOICE_MALE)){

            if (idioma.equalsIgnoreCase(LANGUAGE_ENGLISH)){

                if (userVoice.equalsIgnoreCase(VOICE_FEMALE)){
                    systemVoice = VOICE_FEMALE_ENGLISH;
                } else if(userVoice.equalsIgnoreCase(VOICE_MALE)){
                    systemVoice = VOICE_MALE_ENGLISH;
                }
            }else if(idioma.equalsIgnoreCase(LANGUAGE_CATALA)){

                if (userVoice.equalsIgnoreCase(VOICE_FEMALE)){
                    systemVoice = VOICE_FEMALE_CATALA;
                } else if(userVoice.equalsIgnoreCase(VOICE_MALE)){
                    systemVoice = VOICE_MALE_CATALA;
                }
            }

        }else{
            throw new GestorException(ERROR_VOICE);
        }

        return systemVoice;
    }

    public void setUpSystemVoice(String idioma) throws GestorException{

        try{

            if(userVoice.equalsIgnoreCase(VOICE_FEMALE) || userVoice.equalsIgnoreCase(VOICE_MALE)){

                if (idioma.equalsIgnoreCase(LANGUAGE_ENGLISH)){

                    if (userVoice.equalsIgnoreCase(VOICE_FEMALE)){
                        systemVoice = VOICE_FEMALE_ENGLISH;
                    } else if(userVoice.equalsIgnoreCase(VOICE_MALE)){
                        systemVoice = VOICE_MALE_ENGLISH;
                    }
                }else if(idioma.equalsIgnoreCase(LANGUAGE_CATALA)){

                    if (userVoice.equalsIgnoreCase(VOICE_FEMALE)){
                        systemVoice = VOICE_FEMALE_CATALA;
                    } else if(userVoice.equalsIgnoreCase(VOICE_MALE)){
                        systemVoice = VOICE_MALE_CATALA;
                    }
                }

            }else{
                throw new GestorException(ERROR_VOICE);
            }

        }catch(Exception e){
            throw new GestorException(ERROR_VOICE_SELECTED + e);
        }
    }


    private SpeechSynthesizer setUpSynthesizer() throws GestorException{

        try{

            SpeechConfig speechConfig = SpeechConfig.fromSubscription(
                    subscriptionKey,
                    location
            );

            speechConfig.setSpeechSynthesisVoiceName(
                    "Microsoft Server Speech Text to Speech Voice ("
                            + idioma +", "
                            + systemVoice +")"
            );

            synthesizer = new SpeechSynthesizer(speechConfig, null);

            return synthesizer;

        }catch(Exception e){
            throw new GestorException(ERROR_SYNTHESIZER + e);
        }
    }

    public void changeSynthesizer(String language, String userVoice) throws GestorException{

        try{
            String systemVoice = getSystemVoice(language, userVoice);

            SpeechConfig speechConfig = SpeechConfig.fromSubscription(
                    subscriptionKey,
                    location
            );

            speechConfig.setSpeechSynthesisVoiceName(
                    "Microsoft Server Speech Text to Speech Voice ("
                            + language +", "
                            + systemVoice +")"
            );

            synthesizer = new SpeechSynthesizer(speechConfig, null);

        }catch(Exception e){
            throw new GestorException(ERROR_SYNTHESIZER + e);
        }
    }


    public void getAudio(String text) throws GestorException{

        try{

            SpeechSynthesisResult result = synthesizer.SpeakText(text);
            AudioDataStream stream = AudioDataStream.fromResult(result);
            stream.saveToWavFile(context.getFilesDir()+ FILE_NAME);


        }catch(Exception e){
            throw new GestorException(ERROR_FAILED_TO_PLAY + e);
        }
    }

    public void stop() throws GestorException{

        try{
            synthesizer.StopSpeakingAsync();

        }catch(Exception e){
            throw new GestorException(ERROR_FAILED_TO_PLAY + e);
        }
    }
}
