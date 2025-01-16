package com.mp3player.business;

import static com.mp3player.utils.Constants.*;

import de.hsrm.mi.eibo.simpleplayer.*;
import de.hsrm.mi.prog.util.StaticScanner;
import de.vaitschulis.utils.Formatting;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;

/**
 * A class that implements an MP3 player with basic audio playback functionality. This player
 * supports operations like play, pause, resume, stop, volume control, and playlist management.
 *
 * @author Marcus Vaitschulis
 * @version 1.0
 */
public class MP3Player {

  private SimpleMinim minim;
  private SimpleAudioPlayer audioPlayer;
  private boolean muted;
  private boolean looping;
  private SimpleBooleanProperty pausedProperty;
  private PlaylistManager songFileManager;
  private float volume;
  private SimpleIntegerProperty currentTimeProperty;
  private SimpleObjectProperty<Track> currentTrackProperty;
  private SimpleFloatProperty currentVolumeProperty;
  private Object skipObject;
  private Thread timeUpdaterThread;
  private boolean timeUpdaterRunning = true;

  /**
   * Checks if the current track is paused.
   *
   * @return true if the player is in paused state, false otherwise
   */
  public synchronized boolean isPaused() {
    return pausedProperty.get();
  }

  public SimpleBooleanProperty getPausedProperty() {
    return pausedProperty;
  }

  public boolean isMuted() {
    return muted;
  }

  /**
   * Sets the pause state of the player.
   *
   * @param paused the pause state to set
   */
  public void setPaused(boolean paused) {
    this.pausedProperty.set(paused);
  }

  /**
   * Gets the currently playing track.
   *
   * @return Track object representing the current song, or null if no song is playing
   */
  public Track getCurrentSong() {
    return currentTrackProperty.get();
  }

  public SimpleIntegerProperty currentTimeProperty() {
    return currentTimeProperty;
  }

  /**
   * Sets the current track.
   *
   * @param newSong Track object to set as current song
   */
  public void setCurrentSong(Track newSong) {
    debug("CHANGING SONG").redBackground();
    currentTrackProperty.set(newSong);
    audioPlayer = minim.loadMP3File(newSong.getSoundFilePath());
  }

  /**
   * Gets the current track property.
   *
   * @return SimpleObjectProperty<Track> representing the current track
   */
  public SimpleObjectProperty<Track> currentTrackProperty() {
    return currentTrackProperty;
  }

  public SimpleFloatProperty currentVolumeProperty() {
    return currentVolumeProperty;
  }

  /**
   * Constructs a new MP3Player instance. Initializes the audio system, loads sound files, and sets
   * default values for volume and playback states.
   */
  public MP3Player() {
    skipObject = new Object();
    minim = new SimpleMinim();
    loadSoundFiles();
    muted = false;
    looping = false;
    volume = normalizeAudioPercentage(100);
    currentTimeProperty = new SimpleIntegerProperty();
    currentTrackProperty = new SimpleObjectProperty<>();
    currentVolumeProperty = new SimpleFloatProperty(50.0f);
    pausedProperty = new SimpleBooleanProperty(true);
    setCurrentSong(songFileManager.getTracks()[0]);
    startSongTimeUpdater();
    debug(String.format("PLAYING: %s\t PAUSED: %s\t", isSongPlaying(), isPaused())).redBackground();
  }

  private void startSongTimeUpdater() {
    if (timeUpdaterThread == null || !timeUpdaterThread.isAlive()) {
      timeUpdaterRunning = true;
      timeUpdaterThread =
          new Thread(
              () -> {
                while (timeUpdaterRunning) {
                  if (isAudioPlayerInitialized()) {
                    int currentPosition = audioPlayer.position();
                    int totalDuration = audioPlayer.length();

                    if (currentPosition >= totalDuration - 200) {
                      if (!looping) {
                        setPaused(true);
                        // currentTimeProperty.set(totalDuration);
                        try {
                          Thread.sleep(200);
                        } catch (InterruptedException e) {
                          e.printStackTrace();
                        }
                        new Thread() {
                          public void run() {
                            skip();
                          }
                        }.start();
                      } else if (looping) {
                        rewindAndPlay();
                      }
                    }
                    currentTimeProperty.set(currentPosition);

                    // debug(
                    //         String.format(
                    //             "C: %s\tL: %s\tISPLAYING?: %s",
                    //             currentTimeProperty.get(),
                    //             getCurrentSongDuration(),
                    //             audioPlayer.isPlaying()))
                    //     .red();

                    try {
                      Thread.sleep(100);
                    } catch (InterruptedException e) {
                      debug("INTERRUPTING");
                      Thread.currentThread().interrupt();
                      return;
                    }
                  }
                }
              });
      timeUpdaterThread.setDaemon(true);
      timeUpdaterThread.start();
    }
  }

