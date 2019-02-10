package main;

import gui.ManagerPane;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public final class ExplvOSBotManager extends Application {

    public static void main(final String[] args) {
        launch(args);
    }

    public static final String getVersion() {
        String version = ExplvOSBotManager.class.getPackage().getImplementationVersion();
        return version != null ? version : "";
    }

    @Override
    public final void start(final Stage primaryStage) {
        if (Updater.isUpdateRequired()) {
            Updater.update();
        }

        primaryStage.setTitle("Explv's OSBot Manager " + getVersion());
        Scene scene = new Scene(new ManagerPane(), 600, 400);
        scene.getStylesheets().add("css/manager_pane.css");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}