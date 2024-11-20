package com.mp3player.view;

import com.mp3player.view.components.Controls;
import com.mp3player.view.components.SongInfo;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

public class PlayerView extends BorderPane {

    private SongInfo songInfo;
    private Controls controls;
    VBox mainPane;

    public PlayerView() {
        controls = new Controls();
        mainPane = new VBox(12);
        songInfo = new SongInfo();
        this.setCenter(mainPane);
        mainPane.getChildren().addAll(songInfo, controls);
        mainPane.setStyle("-fx-background-radius: 10;");
        this.setStyle("-fx-background-radius: 10;");
    }
}
