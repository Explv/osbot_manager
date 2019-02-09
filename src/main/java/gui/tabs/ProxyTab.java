package gui.tabs;

import bot_parameters.proxy.Proxy;
import bot_parameters.proxy.SecuredProxy;
import gui.ToolbarButton;
import gui.dialogues.error_dialog.ExceptionDialog;
import gui.dialogues.input_dialog.ProxyDialog;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ProxyTab extends TableTab<Proxy> {

    public ProxyTab() {
        super("Proxies", "No proxies found.", new ProxyDialog());

        TableColumn<Proxy, String> ipCol = new TableColumn<>("IP Address");
        ipCol.setCellValueFactory(new PropertyValueFactory<>("ipAddress"));

        TableColumn<Proxy, Integer> portCol = new TableColumn<>("Port");
        portCol.setCellValueFactory(new PropertyValueFactory<>("port"));

        TableColumn<Proxy, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<Proxy, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellFactory(param -> new PasswordFieldCell());
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        getTableView().getColumns().addAll(ipCol, portCol, usernameCol, passwordCol);

        toolBar.getChildren().add(new Separator(Orientation.VERTICAL));

        Button importFromFileButton = new ToolbarButton("Import", "import_icon.png", "import_icon_blue.png");
        importFromFileButton.setOnAction(e -> importFromFile());
        toolBar.getChildren().add(importFromFileButton);
    }

    private void importFromFile() {
        try {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Explv's OSBot Manager");
            final File file = fileChooser.showOpenDialog(null);

            if (file == null) return;

            try (FileReader fileReader = new FileReader(file);
                 BufferedReader br = new BufferedReader(fileReader)) {

                String line;
                while((line = br.readLine()) != null) {

                    String[] values = line.trim().split(":");

                    if (values.length < 2) {
                        new ExceptionDialog(new Exception("The proxy: " + line + " is missing values, skipping.")).show();
                        continue;
                    }

                    if (values.length == 2) {
                        getTableView().getItems().add(new Proxy(values[0], Integer.parseInt(values[1])));
                    } else {
                        getTableView().getItems().add(new SecuredProxy(values[0], Integer.parseInt(values[1]), values[2], values[3]));
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            new ExceptionDialog(e).show();
        }
    }
}
