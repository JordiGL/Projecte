package model;

import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechSynthesizer;
import com.microsoft.cognitiveservices.speech.audio.AudioConfig;

import controlador.gestor.GestorException;

public class Reproductor {
    private String subscriptionKey;
    private String location;
    private String idioma;
    private String veu;
    private String toDeVeu;
    private static SpeechSynthesizer synthesizer;

    public Reproductor(String subscriptionKey, String location, String idioma, String toDeVeu) throws GestorException{
        this.subscriptionKey = subscriptionKey;
        this.location = location;
        this.toDeVeu = toDeVeu;
        setIdioma(idioma);
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

            if (idioma.equalsIgnoreCase("en")){
                this.idioma = "en-GB";
            }else if(idioma.equalsIgnoreCase("es")){
                this.idioma = "es-ES";
            }else if(idioma.equalsIgnoreCase("ca")){
                this.idioma = "ca-ES";
            }else{
                throw new GestorException("Idioma no disponible.");
            }

        }catch(Exception e){
            throw new GestorException("S'ha produit un error en la selecció "
                    + "de l'idioma. " + e);
        }
    }

    public String getIdioma() {
        return idioma;
    }

    private void setVeu(String idioma, String toDeVeu) throws GestorException{

        try{

            if(toDeVeu.equalsIgnoreCase("femeni") || toDeVeu.equalsIgnoreCase("masculi")){

                if (idioma.equalsIgnoreCase("en")){

                    if (toDeVeu.equalsIgnoreCase("femeni")){
                        veu = "LibbyNeural";
                    } else if(toDeVeu.equalsIgnoreCase("masculi")){
                        veu = "RyanNeural";
                    }

                }else if(idioma.equalsIgnoreCase("es")){

                    if (toDeVeu.equalsIgnoreCase("femeni")){
                        veu = "ElviraNeural";
                    } else if(toDeVeu.equalsIgnoreCase("masculi")){
                        veu = "AlvaroNeural";
                    }

                }else if(idioma.equalsIgnoreCase("ca")){

                    if (toDeVeu.equalsIgnoreCase("femeni")){
                        veu = "AlbaNeural";
                    } else if(toDeVeu.equalsIgnoreCase("masculi")){
                        veu = "EnricNeural";
                    }
                }

            }else{
                throw new GestorException("Veu no disponible.");
            }

        }catch(Exception e){
            throw new GestorException("S'ha produit un error en la selecció "
                    + "de la veu. " + e);
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

            AudioConfig audioConfig = AudioConfig.fromDefaultSpeakerOutput();
            synthesizer = new SpeechSynthesizer(speechConfig, audioConfig);

            return synthesizer;

        }catch(Exception e){
            throw new GestorException("S'ha produit un error a l'hora de "
                    + "configurar el sintetitzador del reproductor. " + e);
        }
    }

    public void reproduir(String text) throws GestorException{

        try{

            synthesizer.SpeakText(text);

        }catch(Exception e){
            throw new GestorException("S'ha produit un error a l'hora "
                    + "d'efectuar la reproducció. " + e);
        }
    }
}
