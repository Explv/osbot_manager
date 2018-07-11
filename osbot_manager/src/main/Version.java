package main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Version {

    private final static float currentVersion = 8.1f;

    public static boolean isLatestVersion() {
        try {
            final URL url = new URL("https://raw.githubusercontent.com/Explv/osbot_manager/master/version");
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try (final InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                 final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                final String version = bufferedReader.readLine();

                System.out.println("Latest version: " + version);

                if (Float.parseFloat(version) > currentVersion) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }
}