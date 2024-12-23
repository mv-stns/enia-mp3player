package com.mp3player.business;

import com.mpatric.mp3agic.*;
import de.vaitschulis.utils.Formatting;
import java.io.ByteArrayInputStream;
import javafx.scene.image.Image;

public class Track {

  private String title;
  private String artist;
  private String album;
  private String soundFilePath;
  private ID3v2 dataTag;
  private Image cover;

  public Track(String filePath) {
    this.soundFilePath = filePath;
    try {
      Mp3File mp3file = new Mp3File(filePath);
      if (mp3file.hasId3v2Tag()) {
        dataTag = mp3file.getId3v2Tag();
        this.title =
            (dataTag.getTitle() == null)
                ? mp3file.getFilename().replace("src/resources/audio/", "")
                : dataTag.getTitle();
        this.artist = (dataTag.getArtist() == null) ? "Unknown Artist" : dataTag.getArtist();
        this.album = (dataTag.getAlbum() == null) ? "Unknown Album" : dataTag.getAlbum();
        this.cover = new Image(new ByteArrayInputStream(dataTag.getAlbumImage()));
      }
    } catch (Exception e) {
      System.out.println(
          Formatting.RED_BOLD
              + "Error reading ID3v2 tag from file: "
              + filePath
              + Formatting.RESET);
      // e.printStackTrace();
    }
  }

  // Getters and Setters
  public String getTitle() {
    return title;
  }

  public String getArtist() {
    return artist;
  }

  public String getAlbum() {
    return album;
  }

  public String getSoundFilePath() {
    return soundFilePath;
  }

  public Image getCover(){
    return cover;
  }
}
