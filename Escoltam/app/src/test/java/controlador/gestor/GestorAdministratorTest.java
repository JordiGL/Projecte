package controlador.gestor;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * Classe per a fer el test de les classe AdministratorActivity.
 * @author Jordi Gómez Lozano
 */
public class GestorAdministratorTest {
    private GestorAdministrator gestorAdministrator;

    @Before
    public void setUp() throws Exception {
        gestorAdministrator = new GestorAdministrator();
    }

    /**
     * Comprovació de l'entrada del camp de l'email.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void emailChecker() {
        gestorAdministrator.setCercadorText("");
        assertFalse(gestorAdministrator.emailChecker());
        gestorAdministrator.setCercadorText("JoGomLoz@gmail.com");
        assertTrue(gestorAdministrator.emailChecker());
    }

    /**
     * Comprovació de l'entrada del camp del rol.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void roleChecker() {
        gestorAdministrator.setCercadorText("");
        assertFalse(gestorAdministrator.roleChecker());
        gestorAdministrator.setCercadorText("R");
        assertFalse(gestorAdministrator.roleChecker());
        gestorAdministrator.setCercadorText("ROLE_USER");
        assertTrue(gestorAdministrator.roleChecker());
        gestorAdministrator.setCercadorText("role_admin");
        assertTrue(gestorAdministrator.roleChecker());
    }

    /**
     * Comprovació de l'entrada del camp de la veu.
     * @author Jordi Gómez Lozano
     */
    @Test
    public void voiceChecker() {
        gestorAdministrator.setCercadorText("");
        assertFalse(gestorAdministrator.voiceChecker());
        gestorAdministrator.setCercadorText("voice");
        assertFalse(gestorAdministrator.voiceChecker());
        gestorAdministrator.setCercadorText("FEMALE");
        assertTrue(gestorAdministrator.voiceChecker());
        gestorAdministrator.setCercadorText("male");
        assertTrue(gestorAdministrator.voiceChecker());
    }
}