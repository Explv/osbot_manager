package gui.dialogues.error_dialog;

import javafx.scene.control.Alert;

public final class ExceptionDialog extends Alert {

    public ExceptionDialog(final Exception exception) {
        super(AlertType.ERROR);
        setTitle("Explv's OSBot Manager");
        setHeaderText("Error!");
        setContentText(exception.getMessage());
    }
}
