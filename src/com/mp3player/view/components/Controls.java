package com.mp3player.view.components;

import com.mp3player.utils.AdditionalFuncs;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Slider;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Controls extends HBox implements Styleable {

    private HBox musicPlayerControls;

    public Controls() {
        loadCSS("controls.css");
        Button shuffleButton = AdditionalFuncs.createSVGButton(
                "M16.47 5.47a.75.75 0 0 0 0 1.06l.72.72h-3.813a1.75 1.75 0 0 0-1.575.987l-.21.434a.4.4"
                        + " 0 0 0 0 .35l.568 1.174a.2.2 0 0 0 .36 0l.632-1.304a.25.25 0 0 1"
                        + " .225-.141h3.812l-.72.72a.75.75 0 1 0 1.061 1.06l2-2a.75.75 0 0 0"
                        + " 0-1.06l-2-2a.75.75 0 0 0-1.06 0m-6.436 9.859a.4.4 0 0 0 0-.35l-.57-1.174a.2.2 0"
                        + " 0 0-.36 0l-.63 1.304a.25.25 0 0 1-.226.141H5a.75.75 0 0 0 0 1.5h3.248a1.75 1.75"
                        + " 0 0 0 1.575-.987z M16.47 18.53a.75.75 0 0 1 0-1.06l.72-.72h-3.813a1.75 1.75 0 0"
                        + " 1-1.575-.987L8.473 8.89a.25.25 0 0 0-.225-.141H5a.75.75 0 0 1 0-1.5h3.248c.671"
                        + " 0 1.283.383 1.575.987l3.329 6.872a.25.25 0 0 0 .225.141h3.812l-.72-.72a.75.75 0"
                        + " 1 1 1.061-1.06l2 2a.75.75 0 0 1 0 1.06l-2 2a.75.75 0 0 1-1.06 0");
        Button prevButton = AdditionalFuncs.createSVGButton(
                "M6.75 7a.75.75 0 0 0-1.5 0v10a.75.75 0 0 0 1.5 0zm3.102 5.66a.834.834 0 0 1 0-1.32a25"
                        + " 25 0 0 1 6.935-3.787l.466-.165a.944.944 0 0 1 1.243.772a29.8 29.8 0 0 1 0"
                        + " 7.68a.944.944 0 0 1-1.243.772l-.466-.165a25 25 0 0 1-6.935-3.788");
        Button playButton = AdditionalFuncs.createSVGButton(
                "M19.266 13.516a1.917 1.917 0 0 0 0-3.032A35.8 35.8 0 0 0 9.35"
                        + " 5.068l-.653-.232c-1.248-.443-2.567.401-2.736 1.69a42.5 42.5 0 0 0 0 10.948c.17"
                        + " 1.289 1.488 2.133 2.736 1.69l.653-.232a35.8 35.8 0 0 0 9.916-5.416");
        Button pauseButton = AdditionalFuncs.createSVGButton(
                "M17.276 5.47c.435.16.724.575.724 1.039V17.49c0 .464-.29.879-.724 1.039a3.7 3.7 0 0"
                        + " 1-2.552 0A1.11 1.11 0 0 1 14 17.491V6.51c0-.464.29-.879.724-1.04a3.7 3.7 0 0 1"
                        + " 2.552 0m-8 0c.435.16.724.575.724 1.039V17.49c0 .464-.29.879-.724 1.039a3.7 3.7"
                        + " 0 0 1-2.552 0A1.11 1.11 0 0 1 6 17.491V6.51c0-.464.29-.879.724-1.04a3.7 3.7 0 0"
                        + " 1 2.552 0");
        Button skipButton = AdditionalFuncs.createSVGButton(
                "M18.75 7a.75.75 0 0 0-1.5 0v10a.75.75 0 0 0 1.5 0zm-4.296 3.945c.69.534.69 1.576 0"
                        + " 2.11a25.5 25.5 0 0 1-7.073 3.863l-.466.166c-.87.308-1.79-.28-1.907-1.178a30.3"
                        + " 30.3 0 0 1 0-7.812c.118-.898 1.037-1.486 1.907-1.177l.466.165a25.5 25.5 0 0 1"
                        + " 7.073 3.863");
        Button volumeButton = AdditionalFuncs.createSVGButton(
                "M5.003 11.716c.038-1.843.057-2.764.678-3.552c.113-.144.28-.315.42-.431c.763-.636"
                        + " 1.771-.636 3.788-.636c.72 0 1.081 0"
                        + " 1.425-.092q.107-.03.211-.067c.336-.121.637-.33 1.238-.746c2.374-1.645"
                        + " 3.56-2.467 4.557-2.11c.191.069.376.168.541.29c.861.635.927 2.115 1.058"
                        + " 5.073C18.967 10.541 19 11.48 19 12s-.033 1.46-.081 2.555c-.131 2.958-.197"
                        + " 4.438-1.058 5.073a2.2 2.2 0 0"
                        + " 1-.54.29c-.997.357-2.184-.465-4.558-2.11c-.601-.416-.902-.625-1.238-.746a3 3 0"
                        + " 0 0-.211-.067c-.344-.092-.704-.092-1.425-.092c-2.017 0-3.025 0-3.789-.636a3 3 0"
                        + " 0 1-.419-.43c-.621-.79-.64-1.71-.678-3.552a14 14 0 0 1 0-.57");

        volumeButton.setOnAction(e -> {
            VolumeDialog volumeDialog = new VolumeDialog();
            // Create a new stage for the volume dialog
            Stage volumeStage = new Stage();
            volumeStage.initStyle(StageStyle.TRANSPARENT); // Make the stage transparent
            volumeStage.setScene(new Scene(volumeDialog, 300, 400)); // Set the scene with desired dimensions
            volumeStage.setAlwaysOnTop(true); // Make the stage always on top
            volumeButton.setDisable(true); // Disable the volume button
            volumeStage.show(); // Show the dialog
        });

        musicPlayerControls = new HBox(8);
        musicPlayerControls.getStyleClass().add("music-player-controls");
        musicPlayerControls.setPadding(new Insets(12));
        musicPlayerControls.setAlignment(Pos.CENTER);
        musicPlayerControls.setMinWidth(400);
        musicPlayerControls
                .getChildren()
                .addAll(shuffleButton, prevButton, pauseButton, playButton, skipButton, volumeButton);

        this.setId("controls");
        this.setAlignment(Pos.CENTER);
        this.getChildren().add(musicPlayerControls);
    }

    @Override
    public void loadCSS(String stylesheet) {
        this.getStylesheets().add("file:" + this.getClass().getResource(stylesheet).getPath());
    }

    class VolumeDialog extends Pane {

        public VolumeDialog() {
            // Make dialog transparent
            Pane dialogPane = new Pane();
            dialogPane.getStylesheets().add("file:src/com/mp3player/style.css");

            // Set background transparent for dialog pane
            dialogPane.setBackground(Background.fill(Color.web("#414141")));
            dialogPane.setStyle("-fx-background-color: #2A2E37; -fx-border-color: #363b41; -fx-effect: dropshadow( three-pass-box , rgba(0,0,0,1.6) , 5, 0.0 , 0 , 2 );");

            // Make scene transparent
            Scene scene = dialogPane.getScene();
            if (scene != null) {
                scene.setFill(null);
            }

            Slider volumeSlider = new Slider(0, 100, 20);
            volumeSlider.setMaxHeight(50);
            volumeSlider.setOrientation(Orientation.VERTICAL);
            volumeSlider.setBlockIncrement(25);
            volumeSlider.setSnapToTicks(true);
            volumeSlider
                    .valueProperty()
                    .addListener(
                            (ObservableValue<? extends Number> ov, Number oldVal, Number newVal) -> {
                                volumeSlider
                                        .lookup(".track")
                                        .setStyle(
                                                String.format(
                                                        "-fx-background-color: linear-gradient(to top, rgb(255, 94, 0) %d%%, "
                                                                + "#e6e6e6 %d%%);",
                                                        newVal.intValue(), newVal.intValue()));
                            });

            VBox mainContainer = new VBox();
            mainContainer.setStyle("-fx-background-color: black;".concat("-fx-background-radius: 12;"));
            dialogPane.setStyle("-fx-background-color: -media-control-bg;".concat("-fx-padding: 0;"));
            // Rounded Corners for root
            mainContainer.minWidth(450);
            mainContainer.setAlignment(Pos.CENTER);

            dialogPane.setOnMousePressed(
                    e -> {
                        // allow move around of the dialog when pressed inside the dialog
                        dialogPane.setOnMouseDragged(
                                e2 -> {
                                    dialogPane.getScene().getWindow().setX(e2.getScreenX() - e.getSceneX());
                                    dialogPane.getScene().getWindow().setY(e2.getScreenY() - e.getSceneY());
                                });
                    });

            // Create the first DropShadow effect
            DropShadow shadow1 = new DropShadow();
            shadow1.setColor(Color.rgb(0, 0, 0, 0.5));
            shadow1.setRadius(24);
            shadow1.setOffsetX(0);
            shadow1.setOffsetY(4);

            // Create the second DropShadow effect
            DropShadow shadow2 = new DropShadow();
            shadow2.setColor(Color.rgb(255, 255, 255, 0.5));
            shadow2.setRadius(0);
            shadow2.setOffsetX(0);
            shadow2.setOffsetY(-1);

            Blend blend = new Blend();
            blend.setMode(BlendMode.SRC_OVER);
            blend.setBottomInput(shadow1);
            blend.setTopInput(shadow2);
            dialogPane.setEffect(blend);

            mainContainer.getChildren().add(volumeSlider);
            dialogPane.getChildren().add(mainContainer);
        }
    }
}
