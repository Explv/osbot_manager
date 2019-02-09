package file_manager;

import gui.dialogues.error_dialog.ExceptionDialog;
import javafx.stage.FileChooser;

import java.io.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public final class SettingsFileManager {

    public static List<Object> loadSettings() {

        final List<Object> objects = new ArrayList<>();
        try {
            final File file = getFileChooser().showOpenDialog(null);

            if (file == null) return objects;

            try (FileInputStream fileInputStream = new FileInputStream(file);
                 ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream)) {

                Object object;
                while ((object = objectInputStream.readObject()) != null) {
                    objects.add(object);
                }
            }
        } catch (final EOFException e) {
            System.out.println("Config file read successfully.");
        } catch (final Exception e) {
            e.printStackTrace();
            new ExceptionDialog(e).show();
        }
        return objects;
    }

    public static void saveSettings(final List<Serializable> objects) {
        try {

            final File file = getFileChooser().showSaveDialog(null);

            if(file == null) return;

            if (!file.exists() && !file.createNewFile()) {
                new ExceptionDialog(new Exception("Failed to save settings")).showAndWait();
                return;
            }

            try (FileOutputStream fileOutputStream = new FileOutputStream(file);
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream)) {
                for (final Serializable object : objects) {
                    objectOutputStream.writeObject(object);
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            new ExceptionDialog(e).show();
        }
    }

    private static FileChooser getFileChooser() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Explv's OSBot Manager");
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("Config files (*.config)", "*.config");
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setInitialDirectory(Paths.get(System.getProperty("user.home"), "ExplvOSBotManager", "Configurations").toFile());
        return fileChooser;
    }
}
