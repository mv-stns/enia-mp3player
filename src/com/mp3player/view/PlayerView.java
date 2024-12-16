package com.mp3player.view;

import com.mp3player.model.MP3Player;
import com.mp3player.view.components.Controls;
import com.mp3player.view.components.SongInfo;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PlayerView extends BorderPane {

    private SongInfo songInfo;
    private Controls controls;
    private MP3Player player;
    VBox mainPane;

    public PlayerView(MP3Player player) {
        this.player = player;
        controls = new Controls();
        mainPane = new VBox(12);
        songInfo = new SongInfo(player);
        this.setCenter(mainPane);
        mainPane.getChildren().addAll(songInfo, controls);
        mainPane.setStyle("-fx-background-radius: 10;");
        this.setStyle("-fx-background-radius: 10;");
    }
}
