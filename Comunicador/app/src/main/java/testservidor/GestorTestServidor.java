package testservidor;

import android.util.Log;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class GestorTestServidor {

    public void TestServidor(String username, String clau, ServerResponseCallBack serverResponseCallBack){

        Thread thread = new Thread(new Runnable() {

            @Override
            public void run() {
                try {

                    String urlParameters = "username="+ username +"&password="+ clau +"&grant_type=password";
                    byte[] postData = urlParameters.getBytes( StandardCharsets.UTF_8 );
                    int postDataLength = postData.length;
                    //Atenció a Android Studio he d'utilitzar 10.0.2.2, en comptes de localhost.
                    String request = "http://10.0.2.2:8080/oauth/token";
                    URL url = new URL( request );
                    HttpURLConnection conn= (HttpURLConnection) url.openConnection();
                    conn.setDoOutput( true );
                    conn.setInstanceFollowRedirects( false );
                    conn.setRequestMethod( "POST" );
                    conn.setRequestProperty( "Authorization", "Basic YW5kcm9pZGFwcDoxMjM0NQ==");
                    conn.setRequestProperty( "Content-Type", "application/x-www-form-urlencoded");
                    conn.setRequestProperty( "charset", "utf-8");
                    conn.setRequestProperty( "Content-Length", Integer.toString( postDataLength ));
                    conn.setUseCaches( false );
                    try( DataOutputStream wr = new DataOutputStream( conn.getOutputStream())) {

                        wr.write(postData);

                        int responseCode = conn.getResponseCode();
                        serverResponseCallBack.onCallBack(String.valueOf(responseCode));
                        Log.i("Info", "Response: " + responseCode);

                    }
                    conn.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
    }
}
