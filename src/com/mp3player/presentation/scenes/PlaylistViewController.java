package com.mp3player.presentation.scenes;

import static com.mp3player.utils.Constants.*;

import com.mp3player.business.MP3Player;
import com.mp3player.business.PlaylistManager;
import com.mp3player.business.Track;
import com.mp3player.presentation.Controller;
import com.mp3player.presentation.components.SongEntry;
import javafx.scene.control.Button;

public class PlaylistViewController extends Controller<PlaylistView> {
  MP3Player player;
  PlaylistManager songFileManager;
  Button skipButton, prevButton, playButton, playPauseButton, volumeButton, loopButton;

  public PlaylistViewController(MP3Player player) {
    this.player = player;
    this.songFileManager = player.getSongFileManager();
    root = new PlaylistView();

    initialize();
  }

  private void initialize() {
    debug("INITIALIZING PLAYLISTVIEW CONTROLLER").blue();

    Track[] tracks = songFileManager.getTracks();
    SongEntry[] songEntries = new SongEntry[tracks.length];

    for (int i = 0; i < tracks.length; i++) {
      Track track = tracks[i];
      SongEntry songEntry = new SongEntry(track.getTitle(), track.getAlbum());

      if (track.getCover() != null) {
        songEntry.setImage(track.getCover());
      }

      final int songIndex = i;
      songEntry.playButton.setOnAction(
          event -> {
            new Thread() {
              public void run() {
                player.play(songIndex);
              }
            }.start();
          });

      songEntries[i] = songEntry;
    }

    root.setSongList(songEntries);
  }
}
