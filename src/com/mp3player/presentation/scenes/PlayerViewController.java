package com.mp3player.presentation.scenes;

import com.mp3player.business.MP3Player;
import com.mp3player.presentation.Controller;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;

public class PlayerViewController extends Controller<PlayerView> {
    MP3Player player;
    Button skipButton;
    Button prevButton;
    Button playButton;
    Button pauseButton;
    Button shuffleButton;
    Button volumeButton;

    public PlayerViewController(MP3Player player) {
        this.player = player;
        root = new PlayerView();

        playButton = root.controls.playButton;
        pauseButton = root.controls.pauseButton;
        skipButton = root.controls.skipButton;
        prevButton = root.controls.prevButton;
        shuffleButton = root.controls.shuffleButton;
        volumeButton = root.controls.volumeButton;

        initialize();
    }

    private void initialize() {
        playButton.addEventHandler(ActionEvent.ACTION, event -> {
            player.play();
            // playButton.setVisible(false);
            // pauseButton.setVisible(true);
        });
        pauseButton.addEventHandler(ActionEvent.ACTION, event -> {
            player.pause();
            // playButton.setVisible(true);
            // pauseButton.setVisible(false);
        });
        skipButton.addEventHandler(ActionEvent.ACTION, event -> {
            player.next();
        });
        prevButton.addEventHandler(ActionEvent.ACTION, event -> {
            player.previous();
        });
    }

    
}
