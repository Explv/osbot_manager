package gui.tabs;

import bot_parameters.account.OSBotAccount;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;

public class BotSettingsTab extends Tab {

    private final TextField usernameField;
    private final PasswordField passwordField;

    public BotSettingsTab() {
        setText("Bot Settings");

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));

        Label osbotUsernameLabel = new Label("OSBot Username:");
        Label osbotPasswordLabel = new Label("OSBot Password:");

        usernameField = new TextField();
        usernameField.setPromptText("Username");

        passwordField = new PasswordField();
        passwordField.setPromptText("Password");

        gridPane.add(osbotUsernameLabel, 0, 1);
        gridPane.add(usernameField, 1, 1, 2, 1);

        gridPane.add(osbotPasswordLabel, 0, 2);
        gridPane.add(passwordField, 1, 2, 2, 1);

        setContent(gridPane);

        usernameField.setOnKeyTyped(event -> {
            OSBotAccount.getInstance().setUsername(usernameField.getText());
        });

        passwordField.setOnKeyTyped(event -> {
            OSBotAccount.getInstance().setPassword(passwordField.getText());
        });
    }

    public void setOsbotUsername(final String username) {
        usernameField.setText(username);
        OSBotAccount.getInstance().setUsername(username);
    }

    public void setOsbotPassword(final String password) {
        passwordField.setText(password);
        OSBotAccount.getInstance().setPassword(password);
    }
}
