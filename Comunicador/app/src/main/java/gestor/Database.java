/*
package gestor;

import java.sql.DriverManager;
import java.sql.Connection;
import gestor.GestorException;
import gestor.GestorUsuari;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

public class Database {

    private Connection connection;

    private final String host = "10.0.2.2";
    private final String database = "db_escoltam";
    private final int port = 5432;
    private final String user = "postgres";
    private final String pass = "pass";
    private String url = "jdbc:postgresql://10.0.2.2:5432/db_escoltam";
    //private String url = "jdbc:sqlserver://jogomloz.database.windows.net:1433;database=comunicador;user=jordi@jogomloz;password=Test0001;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;";
    private boolean status;

    public Database() {
        //this.url = String.format(this.url, this.host, this.port, this.database);
        //getExtraConnection();
        connect();
        //this.disconnect();
        System.out.println("connection status:" + status);
    }

    private void connect() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Class.forName("net.sourceforge.jtds.jdbc.Driver");
                    connection = DriverManager.getConnection(url, user, pass);
                    status = true;
                    System.out.println("connected:" + status);
                } catch (Exception e) {
                    status = false;
                    System.out.print(e.getMessage());
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        try {
            thread.join();
        } catch (Exception e) {
            e.printStackTrace();
            this.status = false;
        }
    }

    public Connection getExtraConnection(){
        //Connection c = null;
        try {
            Class.forName("org.postgresql.Driver");
            connection = DriverManager.getConnection(url, user, pass);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
*/
