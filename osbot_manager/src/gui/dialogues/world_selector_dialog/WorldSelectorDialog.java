package gui.dialogues.world_selector_dialog;

import bot_parameters.configuration.World;
import bot_parameters.configuration.WorldType;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.transformation.SortedList;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.util.List;
import java.util.stream.Collectors;

public class WorldSelectorDialog extends Dialog {

    private List<World> allWorlds = World.getWorlds();
    private ObservableList<World> availableWorlds =  FXCollections.observableArrayList(allWorlds);
    private ObservableList<World> selectedWorlds = FXCollections.observableArrayList();

    public WorldSelectorDialog() {
        setTitle("Explv's OSBot Manager");

        VBox content = new VBox(5);
        getDialogPane().setContent(content);

        HBox worldListsBox = new HBox(5);
        content.getChildren().add(worldListsBox);

        VBox worldSelectorBox = new VBox();
        worldListsBox.getChildren().add(worldSelectorBox);

        SortedList<World> sortedWorlds = new SortedList<>(availableWorlds);
        sortedWorlds.setComparator(World.getWorldComparator());
        ListView<World> worldListView = new ListView<>(sortedWorlds);
        worldListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        worldSelectorBox.getChildren().add(worldListView);

        VBox selectedWorldsBox = new VBox();
        worldListsBox.getChildren().add(selectedWorldsBox);

        SortedList<World> sortedSelectedWorlds = new SortedList<>(selectedWorlds);
        sortedSelectedWorlds.setComparator(World.getWorldComparator());
        ListView<World> selectedWorldsListView = new ListView<>(sortedSelectedWorlds);
        selectedWorldsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

        selectedWorldsListView.getSelectionModel().selectedItemProperty().addListener(e -> worldListView.getSelectionModel().clearSelection());
        worldListView.getSelectionModel().selectedItemProperty().addListener(e -> selectedWorldsListView.getSelectionModel().clearSelection());

        selectedWorldsBox.getChildren().add(selectedWorldsListView);

        HBox buttonsBox = new HBox(5);
        content.getChildren().add(buttonsBox);

        Button toggleButton = new Button("Toggle");
        toggleButton.setOnAction(event -> {
            List<World> worldSelectorSelection = worldListView.getSelectionModel().getSelectedItems();
            selectedWorlds.addAll(worldSelectorSelection);
            availableWorlds.removeAll(worldSelectorSelection);

            List<World> selectedWorldsSelection = selectedWorldsListView.getSelectionModel().getSelectedItems();
            availableWorlds.addAll(selectedWorldsSelection);
            selectedWorlds.removeAll(selectedWorldsSelection);
        });
        buttonsBox.getChildren().add(toggleButton);

        Button addAllF2PButton = new Button("Add all F2P");
        addAllF2PButton.setOnAction(event -> {
            List<World> f2pWorlds = availableWorlds.stream().filter(w -> w.getType() == WorldType.F2P).collect(Collectors.toList());
            availableWorlds.removeAll(f2pWorlds);
            selectedWorlds.addAll(f2pWorlds);
        });
        buttonsBox.getChildren().add(addAllF2PButton);

        Button addAllMemButton = new Button("Add all Members");
        addAllMemButton.setOnAction(event -> {
            List<World> membersWorlds = availableWorlds.stream().filter(w -> w.getType() == WorldType.MEMBERS).collect(Collectors.toList());
            availableWorlds.removeAll(membersWorlds);
            selectedWorlds.addAll(membersWorlds);
        });
        buttonsBox.getChildren().add(addAllMemButton);

        Button addAllButton = new Button("Add all");
        addAllButton.setOnAction(event -> {
            selectedWorlds.addAll(availableWorlds);
            availableWorlds.clear();
        });
        buttonsBox.getChildren().add(addAllButton);

        Button removeAllButton = new Button("Remove all");
        removeAllButton.setOnAction(event -> {
            availableWorlds.addAll(selectedWorlds);
            selectedWorlds.clear();
        });
        buttonsBox.getChildren().add(removeAllButton);

        getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        Node okButton = getDialogPane().lookupButton(ButtonType.OK);
        okButton.setDisable(true);

        selectedWorldsListView.getItems().addListener((ListChangeListener<World>) c -> {
            okButton.setDisable(selectedWorldsListView.getItems().isEmpty());
        });

        getDialogPane().getStylesheets().add("style/dialog.css");
        getDialogPane().getStyleClass().add("custom-dialog");
    }

    public void setSelectedWorlds(final ObservableList<World> selectedWorlds) {
        this.selectedWorlds.setAll(selectedWorlds);
        this.availableWorlds.setAll(allWorlds);
        this.availableWorlds.removeAll(selectedWorlds);
    }

    public void clearSelectedWorlds() {
        this.selectedWorlds.clear();
        this.availableWorlds.setAll(allWorlds);
    }

    public ObservableList<World> getSelectedWorlds() {
        return selectedWorlds;
    }
}
