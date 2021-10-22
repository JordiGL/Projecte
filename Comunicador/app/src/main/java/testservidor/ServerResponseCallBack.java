package testservidor;

/**
 * Interface per extreure la informació del metode que fa el request.
 * @author Jordi Gómez Lozano
 */
public interface ServerResponseCallBack {
    void onCallBack(String response);
}
