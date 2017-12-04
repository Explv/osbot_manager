package gui.tabs;

import bot_parameters.account.OSBotAccount;
import bot_parameters.bot.Bot;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;

import java.io.File;

public class BotSettingsTab extends Tab {

    private final Label osbotPath;
    private final TextField usernameField;
    private final PasswordField passwordField;

    private final Bot bot = new Bot();
    private final OSBotAccount osBotAccount = new OSBotAccount("", "");

    public BotSettingsTab() {
        setText("Bot Settings");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label osbotPathLabel = new Label("OSBot Path:");
        Label osbotUsernameLabel = new Label("OSBot Username:");
        Label osbotPasswordLabel = new Label("OSBot Password:");

        osbotPath = new Label("No path selected.");

        Button browsePathButton = new Button("Browse...");
        browsePathButton.setMnemonicParsing(false);
        browsePathButton.setOnAction(e -> browseOSBOTPath());

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        gridPane.add(osbotPathLabel, 0, 0);
        gridPane.add(browsePathButton, 1, 0);
        gridPane.add(osbotPath, 2, 0);

        gridPane.add(osbotUsernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1, 2, 1);

        gridPane.add(osbotPasswordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2, 2, 1);

        setContent(gridPane);
    }

    public void setOsbotPath(final String path) {
        osbotPath.setText(path);
    }

    public void setOsbotUsername(final String username) {
        usernameField.setText(username);
    }

    public void setOsbotPassword(final String password) {
        passwordField.setText(password);
    }

    public final Bot getBot() {
        bot.setOsbotPath(osbotPath.getText());
        return bot;
    }

    public final OSBotAccount getOsBotAccount() {
        osBotAccount.setUsername(usernameField.getText());
        osBotAccount.setPassword(passwordField.getText());
        return osBotAccount;
    }

    private void browseOSBOTPath() {
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extensionFilter = new FileChooser.ExtensionFilter("JAR files (*.jar)", "*.jar");
        fileChooser.getExtensionFilters().add(extensionFilter);
        File osbot = fileChooser.showOpenDialog(null);
        if (osbot != null) osbotPath.setText(osbot.getAbsolutePath());
    }
}
