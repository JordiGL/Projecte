package controlador.gestor;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetUser {
    private static final String BASE_URL =  "http://10.0.2.2:8080/api/usuaris";

    HttpURLConnection urlConnection = null;
    BufferedReader reader = null;
    String bookJSONString = null;


    public String getUsersList(){

        try{

            Uri builtURI = Uri.parse(BASE_URL);

            URL requestURL = null;

            requestURL = new URL(builtURI.toString());

            urlConnection = (HttpURLConnection) requestURL.openConnection();

            urlConnection.setRequestMethod("GET");

            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
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

            bookJSONString = builder.toString();

        } catch (IOException e){
            e.printStackTrace();
        } finally{
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        Log.d("Info", bookJSONString);

        return bookJSONString;
    }
}



