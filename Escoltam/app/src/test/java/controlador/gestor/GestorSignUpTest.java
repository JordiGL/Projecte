package controlador.gestor;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

/**
 * Classe per a fer el test del registre d'usuari.
 * @author Jordi Gómez Lozano
 */
public class GestorSignUpTest {
    private GestorSignUp gestorSignUp;

    @Before
    public void setUp() throws Exception {

        gestorSignUp = new GestorSignUp();
    }


    /**
     * Comprovació de l'entrada del camp de l'email per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void testEmailChecker() {

        gestorSignUp.setEmail("");
        assertFalse(gestorSignUp.emailChecker());
        gestorSignUp.setEmail("Email malformat @gmail.com");
        assertFalse(gestorSignUp.emailChecker());
        gestorSignUp.setEmail("Emailbenformat@gmail.com");
        assertTrue(gestorSignUp.emailChecker());
    }

    /**
     * Comprovació de l'entrada del camp de la clau per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void testPasswordChecker() {

        gestorSignUp.setPassword("");
        assertFalse(gestorSignUp.passwordChecker());
        gestorSignUp.setPassword("password");
        assertFalse(gestorSignUp.passwordChecker());
        gestorSignUp.setPassword("curta");
        assertFalse(gestorSignUp.passwordChecker());
        gestorSignUp.setPassword("cincdigitssensenumero");
        assertFalse(gestorSignUp.passwordChecker());
        gestorSignUp.setPassword("5digitsambnumero");
        assertTrue(gestorSignUp.passwordChecker());
    }

    /**
     * Comprovació de l'entrada del camp de la clau a confirmar per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void testConformPasswordChecker() {

        gestorSignUp.setPassword("");
        gestorSignUp.setConformPassword("123456789");
        assertFalse(gestorSignUp.conformPasswordChecker());

        gestorSignUp.setPassword("123456789");
        gestorSignUp.setConformPassword("");
        assertFalse(gestorSignUp.conformPasswordChecker());

        gestorSignUp.setPassword("123");
        gestorSignUp.setConformPassword("123456789");
        assertFalse(gestorSignUp.conformPasswordChecker());

        gestorSignUp.setPassword("123456789");
        gestorSignUp.setConformPassword("123");
        assertFalse(gestorSignUp.conformPasswordChecker());

        gestorSignUp.setPassword("123456789");
        gestorSignUp.setConformPassword("123456789");
        assertTrue(gestorSignUp.conformPasswordChecker());
    }

    /**
     * Comprovació de l'entrada del camp de la veu per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void testVoiceChecker() {

        gestorSignUp.setVoice("");
        assertFalse(gestorSignUp.voiceChecker());
        gestorSignUp.setVoice("hjkasdf");
        assertFalse(gestorSignUp.voiceChecker());
        gestorSignUp.setVoice("MALE");
        assertTrue(gestorSignUp.voiceChecker());
        gestorSignUp.setVoice("FEMALE");
        assertTrue(gestorSignUp.voiceChecker());
    }
}