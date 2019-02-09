package gui.tabs;

import bot_parameters.script.Script;
import gui.dialogues.input_dialog.ScriptDialog;
import javafx.scene.control.TableColumn;
import javafx.scene.control.cell.PropertyValueFactory;

public class ScriptTab extends TableTab<Script> {

    public ScriptTab() {
        super("Scripts", "No scripts found.", new ScriptDialog());

        TableColumn<Script, String> scriptIdCol = new TableColumn<>("Script ID");
        scriptIdCol.setCellValueFactory(new PropertyValueFactory<>("scriptIdentifier"));

        TableColumn<Script, String> scriptParamCol = new TableColumn<>("Parameters");
        scriptParamCol.setCellValueFactory(new PropertyValueFactory<>("parameters"));

        getTableView().getColumns().addAll(scriptIdCol, scriptParamCol);
    }
}
