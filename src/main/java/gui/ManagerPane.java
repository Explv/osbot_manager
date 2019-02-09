package gui;

import bot_parameters.account.OSBotAccount;
import bot_parameters.account.RunescapeAccount;
import bot_parameters.configuration.Configuration;
import bot_parameters.proxy.Proxy;
import bot_parameters.script.Script;
import file_manager.PropertiesFileManager;
import file_manager.SettingsFileManager;
import gui.tabs.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.TabPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ManagerPane extends BorderPane {

    public ManagerPane() {

        HBox topToolBar = new HBox(15);
        topToolBar.setPadding(new Insets(10, 10, 10, 10));

        Font titleFont = new Font("Arial", 22);

        Text titleText1 = new Text("Explv");
        titleText1.setFill(Color.web("#33b5e5"));
        titleText1.setFont(titleFont);

        Text titleText2 = new Text("'s OSBot Manager");
        titleText2.setFill(Color.WHITE);
        titleText2.setFont(titleFont);

        TextFlow title = new TextFlow(titleText1, titleText2);

        topToolBar.getChildren().add(title);

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        topToolBar.getChildren().add(spacer);

        Button saveButton = new ToolbarButton("Save", "save_icon.png", "save_icon_blue.png");
        saveButton.setContentDisplay(ContentDisplay.LEFT);
        topToolBar.getChildren().add(saveButton);

        Button loadButton = new ToolbarButton("Load", "open_icon.png", "open_icon_blue.png");
        loadButton.setContentDisplay(ContentDisplay.LEFT);
        topToolBar.getChildren().add(loadButton);

        setTop(topToolBar);

        BotSettingsTab botSettingsTab = new BotSettingsTab();

        TableTab<RunescapeAccount> runescapeAccountTab = new RunescapeAccountTab();
        TableTab<Script> scriptTab = new ScriptTab();
        TableTab<Proxy> proxyTab = new ProxyTab();
        TableTab<Configuration> runTab = new ConfigurationTab(runescapeAccountTab.getTableView().getItems(), scriptTab.getTableView().getItems(), proxyTab.getTableView().getItems(), botSettingsTab);

        TabPane tabPane = new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        tabPane.getTabs().addAll(runTab, scriptTab, runescapeAccountTab, proxyTab, botSettingsTab);
        setCenter(tabPane);

        saveButton.setOnAction(event -> {
            List<Serializable> objects = new ArrayList<>();
            objects.addAll(runescapeAccountTab.getTableView().getItems());
            objects.addAll(proxyTab.getTableView().getItems());
            objects.addAll(scriptTab.getTableView().getItems());
            objects.addAll(runTab.getTableView().getItems());
            SettingsFileManager.saveSettings(objects);
            PropertiesFileManager.setOSBotProperties(
                    OSBotAccount.getInstance().getUsername(),
                    OSBotAccount.getInstance().getPassword()
            );
        });

        loadButton.setOnAction(event -> {
            runescapeAccountTab.getTableView().getItems().clear();
            proxyTab.getTableView().getItems().clear();
            scriptTab.getTableView().getItems().clear();
            runTab.getTableView().getItems().clear();
            for (final Object object : SettingsFileManager.loadSettings()) {
                if (object instanceof RunescapeAccount) {
                    runescapeAccountTab.getTableView().getItems().add((RunescapeAccount) object);
                } else if (object instanceof Proxy) {
                    proxyTab.getTableView().getItems().add((Proxy) object);
                } else if (object instanceof Script) {
                    scriptTab.getTableView().getItems().add((Script) object);
                } else if (object instanceof Configuration) {
                    runTab.getTableView().getItems().add((Configuration) object);
                }
            }
        });

        PropertiesFileManager.getOSBotProperties().ifPresent(properties -> {
            if(properties.containsKey("username")) {
                botSettingsTab.setOsbotUsername(properties.getProperty("username"));
            }
            if(properties.containsKey("password")) {
                botSettingsTab.setOsbotPassword(properties.getProperty("password"));
            }
        });
    }
}