  // Add a method to stop the thread
  public void stopTimeUpdater() {
    timeUpdaterRunning = false;
    if (timeUpdaterThread != null) {
      timeUpdaterThread.interrupt();
    }
  }

  // Add a method to check thread status
  public boolean isTimeUpdaterRunning() {
    return timeUpdaterThread != null && timeUpdaterThread.isAlive();
  }

  /**
   * Loads available sound files into the player's playlist.
   *
   * @throws NullPointerException if the playlist directory is not found or cannot be accessed
   */
  public synchronized void loadSoundFiles() throws NullPointerException {
    try {
      songFileManager = new PlaylistManager();
    } catch (NullPointerException e) {
      debug(Arrays.toString(e.getStackTrace()));
    }
  }

  /**
   * Gets the current volume level in decibels.
   *
   * @return float value representing the current volume gain in dB
   */
  public float getVolume() {
    return currentVolumeProperty.floatValue();
  }

  /**
   * Normalizes the volume percentage to a corresponding decibel value. Converts a percentage
   * (0-100) to decibels (-80 to 0).
   *
   * @param percentage volume level as percentage (0-100)
   * @return normalized volume in decibels
   */
  private float normalizeAudioPercentage(float percentage) {
    percentage = Math.max(0, Math.min(100, percentage));
    float minPercentage = 0.01f;
    float gainDB = 20 * (float) Math.log10(Math.max(percentage, minPercentage) / 100);
    return Math.max(gainDB, -80);
  }

  /** Increases the volume by 3dB. Maximum volume is 0dB. */
  public void increaseVolume() {
    float currentGainDB = audioPlayer.getGain();
    float newGainDB = Math.min(currentGainDB + 3, 0);
    audioPlayer.setGain(newGainDB);
    System.out.println(
        Formatting.BLUE_BOLD
            + "Volume increased to "
            + String.format("%.2f", newGainDB)
            + " dB"
            + Formatting.RESET);
  }

  /** Decreases the volume by 3dB. Minimum volume is -80dB. */
  public void decreaseVolume() {
    float currentGainDB = audioPlayer.getGain();
    float newGainDB = Math.max(currentGainDB - 3, -80);
    audioPlayer.setGain(newGainDB);
    System.out.println(
        Formatting.BLUE_BOLD
            + "Volume decreased to "
            + String.format("%.2f", newGainDB)
            + " dB"
            + Formatting.RESET);
  }

  /** Mutes the audio output by setting volume to minimum (-80dB). */
  public void mute() {
    setVolume(0);
    setMuted(true);
    System.out.println(Formatting.BLUE_BOLD + "Volume muted" + Formatting.RESET);
  }

  /**
   * Sets the volume to a specific percentage value.
   *
   * @param vol volume level as percentage (0-100)
   */
  public void setVolume(float vol) {
    // System.out.println(vol);
    this.volume = vol;
    currentVolumeProperty.set(vol);
    debug(Formatting.RED_BOLD + "CURRENT VOL IS:" + volume + Formatting.RESET);
    try {
      if (audioPlayer == null) {
        System.out.println(Formatting.RED_BOLD + "No song is playing." + Formatting.RESET);
        return;
      }
      float gainDB = normalizeAudioPercentage(vol);
      audioPlayer.setGain(gainDB);
      // System.out.println(
      //     Formatting.BLUE_BOLD
      //         + "Volume set to "
      //         + vol
      //         + "% ("
      //         + String.format("%.2f", gainDB)
      //         + " dB)"
      //         + Formatting.RESET);
      this.volume = gainDB;
    } catch (Exception e) {
      System.out.println(Formatting.RED_BOLD + "Invalid volume." + Formatting.RESET);
    }
    this.volume = vol;
  }

