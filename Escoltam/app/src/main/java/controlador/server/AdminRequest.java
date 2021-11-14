package controlador.server;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.nio.charset.StandardCharsets;

public class AdminRequest extends Connexio {
    private static final String BASIC_URL = "http://10.0.2.2:8080/api/usuaris/";
//    private String dataReceived;
    private Context context;

    public AdminRequest(Context context) {
        this.context = context;
    }

    public AdminRequest(){};


//    public String getDataReceived(){
//        return dataReceived;
//    }

//    public int requestAllUsers(String token, String url){
//        int responseCode = 0;
//        HttpURLConnection connexio = null;
//        BufferedReader reader = null;
//
//        try{
//            connexio = getRequest(token,url);
//
//            responseCode = connexio.getResponseCode();
//
//            Log.d("Info", String.valueOf(responseCode));
//
//            InputStream inputStream = connexio.getInputStream();
//            reader = new BufferedReader(new InputStreamReader(inputStream));
//
//            // StringBuilder on hi guardarem la resposta.
//            StringBuilder builder = new StringBuilder();
//
//            String line;
//            while ((line = reader.readLine()) != null) {
//                builder.append(line);
//                builder.append("\n");
//            }
//
//            if (builder.length() == 0) {
//                return 0;
//            }
//
//            dataReceived = builder.toString();
//
//            Log.d("Info", dataReceived);
//
//        }catch (IOException e){
//            e.printStackTrace();
//        } finally{
//            if (connexio != null) {
//                connexio.disconnect();
//            }
//        }
//
//        return responseCode;
//    }

    public String requestData(String token, String opcio){
        HttpURLConnection connexio = null;
        BufferedReader reader = null;
        String receivedData = null;

        try{
            connexio = getRequest(token, BASIC_URL + opcio);

//            responseCode = connexio.getResponseCode();
//
//            Log.d("Info", String.valueOf(responseCode));

            InputStream inputStream = connexio.getInputStream();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            // StringBuilder on hi guardarem la resposta.
            StringBuilder builder = new StringBuilder();

            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
                builder.append("\n");
            }

            if (builder.length() == 0) {
                return null;
            }

            receivedData = builder.toString();

            Log.d("Info", receivedData);

        }catch (IOException e){
            e.printStackTrace();
        } finally{
            if (connexio != null) {
                connexio.disconnect();
            }
        }

        return receivedData;
    }

}
