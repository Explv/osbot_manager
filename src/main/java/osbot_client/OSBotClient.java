package osbot_client;

import settings.Settings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OSBotClient {
    private static final String OSBOT_DOWNLOAD_URL = "https://osbot.org/mvc/get";

    private static String latestLocalVersion;

    public static boolean isUpdateRequired() {
        Optional<String> latestLocalVersion = getLatestLocalVersion();

        if (!latestLocalVersion.isPresent()) {
            return true;
        }

        OSBotClient.latestLocalVersion = latestLocalVersion.get();

        Optional<String> latestRemoteVersion = getLatestRemoteVersion();

        if (!latestRemoteVersion.isPresent()) {
            System.out.println("Could not get latest OSBot version");
            return true;
        }

        return !latestLocalVersion.get().equals(latestRemoteVersion.get());
    }

    public static Optional<String> getLatestLocalVersion() {
        if (latestLocalVersion != null) {
            return Optional.of(latestLocalVersion);
        }

        File[] clients = new File(Settings.OSBOT_CLIENT_DIR).listFiles();
        if (clients == null) {
            return Optional.empty();
        }
        return Arrays.stream(clients).map(File::getName).max(String::compareTo);
    }

    public static boolean download() {
        try {
            URL url = new URL(OSBOT_DOWNLOAD_URL);
            Optional<String> filename = getLatestRemoteVersion();

            if (!filename.isPresent()) {
                System.out.println("Failed to get latest OSBot version");
                return false;
            }

            File outputFile = Paths.get(Settings.OSBOT_CLIENT_DIR, filename.get()).toFile();

            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            OSBotClient.latestLocalVersion = filename.get();

            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Optional<String> getLatestRemoteVersion() {
        try {
            URL url = new URL(OSBOT_DOWNLOAD_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            String contentDisposition = conn.getHeaderField("Content-Disposition");
            contentDisposition = contentDisposition.toLowerCase();
            Pattern clientPattern = Pattern.compile("osbot [\\d.]+\\.jar");
            Matcher matcher = clientPattern.matcher(contentDisposition);
            if (matcher.find()) {
                return Optional.of(matcher.group(0));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
}
