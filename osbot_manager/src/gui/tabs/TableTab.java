package gui.tabs;

import bot_parameters.interfaces.Copyable;
import gui.ToolbarButton;
import gui.dialogues.input_dialog.InputDialog;
import javafx.collections.ListChangeListener;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.*;

public class TableTab<T extends Copyable<T>> extends Tab {

    private final TableView<T> tableView;
    final HBox toolBar;
    final ContextMenu contextMenu;
    private final InputDialog<T> inputDialog;
    private T copiedItem;

    TableTab(final String text, final String placeholder, final InputDialog<T> inputDialog) {

        this.inputDialog = inputDialog;

        setText(text);

        BorderPane borderPane = new BorderPane();

        tableView = new TableView<>();
        tableView.setPlaceholder(new Label(placeholder));
        tableView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        tableView.setOnKeyPressed(event -> {
            if(event.getCode() == KeyCode.DELETE) {
                tableView.getItems().remove(tableView.getSelectionModel().getSelectedIndex());
            }
        });
        tableView.setOnMouseClicked(event -> {
            if(event.getClickCount() == 2) {
                onEdit();
            }
        });

        tableView.widthProperty().addListener((source, oldWidth, newWidth) -> {
            Pane header = (Pane) tableView.lookup("TableHeaderRow");
            if (getTableView().getItems().isEmpty()) {
                header.setVisible(false);
            }
        });

        borderPane.setCenter(tableView);

        toolBar = new HBox(15);
        toolBar.setPadding(new Insets(10, 10, 10, 10));

        Pane spacer = new Pane();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        toolBar.getChildren().add(spacer);

        Button addButton = new ToolbarButton("Add", "add_icon.png", "add_icon_blue.png");
        addButton.setOnAction(e -> onAdd());

        Button editButton = new ToolbarButton("Edit", "edit_icon.png", "edit_icon_blue.png");
        editButton.setOnAction(e -> onEdit());

        Button removeButton = new ToolbarButton("Remove", "delete_icon.png", "delete_icon_blue.png");
        removeButton.setOnAction(e -> onRemove());

        Button copyButton = new ToolbarButton("Copy", "copy_icon.png", "copy_icon_blue.png");
        copyButton.setOnAction(e -> onCopy());

        Button pasteButton = new ToolbarButton("Paste", "paste_icon.png", "paste_icon_blue.png");
        pasteButton.setOnAction(e -> onPaste());

        toolBar.getChildren().addAll(addButton, editButton, removeButton, copyButton, pasteButton);
        borderPane.setBottom(toolBar);

        AnchorPane.setBottomAnchor(borderPane, 0.0);
        AnchorPane.setRightAnchor(borderPane, 0.0);
        AnchorPane.setLeftAnchor(borderPane, 0.0);
        AnchorPane.setTopAnchor(borderPane, 0.0);
        AnchorPane anchorPane = new AnchorPane(borderPane);

        setContent(anchorPane);

        getTableView().getItems().addListener((ListChangeListener<T>) c -> {
            if (getTableView().getItems().isEmpty()) {
                getTableView().lookup("TableHeaderRow").setVisible(false);
            } else {
                getTableView().lookup("TableHeaderRow").setVisible(true);
            }
        });

        contextMenu = new ContextMenu();

        MenuItem addOption = new MenuItem("Add");
        addOption.setOnAction(e -> onAdd());
        contextMenu.getItems().add(addOption);

        MenuItem editOption = new MenuItem("Edit");
        editOption.setOnAction(e -> onEdit());
        contextMenu.getItems().add(editOption);

        MenuItem copyOption = new MenuItem("Copy");
        copyOption.setOnAction(event -> onCopy());
        contextMenu.getItems().add(copyOption);

        MenuItem pasteOption = new MenuItem("Paste");
        pasteOption.setOnAction(e -> onPaste());
        contextMenu.getItems().add(pasteOption);

        MenuItem removeOption = new MenuItem("Remove");
        removeOption.setOnAction(e -> onRemove());
        contextMenu.getItems().add(removeOption);

        getTableView().contextMenuProperty().set(contextMenu);

        getTableView().setOnKeyPressed(event -> {
            if (event.isControlDown()) {
                if (event.getCode() == KeyCode.C) {
                    onCopy();
                } else if (event.getCode() == KeyCode.V) {
                    onPaste();
                }
            } else if (event.getCode() == KeyCode.DELETE) {
                onRemove();
            }
        });
    }

    public final TableView<T> getTableView() {
        return tableView;
    }

    private void onAdd() {
        inputDialog.setExistingItem(null);
        inputDialog.showAndWait().ifPresent(item -> tableView.getItems().add(item));
    }

    private void onEdit() {
        final int selectedIndex = tableView.getSelectionModel().getSelectedIndex();
        if(selectedIndex > -1) {
            inputDialog.setExistingItem(tableView.getSelectionModel().getSelectedItem());
            inputDialog.showAndWait().ifPresent(editedItem -> {
                tableView.getItems().set(selectedIndex, editedItem);
                tableView.refresh();
            });
        }
    }

    private void onCopy() {
        copiedItem = getTableView().getSelectionModel().getSelectedItem();
    }

    private void onPaste() {
        if (copiedItem != null) {
            getTableView().getItems().add(copiedItem.createCopy());
        }
    }

    private void onRemove() {
        tableView.getItems().removeAll(tableView.getSelectionModel().getSelectedItems());
    }

    final class PasswordFieldCell extends TableCell<T, String> {

        @Override
        protected void updateItem(final String item, final boolean empty) {
            super.updateItem(item, empty);
            if(item != null && !item.isEmpty()){
                setText(new String(new char[item.length()]).replace("\0", "*"));
            } else{
                setText(null);
            }
        }
    }
}
