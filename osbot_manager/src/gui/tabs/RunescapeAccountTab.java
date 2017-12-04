package gui.tabs;

import bot_parameters.account.RunescapeAccount;
import gui.ToolbarButton;
import gui.dialogues.error_dialog.ExceptionDialog;
import gui.dialogues.input_dialog.RunescapeAccountDialog;
import javafx.geometry.Orientation;
import javafx.scene.control.Button;
import javafx.scene.control.Separator;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class RunescapeAccountTab extends TableTab<RunescapeAccount> {

    public RunescapeAccountTab() {
        super("Runescape Accounts", "No accounts found.", new RunescapeAccountDialog());

        TableColumn<RunescapeAccount, String> usernameCol = new TableColumn<>("Username");
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<RunescapeAccount, String> passwordCol = new TableColumn<>("Password");
        passwordCol.setCellFactory(param -> new PasswordFieldCell());
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));

        TableColumn<RunescapeAccount, Integer> pinCol = new TableColumn<>("Bank Pin");
        pinCol.setCellValueFactory(new PropertyValueFactory<>("pin"));

        getTableView().getColumns().addAll(usernameCol, passwordCol, pinCol);

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
                        new ExceptionDialog(new Exception("The account: " + line + " is missing values, skipping.")).show();
                        continue;
                    }

                    if (values.length == 2) {
                        getTableView().getItems().add(new RunescapeAccount(values[0], values[1], 1234));
                    } else {
                        getTableView().getItems().add(new RunescapeAccount(values[0], values[1], Integer.parseInt(values[2])));
                    }
                }
            }
        } catch (final Exception e) {
            e.printStackTrace();
            new ExceptionDialog(e).show();
        }
    }
}
