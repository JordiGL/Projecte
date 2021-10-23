package controlador.gestor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Usuari;

public class GestorLoginTest {
    GestorLogin gestorLogin;

    @Before
    public void setUp() throws Exception {
        gestorLogin = new GestorLogin();
    }

    @Test
    public void testEmailChecker() {
        gestorLogin.setEmail("");
        assertFalse(gestorLogin.emailChecker());
        gestorLogin.setEmail("Email malformat @gmail.com");
        assertFalse(gestorLogin.emailChecker());
        gestorLogin.setEmail("Emailbenformat@gmail.com");
        assertTrue(gestorLogin.emailChecker());
    }

    @Test
    public void testPasswordChecker() {
        gestorLogin.setPassword("");
        assertFalse(gestorLogin.passwordChecker());
        gestorLogin.setPassword("curta");
        assertFalse(gestorLogin.passwordChecker());
        gestorLogin.setPassword("vuitDigitsSenseNumero");
        assertFalse(gestorLogin.passwordChecker());
        gestorLogin.setPassword("8DigitsOMes");
        assertTrue(gestorLogin.passwordChecker());
    }

    @Test
    public void testCheckAuthentication() {
        List<Usuari> basededades = new ArrayList<>();
        Usuari usuari = new Usuari(
                "jogomloz@gmail.com",
                1,
                "male",
                "123456789",
                false
        );
        basededades.add(usuari);

        gestorLogin.setEmail("jogomloz@gmail.com");
        gestorLogin.setPassword("qwerqwer123");
        assertFalse(gestorLogin.checkAuthenticationForTest(basededades));

        gestorLogin.setEmail("jogomloz@gmail.com");
        gestorLogin.setPassword("123456789");
        assertTrue(gestorLogin.checkAuthenticationForTest(basededades));
    }
}