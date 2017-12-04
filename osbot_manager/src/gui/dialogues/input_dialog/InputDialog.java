package gui.dialogues.input_dialog;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

public abstract class InputDialog<T> extends Dialog<T> {

    final VBox contentBox;
    final Node okButton;
    private T existingItem;

    InputDialog() {
        setTitle("Explv's OSBot Manager");
        contentBox = new VBox(10);
        contentBox.setPadding(new Insets(20, 150, 10, 10));
        getDialogPane().setContent(contentBox);
        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);
        setResultConverter(buttonType -> {
            if(buttonType != ButtonType.OK) return null;
            if(existingItem != null) return onEdit(existingItem);
            return onAdd();
        });
        getDialogPane().getStylesheets().add("style/dialog.css");
        getDialogPane().getStyleClass().add("custom-dialog");
    }

    public final void setExistingItem(final T existingItem) {
        this.existingItem = existingItem;
        setValues(existingItem);
    }

    protected abstract void setValues(final T existingItem);

    protected abstract T onAdd();

    protected abstract T onEdit(final T existingItem);
}
