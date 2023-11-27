package de.fhdw.app_entwicklung.chatgpt.webapi;

import android.renderscript.ScriptGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Logger;

public class WebAPI {
    public final static String ERROR_RESPONSE = "Es konnten keine Wetterdaten abgerufen werden.";

    public static String fetchDataFromApi(String endpoint) throws IOException {
        CompletableFuture<String> weatherFuture = CompletableFuture.supplyAsync(() -> {
            InputStream inputStream = connectAndGetInputStream(endpoint);
            String jsonResponse = parseInputStream(inputStream);

            return jsonResponse;
        });

        try {
            String response = weatherFuture.get();

            if (response != null) {
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ERROR_RESPONSE;
    }

    private static InputStream connectAndGetInputStream(String endpoint) {
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(endpoint);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            return urlConnection.getInputStream();
        } catch(Exception e) {

        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return null;
    }

    private static String parseInputStream(InputStream inputStream){
        BufferedReader reader = null;

        if (inputStream == null) {
            return null;
        }

        try {
            StringBuilder buffer = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                return null;
            }

            return buffer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }
}
