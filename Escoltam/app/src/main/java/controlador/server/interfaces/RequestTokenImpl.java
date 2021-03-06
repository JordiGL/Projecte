package controlador.server.interfaces;

/**
 * Interficie del RequestToken
 * @author Jordi Gómez Lozano
 */
public interface RequestTokenImpl {
    public int requestToken(String username, String clau);
    public String getEmailFromToken(String token);
    public String getRoleFromToken(String token);
    public long getExpireTimeFromToken(String token);
}
