package settings;

import java.io.File;
import java.nio.file.Paths;

public class Settings {
    public static final String STORAGE_DIR = Paths.get(System.getProperty("user.home"), "explv_osbot_manager").toString();
    public static final String CONFIG_DIR = Paths.get(STORAGE_DIR, "configurations").toString();
    public static final String LOGS_DIR = Paths.get(STORAGE_DIR, "logs").toString();
    public static final String OSBOT_CLIENT_DIR = Paths.get(STORAGE_DIR, "osbot_client").toString();
    public static final String TEMP_DIR = Paths.get(STORAGE_DIR, "temp").toString();

    static {
        makeDirs(STORAGE_DIR);
        makeDirs(CONFIG_DIR);
        makeDirs(LOGS_DIR);
        makeDirs(OSBOT_CLIENT_DIR);
        makeDirs(TEMP_DIR);

        File oldConfigDir = Paths.get(System.getProperty("user.home"), "ExplvOSBotManager", "Configurations").toFile();
        if (oldConfigDir.exists()) {
            File[] configFiles = oldConfigDir.listFiles();

            if (configFiles != null) {
                System.out.println("Moving old config files");

                for (File configFile : configFiles) {
                    configFile.renameTo(Paths.get(CONFIG_DIR, configFile.getName()).toFile());
                }
            }
        }
    }

    private static boolean makeDirs(final String dir) {
        File dirFile = new File(dir);
        if (dirFile.isDirectory()) {
            return true;
        }
        if (!dirFile.mkdirs()) {
            System.err.println("Failed to make dir: " + dir);
            return false;
        }
        return true;
    }
}
