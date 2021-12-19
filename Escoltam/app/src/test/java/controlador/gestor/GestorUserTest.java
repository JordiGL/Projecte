package controlador.gestor;

import static org.junit.Assert.*;

import org.junit.Before;

import java.util.ArrayList;
import java.util.List;

import model.Icona;
import model.Panell;

/**
 * Classe per a fer el test sobre els mètodes de la classe GestorUser.
 * @author Jordi Gómez Lozano
 */
public class GestorUserTest {

    GestorUser gestorUser;

    @Before
    public void setUp() throws Exception {
        Icona icona = new Icona("Tennis", 1, new byte[]{}, 1);
        List<Icona> icones = new ArrayList<>();
        icones.add(icona);
        Panell panell = new Panell("Esports", 1, true, icones, 1);
        List<Panell> panells = new ArrayList<>();
        panells.add(panell);

        gestorUser = new GestorUser(panells);
    }

    /**
     * Comprovació de del mètode per obtenir el nombre de panells.
     * @author Jordi Gómez Lozano
     */
    @org.junit.Test
    public void getNumPanells() {
        assertNotEquals(3, gestorUser.getNumPanells());
        assertEquals(1, gestorUser.getNumPanells());
    }

    /**
     * Comprovació del mètode per a crear un nou panell.
     * @author Jordi Gómez Lozano
     */
    @org.junit.Test
    public void newPanell() {

        assertEquals("Acció", gestorUser.newPanell(2, "Acció").getNom());
        assertNotEquals(3, gestorUser.newPanell(2, "Acció").getPosicio());
    }

    /**
     * Comprovació del mètode per a fer la cerca i assignar a la variable
     * la posicio del panell favorit.
     * @author Jordi Gómez Lozano
     */
    @org.junit.Test
    public void setUpPanellFavoritePosition() {
        gestorUser.setUpPanellFavoritePosition();
        int panellFavorit = gestorUser.getPanellFavoritePosition();
        assertEquals(panellFavorit = 0, gestorUser.getPanellFavoritePosition());
        assertNotEquals(panellFavorit = 2, gestorUser.getPanellFavoritePosition());
    }

    /**
     * Comprovació del mètode per a obtenir el panell favorit.
     * @author Jordi Gómez Lozano
     */
    @org.junit.Test
    public void getPanellFavorite() {
        assertEquals("Esports", gestorUser.getPanellFavorite().getNom());
        assertNotEquals("Acció", gestorUser.getPanellFavorite().getNom());
    }

    /**
     * Comprovació del mètode per a cercar una icona pel seu id.
     * @author Jordi Gómez Lozano
     */
    @org.junit.Test
    public void findIcona() {
        assertEquals("Tennis", gestorUser.findIcona(1).getNom());
        assertNull(gestorUser.findIcona(3));
    }

}