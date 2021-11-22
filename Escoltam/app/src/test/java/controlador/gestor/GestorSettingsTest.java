package controlador.gestor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Classe per a fer el test de les classes AdminSettings i UserSettings.
 * @author Jordi Gómez Lozano
 */
public class GestorSettingsTest {
    private GestorSettings gestorSettings;

    @Before
    public void setUp() throws Exception {

        gestorSettings = new GestorSettings();
    }

    /**
     * Comprovació de l'entrada del camp de la actual clau per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void previousPasswordChecker() {
        gestorSettings.setPreviousPassword("");
        gestorSettings.setReceivedPassword("12345");
        assertFalse(gestorSettings.previousPasswordChecker());
        gestorSettings.setPreviousPassword("45637");
        assertFalse(gestorSettings.previousPasswordChecker());
        gestorSettings.setPreviousPassword("12345");
        assertTrue(gestorSettings.previousPasswordChecker());
    }

    /**
     * Comprovació de l'entrada del camp de la nova clau per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void newPasswordChecker() {
        gestorSettings.setNewPassword("");
        assertFalse(gestorSettings.newPasswordChecker());
        gestorSettings.setNewPassword("password");
        assertFalse(gestorSettings.newPasswordChecker());
        gestorSettings.setNewPassword("curta");
        assertFalse(gestorSettings.newPasswordChecker());
        gestorSettings.setNewPassword("cincdigitssensenumero");
        assertFalse(gestorSettings.newPasswordChecker());
        gestorSettings.setNewPassword("5digitsambnumero");
        assertTrue(gestorSettings.newPasswordChecker());
    }

    /**
     * Comprovació de l'entrada del camp de la clau a confirmar per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void conformPasswordChecker() {
        gestorSettings.setNewPassword("");
        gestorSettings.setConformPassword("123456789");
        assertFalse(gestorSettings.conformPasswordChecker());

        gestorSettings.setNewPassword("123456789");
        gestorSettings.setConformPassword("");
        assertFalse(gestorSettings.conformPasswordChecker());

        gestorSettings.setNewPassword("123");
        gestorSettings.setConformPassword("123456789");
        assertFalse(gestorSettings.conformPasswordChecker());

        gestorSettings.setNewPassword("123456789");
        gestorSettings.setConformPassword("123");
        assertFalse(gestorSettings.conformPasswordChecker());

        gestorSettings.setNewPassword("123456789");
        gestorSettings.setConformPassword("123456789");
        assertTrue(gestorSettings.conformPasswordChecker());
    }

    /**
     * Comprovació de l'entrada del camp de la veu per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void voiceChecker() {
        gestorSettings.setVoice("");
        assertFalse(gestorSettings.voiceChecker());
        gestorSettings.setVoice("qwerqwer");
        assertFalse(gestorSettings.voiceChecker());
        gestorSettings.setVoice("MALE");
        assertTrue(gestorSettings.voiceChecker());
        gestorSettings.setVoice("FEMALE");
        assertTrue(gestorSettings.voiceChecker());
    }
}