package file_manager;

import settings.Settings;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;

public class PropertiesFileManager {

    private static final String propertiesFileName = Paths.get(Settings.STORAGE_DIR, "osbotAccountProperties").toString();

    public static Optional<Properties> getOSBotProperties() {
        try {
            Properties osbotAccountProperties = new Properties();
            File propertiesFile = new File(propertiesFileName);
            if(!propertiesFile.exists()) return Optional.empty();
            FileInputStream propertiesInputStream = new FileInputStream(propertiesFile);
            osbotAccountProperties.load(propertiesInputStream);
            propertiesInputStream.close();
            return Optional.of(osbotAccountProperties);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public static void setOSBotProperties(final String username, final String password) {
        try {
            FileOutputStream out = new FileOutputStream(propertiesFileName);
            Properties osbotAccountProperties = new Properties();
            osbotAccountProperties.setProperty("username", username);
            osbotAccountProperties.setProperty("password", password);
            osbotAccountProperties.store(out, "");
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
