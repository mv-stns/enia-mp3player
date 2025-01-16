package com.mp3player.presentation.scenes;

import com.mp3player.presentation.components.Controls;
import com.mp3player.presentation.components.SongEntry;
import com.mp3player.presentation.components.SongInfo;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class PlaylistView extends BorderPane {

  SongInfo songInfo;
  Controls controls;
  VBox mainPane;
  ScrollPane songList;
  SongEntry songEntry;

  public PlaylistView() {
    AnchorPane mainPane = new AnchorPane();
    mainPane.setStyle(
        "-fx-background-image: url(\"/resources/images/pattern.jpg\");-fx-background-size:"
            + " contain;-fx-background-repeat: repeat;-fx-background-color: #141414;");

    
    songList = new ScrollPane();
    VBox songListContent = new VBox();
    
    songList.setContent(songListContent);
    songList.getStyleClass().add("song-list");
    songListContent.setSpacing(20);
    songList.setPrefSize(400, 200);
    songList.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    songList.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    mainPane.getChildren().addAll(songList);
    AnchorPane.setTopAnchor(songList, 0.0);
    AnchorPane.setBottomAnchor(songList, 0.0);
    AnchorPane.setLeftAnchor(songList, 0.0);
    AnchorPane.setRightAnchor(songList, 0.0);

    this.setStyle("-fx-background-radius: 10;");
    this.setCenter(mainPane);
  }

  public void setSongList(SongEntry... songs) {
    VBox songListContent = (VBox) songList.getContent();
    songListContent.getChildren().clear();
    for (SongEntry song : songs) {
        songListContent.getChildren().add(song);
    }
  }
}
