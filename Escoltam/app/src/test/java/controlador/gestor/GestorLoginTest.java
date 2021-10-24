package controlador.gestor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import model.Usuari;

/**
 * Classe per a fer el test del login d'usuari.
 * @author Jordi Gómez Lozano
 */
public class GestorLoginTest {
    GestorLogin gestorLogin;

    @Before
    public void setUp() {
        gestorLogin = new GestorLogin();
    }

    /**
     * Comprovació de l'entrada del camp de l'email per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void testEmailChecker() {

        gestorLogin.setEmail("");
        assertFalse(gestorLogin.emailChecker());
        gestorLogin.setEmail("Email malformat @gmail.com");
        assertFalse(gestorLogin.emailChecker());
        gestorLogin.setEmail("Emailbenformat@gmail.com");
        assertTrue(gestorLogin.emailChecker());
    }

    /**
     * Comprovació de l'entrada del camp de la clau per part de l'usuari.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void testPasswordChecker() {

        gestorLogin.setPassword("");
        assertFalse(gestorLogin.passwordChecker());
        gestorLogin.setPassword("password");
        assertTrue(gestorLogin.passwordChecker());

//Desactivat de moment.
//        gestorLogin.setPassword("curta");
//        assertFalse(gestorLogin.passwordChecker());
//        gestorLogin.setPassword("vuitDigitsSenseNumero");
//        assertFalse(gestorLogin.passwordChecker());
    }

    /**
     * Comprovació de la clau d'usuari, aquesta ha de coincidir amb la de la base de dades.
     * @author Jordi Gómez Lozano
     */
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