package dao;


import gestor.GestorException;
import model.Usuari;

/**
 * Interface DAO de l'usuari.
 * @Jordi Gómez Lozano.
 */
public interface DAOUsuari {

    public boolean insertar(Usuari usuari) throws GestorException;
    public boolean comprovar(String email);
    public Usuari obtenir(String email);
    public void updateEnable(String email, boolean enable);

}