  /**
   * Pauses the currently playing track. Does nothing if no track is playing or if already paused.
   */
  public void pause() {
    try {
      if (!isSongPlaying() && isPaused()) {
        debug(Formatting.RED_BOLD + "No song is playing." + Formatting.RESET);
        return;
      }
      synchronized (skipObject) {
        audioPlayer.pause();
        setPaused(true);
      }
      stopTimeUpdater();

    } catch (Exception e) {

    }
  }

  /**
   * Resumes playback of a paused track. Does nothing if no track is paused or if already playing.
   */
  public void resume() {
    if (!isAudioPlayerInitialized()) {
      setPaused(false);
      debug("INTITIALLY PLAYING");
      play(0);
      return;
    } else if (isPaused()) {
      debug("RESUMING FROM PAUSED STATE");
      try {
        new Thread() {
          public void run() {
            audioPlayer.play();
          }
        }.start();
        setPaused(false);
        timeUpdaterRunning = true;
        startSongTimeUpdater();
        debug("Song resumed.").blue();
      } catch (Exception e) {
        debug("Error resuming the song.").red();
      }
    } else {
      debug("No Song is paused!").red();
    }
  }

  /** Stops the current playback and resets the player. */
  public void stop() {
    try {
      audioPlayer.pause();
      audioPlayer.rewind();
      audioPlayer = null;
      System.out.println("Song stopped.");
    } catch (Exception e) {
      System.out.println("No song is playing.");
    }
  }

  /**
   * Plays a specific track from the playlist.
   *
   * @param songNumber index of the song in the playlist
   * @throws NullPointerException if the song file cannot be found or loaded
   */
  public void play(int songNumber) {
    try {
      if (songFileManager.getTracks().length <= songNumber) {
        System.out.println("Invalid song number.");
        return;
      }

      // Stoppe aktuelle Wiedergabe
      if (audioPlayer != null) {
        audioPlayer.pause();
        audioPlayer = null;
      }
      setPaused(false);
      startSongTimeUpdater();
      debug(timeUpdaterRunning);

      String songPath = songFileManager.getTrackPath(songNumber);
      Track newTrack = songFileManager.getTracks()[songNumber];
      currentTrackProperty.set(newTrack);
      audioPlayer = minim.loadMP3File(songPath);
      audioPlayer.play();
      setVolume(getVolume());
    } catch (Exception e) {
      System.out.println("Error playing song: " + e.getMessage());
    }
  }

  public void autoPlay() {
    Track[] tracks = songFileManager.getTracks();
    int currentSong = -1;
    for (int i = 0; i < tracks.length; i++) {
      if (tracks[i] == currentTrackProperty.get()) {
        currentSong = i;
        break;
      }
    }
    if (currentSong >= 0 && currentSong < tracks.length - 1) {
      final int nextSong = currentSong + 1;
      new Thread() {
        public void run() {
          play(nextSong);
        }
      }.start();
    }
  }

  public void rewindAndPlay() {
    new Thread() {
      public void run() {
        audioPlayer.rewind();
        audioPlayer.play();
      }
    }.start();
  }

  /**
   * Prompts user to select a song from the playlist.
   *
   * @return selected song index, or -1 if selection is invalid
   */
  public int selectSong() {
    System.out.println(Formatting.BLUE_BOLD + "Enter the song number:" + Formatting.RESET);
    try {
      int song = StaticScanner.nextInt();
      if (song < 0 || song >= songFileManager.getTracks().length) {
        System.out.println(Formatting.RED_BOLD + "Invalid song number." + Formatting.RESET);
        return -1;
      }
      return song;
    } catch (Exception e) {
      System.out.println(
          Formatting.RED_BOLD + "Invalid input. Please enter a number." + Formatting.RESET);
      return -1;
    }
  }

