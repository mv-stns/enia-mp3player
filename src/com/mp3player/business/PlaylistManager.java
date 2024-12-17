package com.mp3player.business;

import com.mp3player.model.Track;
import java.io.File;
import java.util.Arrays;

public class PlaylistManager {

  private static final String SONG_DIRECTORY = "src/resources/audio";
  private Track[] tracks;

  public Track[] getTracks() {
    return tracks;
  }

  public PlaylistManager() {
    initSongFiles();
  }

  public void initSongFiles() {
    File folder = new File(SONG_DIRECTORY);
    String[] files = folder.list((dir, name) -> name.toLowerCase().endsWith(".mp3"));
    tracks = new Track[files.length];
    if (files == null || files.length == 0) {
      throw new RuntimeException("No MP3 files found in " + SONG_DIRECTORY);
    }
    for (String file : files) {
      tracks[Arrays.asList(files).indexOf(file)] = new Track(getSongPath(file));
    }
  }

  public String getSongPath(String fileName) {
    return (SONG_DIRECTORY + File.separator + fileName);
  }

  public String getTrackPath(int index) {
    return tracks[index].getSoundFilePath();
  }

  @Override
  public String toString() {
    if (tracks == null || tracks.length == 0) {
      return "No tracks available";
    }

    StringBuilder playlistString = new StringBuilder();
    System.out.println(String.format("%s\t%-45s%-50s%-30s\n", "Number", "Artist", "Title", "Album"));
    for (int i = 0; i < tracks.length; i++) {
      Track track = tracks[i];
      String truncatedString = track.getTitle().length() > 45 ? track.getTitle().substring(0, 45) + "..." : track.getTitle();
      playlistString.append(String.format("[%s]\t%-45s%-50s%-30s\n", i, track.getArtist(), truncatedString, track.getAlbum()));
    }
    return playlistString.toString().trim();
  }
}
