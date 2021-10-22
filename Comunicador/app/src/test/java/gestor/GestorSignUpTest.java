package gestor;


import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class GestorSignUpTest {
    private GestorSignUp gestorSignUp;

    @Before
    public void setUp() throws Exception {

        gestorSignUp = new GestorSignUp();
    }

    @Test
    public void testEmailChecker() {
        gestorSignUp.setEmail("");
        assertFalse(gestorSignUp.emailChecker());
        gestorSignUp.setEmail("Email malformat @gmail.com");
        assertFalse(gestorSignUp.emailChecker());
        gestorSignUp.setEmail("Emailbenformat@gmail.com");
        assertTrue(gestorSignUp.emailChecker());
    }
    @Test
    public void testPasswordChecker() {
        gestorSignUp.setPassword("");
        assertFalse(gestorSignUp.passwordChecker());
        gestorSignUp.setPassword("curta");
        assertFalse(gestorSignUp.passwordChecker());
        gestorSignUp.setPassword("vuitdigitssensenumero");
        assertFalse(gestorSignUp.passwordChecker());
        gestorSignUp.setPassword("8digits8");
        assertTrue(gestorSignUp.passwordChecker());
    }
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
    @Test
    public void testVoiceChecker() {
        gestorSignUp.setVoice("");
        assertFalse(gestorSignUp.voiceChecker());
        gestorSignUp.setVoice("male");
        assertTrue(gestorSignUp.voiceChecker());
        gestorSignUp.setVoice("female");
        assertTrue(gestorSignUp.voiceChecker());
    }
}