package gestor;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import model.Usuari;

public class GestorRegistreTest{
    private GestorRegistre gestorRegistre;

    @Before
    public void setUp() throws Exception {

        gestorRegistre = new GestorRegistre();
    }

    @Test
    public void testEmailChecker() {
        gestorRegistre.setEmail("");
        assertFalse(gestorRegistre.emailChecker());
        gestorRegistre.setEmail("Email malformat @gmail.com");
        assertFalse(gestorRegistre.emailChecker());
        gestorRegistre.setEmail("Emailbenformat@gmail.com");
        assertTrue(gestorRegistre.emailChecker());
    }
    @Test
    public void testPasswordChecker() {
        gestorRegistre.setPassword("");
        assertFalse(gestorRegistre.passwordChecker());
        gestorRegistre.setPassword("curta");
        assertFalse(gestorRegistre.passwordChecker());
        gestorRegistre.setPassword("vuitdigitssensenumero");
        assertFalse(gestorRegistre.passwordChecker());
        gestorRegistre.setPassword("8digits8");
        assertTrue(gestorRegistre.passwordChecker());
    }
    @Test
    public void testConformPasswordChecker() {
        gestorRegistre.setPassword("");
        gestorRegistre.setConformPassword("123456789");
        assertFalse(gestorRegistre.conformPasswordChecker());

        gestorRegistre.setPassword("123456789");
        gestorRegistre.setConformPassword("");
        assertFalse(gestorRegistre.conformPasswordChecker());

        gestorRegistre.setPassword("123");
        gestorRegistre.setConformPassword("123456789");
        assertFalse(gestorRegistre.conformPasswordChecker());

        gestorRegistre.setPassword("123456789");
        gestorRegistre.setConformPassword("123");
        assertFalse(gestorRegistre.conformPasswordChecker());

        gestorRegistre.setPassword("123456789");
        gestorRegistre.setConformPassword("123456789");
        assertTrue(gestorRegistre.conformPasswordChecker());
    }
    @Test
    public void testVoiceChecker() {
        gestorRegistre.setVoice("");
        assertFalse(gestorRegistre.voiceChecker());
        gestorRegistre.setVoice("male");
        assertTrue(gestorRegistre.voiceChecker());
        gestorRegistre.setVoice("female");
        assertTrue(gestorRegistre.voiceChecker());
    }
}