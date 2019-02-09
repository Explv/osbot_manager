package main;

import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Optional;

public class Updater {

    private static final String LATEST_RELEASE_URL = "https://api.github.com/repos/Explv/osbot_manager/releases/latest";

    private static final JSONObject latestReleaseJSON;

    static {
        Optional<JSONObject> latestReleaseJSONOpt = getLatestReleaseJSON();
        if (!latestReleaseJSONOpt.isPresent()) {
            System.err.print("Failed to get latest release JSON");
            System.exit(1);
        }
        latestReleaseJSON = latestReleaseJSONOpt.get();
    }

    private static Optional<JSONObject> getLatestReleaseJSON() {
        try {
            final URL url = new URL(LATEST_RELEASE_URL);
            final HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            try (final InputStreamReader inputStreamReader = new InputStreamReader(urlConnection.getInputStream());
                 final BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {

                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(bufferedReader);

                return Optional.of(jsonObject);
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static boolean isUpdateRequired() {
        String currentVersion = Updater.class.getPackage().getImplementationVersion();
        return !currentVersion.equals(latestReleaseJSON.get("tag_name"));
    }

    public static void update() {
        Dialog updateDialog = new Dialog();
        updateDialog.setTitle("Explv's OSBot Manager");
        updateDialog.setHeaderText("Updating");
        updateDialog.show();

        if (!downloadAndRunLatestRelease()) {
            updateDialog.close();

            Dialog errorDialog = new Dialog();
            errorDialog.setTitle("Explv's OSBot Manager");
            errorDialog.setHeaderText("Error!");
            errorDialog.setContentText("Failed to download latest version");
            errorDialog.getDialogPane().getButtonTypes().add(ButtonType.CLOSE);
            Node closeButton = errorDialog.getDialogPane().lookupButton(ButtonType.CLOSE);
            closeButton.managedProperty().bind(closeButton.visibleProperty());
            closeButton.setVisible(false);
            errorDialog.showAndWait();
            System.exit(1);
        }
    }

    private static boolean downloadAndRunLatestRelease() {
        try {
            JSONArray assetsArray = (JSONArray) latestReleaseJSON.get("assets");
            JSONObject asset = (JSONObject) assetsArray.get(0);
            String fileName = (String) asset.get("name");
            URL downloadURL = new URL((String) asset.get("browser_download_url"));

            String currentJARPath = Updater.class.getProtectionDomain().getCodeSource().getLocation().getPath();
            String decodedCurrentJARPath = URLDecoder.decode(currentJARPath, "UTF-8");

            String currentJARDirectory = new File(decodedCurrentJARPath).getParent();
            File outputFile = Paths.get(currentJARDirectory, fileName).toFile();

            ReadableByteChannel readableByteChannel = Channels.newChannel(downloadURL.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            ProcessBuilder processBuilder = new ProcessBuilder("java", "-jar", outputFile.toString());
            processBuilder.start();

            System.exit(0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
