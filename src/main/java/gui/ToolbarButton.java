package gui;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class ToolbarButton extends Button {

    private final ImageView imageView, hoverImageView;

    public ToolbarButton(final String text, final String imageName, final String hoverImageName) {
        super(text);
        imageView = new ImageView(new Image(getClass().getResourceAsStream("/images/" + imageName)));
        hoverImageView = new ImageView(new Image(getClass().getResourceAsStream("/images/" + hoverImageName)));
        setGraphic(imageView);
        setContentDisplay(ContentDisplay.TOP);
        setMnemonicParsing(false);
        hoverProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue) setGraphic(hoverImageView);
            else setGraphic(imageView);
        });
    }
}