  /**
   * Checks if a song is currently playing.
   *
   * @return true if a song is playing, false otherwise
   */
  public synchronized boolean isSongPlaying() {
    return audioPlayer != null && audioPlayer.isPlaying();
  }

  /**
   * Sets the muted state of the player.
   *
   * @param b true to mute, false to unmute
   */
  public void setMuted(boolean b) {
    muted = b;
  }

  /** Displays a list of all available songs in the playlist. */
  public void listSongs() {
    System.out.println(Formatting.BLUE_BACKGROUND_BRIGHT + "Available Songs:" + Formatting.RESET);
    System.out.println(songFileManager.toString());
  }

  /** Enables loop mode for the current track. Does nothing if no track is playing. */
  public void loop() {
    if (audioPlayer != null) {
      audioPlayer.loop();
      System.out.println(Formatting.BLUE_BOLD + "Song looped." + Formatting.RESET);
    } else {
      System.out.println(Formatting.RED_BOLD + "No song is playing." + Formatting.RESET);
    }
  }

  /**
   * Plays the next song in the playlist. Wraps to the first song if at the end.
   *
   * @throws IllegalStateException if no song is currently selected
   */
  public void skip() {
    if (currentTrackProperty.get() == null) {
      play(0);
      timeUpdaterRunning = true;
      startSongTimeUpdater();
      return;
    }
    if (audioPlayer != null) {
      pause();
      stopTimeUpdater();
    }
    int nextIndex;
    synchronized (skipObject) {
      // Berechne den Index des nächsten Tracks
      Track[] tracks = songFileManager.getTracks();
      int currentIndex = Arrays.asList(tracks).indexOf(currentTrackProperty.get());
      nextIndex = (currentIndex + 1) % tracks.length;

      // Dann den neuen Track setzen und abspielen
      Track nextTrack = tracks[nextIndex];
      setCurrentSong(nextTrack);
    }
    startSongTimeUpdater();
    new Thread() {
      public void run() {
        play(nextIndex);
      }
    }.start();
  }
  ;

  /**
   * Plays the previous song in the playlist or rewinds the Song, if a song is currently playing
   * longer than 2 seconds. Wraps to the last song if at the beginning.
   *
   * @throws IllegalStateException if no song is currently selected
   */
  public void skipBack() {
    if (currentTrackProperty.get() == null) {
      play(0);
      startSongTimeUpdater();
      return;
    }
    if (currentTimeProperty.get() >= 2000) {
      setPaused(false);
      moveTo(0);
      startSongTimeUpdater();
      return;
    }
    if (audioPlayer != null) {
      audioPlayer.pause();
      stopTimeUpdater();
    }
    int prevIndex;
    synchronized (skipObject) {
      Track[] tracks = songFileManager.getTracks();
      int currentIndex = Arrays.asList(tracks).indexOf(currentTrackProperty.get());
      prevIndex = (currentIndex - 1 + tracks.length) % tracks.length;
      Track prevTrack = tracks[prevIndex];
      setCurrentSong(prevTrack);
    }
    startSongTimeUpdater();
    new Thread() {
      public void run() {
        play(prevIndex);
      }
    }.start();
  }

  public synchronized int getCurrentSongDuration() {
    return audioPlayer.length();
  }

  public void setLooping(boolean looping) {
    this.looping = looping;
  }

  public boolean isLooping() {
    return this.looping;
  }

  public void moveTo(int t) {
    new Thread() {
      public void run() {
        if (isAudioPlayerInitialized()) {
          if (t >= 0 && t <= getCurrentSongDuration()) {
            setPaused(false);
            new Thread() {
              public void run() {
                audioPlayer.cue(t);
              }
            }.start();
            try {
              Thread.sleep(400);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
            new Thread() {
              public void run() {
                audioPlayer.play();
              }
            }.start();
          } else {
            System.out.println("Invalid time: " + t);
          }
        } else {
          Platform.runLater(
              () -> {
                System.out.println("Audio player is not initialized.");
              });
        }
      }
    }.start();
  }

  public boolean isAudioPlayerInitialized() {
    return audioPlayer != null;
  }

  public PlaylistManager getSongFileManager() {
    return songFileManager;
  }
}
