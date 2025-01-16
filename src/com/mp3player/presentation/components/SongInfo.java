package com.mp3player.presentation.components;

import com.mp3player.business.MP3Player;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import mp3player.scene.layout.ImageViewPane;

public class SongInfo extends VBox {

  MP3Player player;
  private VBox songInfo;
  public ImageView albumCover;
  private ImageViewPane albumCoverWrapper;
  public Text songTitle, songArtist, songAlbum;

  public SongInfo() {
    final int imageWidth = 100;
    songTitle = new Text("Title");
    songTitle.setFont(Font.loadFont("file:src/resources/fonts/SF-Pro-Rounded-Semibold.otf", 28));
    songTitle.getStyleClass().addAll("song-title");
    songTitle.setWrappingWidth(400);
    songArtist = new Text("Artist");
    songArtist.setFont(Font.loadFont("file:src/resources/fonts/SF-Pro-Rounded-Semibold.otf", 16));
    songArtist.setFill(Color.rgb(107, 107, 107));
    songArtist.getStyleClass().add("song-artist");
    songAlbum = new Text("Album");
    songAlbum.getStyleClass().add("song-album");
    songAlbum.setFill(Color.rgb(107, 107, 107));
    songAlbum.getStyleClass().add("song-artist");

    songInfo = new VBox();
    songInfo.setMaxWidth(400);
    songInfo.getStyleClass().add("song-info");

    albumCover = new ImageView();
    setImage();
    albumCover.setPickOnBounds(true);
    albumCover.setFitHeight(100);
    albumCoverWrapper = new ImageViewPane();
    albumCoverWrapper.setImageView(albumCover);
    albumCoverWrapper.getStyleClass().add("album-cover");
    albumCoverWrapper.setPrefSize(imageWidth, imageWidth);
    albumCoverWrapper.setMinSize(imageWidth, imageWidth);
    albumCoverWrapper.setMaxSize(imageWidth, imageWidth);
    Rectangle mask = new Rectangle(imageWidth, imageWidth);
    mask.setArcWidth(24);
    mask.setArcHeight(24);
    albumCoverWrapper.setClip(mask);

    albumCoverWrapper.setStyle("-fx-overflow: visible;");

    VBox songDetails = new VBox(10);
    songDetails.getChildren().addAll(albumCoverWrapper, songTitle, songArtist, songAlbum);
    songInfo.getChildren().addAll(songDetails);
    songDetails.setAlignment(Pos.BOTTOM_LEFT);
    this.setAlignment(Pos.CENTER);
    this.getChildren().addAll(songInfo);
  }

  private void setImage() {
    Image placeholder = new Image("file:src/resources/images/album_cover.png");
    albumCover.setImage(placeholder);
    albumCover.setPreserveRatio(true);
  }

  public void setImage(Image img) {
    albumCover.setImage(img);
    albumCover.setPreserveRatio(true);
    albumCover.setFitHeight(albumCoverWrapper.getHeight());
  }
}
