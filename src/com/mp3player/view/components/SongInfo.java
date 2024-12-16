package com.mp3player.view.components;

import com.mp3player.model.MP3Player;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import mp3player.scene.layout.ImageViewPane;

public class SongInfo extends HBox {

    MP3Player player;
    private StackPane songInfo;
    private ImageViewPane albumCover;
    private Text songTitle;
    private Text songArtist;
    private Text songAlbum;

    public SongInfo(MP3Player player) {
        this.player = player;
        songTitle = new Text("Title");
        songArtist = new Text("Artist");
        songAlbum = new Text("Album");

        HBox songDetails = new HBox(10);
        songDetails.getChildren().addAll(songTitle, songArtist, songAlbum);
        // songDetails.getStyle

        songInfo = new StackPane();
        albumCover = new ImageViewPane();
        songInfo.getStyleClass().add("song-info");
        albumCover.getStyleClass().add("album-cover");
        albumCover.setStyle("-fx-background-image: url(\"file:src/resources/images/album_cover.png\");");
                        
        albumCover.setMinWidth(400);
        albumCover.setMinHeight(400);
        songInfo.getChildren().addAll(albumCover, songDetails);
        songDetails.setAlignment(Pos.BOTTOM_LEFT);
        songDetails.setPadding(new Insets(12));
        this.setAlignment(Pos.CENTER);
        this.getChildren().addAll(songInfo);
    }

}
