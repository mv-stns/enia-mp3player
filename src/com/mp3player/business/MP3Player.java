package com.mp3player.business;

import static com.mp3player.utils.Constants.*;

import de.hsrm.mi.eibo.simpleplayer.*;
import de.hsrm.mi.prog.util.StaticScanner;
import de.vaitschulis.utils.Formatting;
import java.util.Arrays;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.concurrent.Task;

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
  private Track currentSong;
  private boolean paused;
  private PlaylistManager songFileManager;
  private float volume;
  private SimpleIntegerProperty currentTimeProperty;
  private SimpleObjectProperty<Track> currentTrackProperty;
  private SimpleFloatProperty currentVolumeProperty;
  public Thread songTimeThread;

  /**
   * Checks if the current track is paused.
   *
   * @return true if the player is in paused state, false otherwise
   */
  public boolean isPaused() {
    return paused;
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
    this.paused = paused;
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
   * @param currentSong Track object to set as current song
   */
  public void setCurrentSong(Track currentSong) {
    currentTrackProperty.set(currentSong);
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
    minim = new SimpleMinim();
    loadSoundFiles();
    muted = false;
    looping = false;
    volume = normalizeAudioPercentage(100);
    currentTimeProperty = new SimpleIntegerProperty();
    currentTrackProperty = new SimpleObjectProperty<>();
    currentVolumeProperty = new SimpleFloatProperty(50.0f);
    setCurrentSong(songFileManager.getTracks()[0]);
    startSongTimeUpdater();
  }

  private void startSongTimeUpdater() {
    songTimeThread =
        new Thread(
            () -> {
              try {
                while (true) {
                  if (isAudioPlayerInitialized() || isSongPlaying()) {
                    long currentPosition = audioPlayer.position();
                    currentTimeProperty.set((int) (currentPosition / 1000));
                    if (currentPosition >= audioPlayer.length() && looping) {
                      rewindAndPlay();
                    } else if (currentPosition >= audioPlayer.length()) {
                      next();
                    }
                  }
                  Thread.sleep(100);
                }
              } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Reset interrupt status
              }
            });

    songTimeThread.setDaemon(true);
    songTimeThread.start();
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
    System.out.println(vol);
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
      System.out.println(
          Formatting.BLUE_BOLD
              + "Volume set to "
              + vol
              + "% ("
              + String.format("%.2f", gainDB)
              + " dB)"
              + Formatting.RESET);
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
      audioPlayer.pause();
      setPaused(true);
      debug(Formatting.BLUE_BOLD + "Song paused." + Formatting.RESET);
    } catch (Exception e) {
      debug(Formatting.RED_BOLD + "Error pausing the song." + Formatting.RESET);
    }
  }

  /**
   * Resumes playback of a paused track. Does nothing if no track is paused or if already playing.
   */
  public synchronized void resume() {
    if (!isAudioPlayerInitialized()) {
      debug("INTITIALLY PLAYING");
      play();
      return;
    } else if (isPaused()) {
      debug("RESUMING FROM PAUSED STATE");
      try {
        setPaused(false);
        audioPlayer.play();
        System.out.println(Formatting.BLUE_BOLD + "Song resumed." + Formatting.RESET);
      } catch (Exception e) {
        System.out.println(Formatting.RED_BOLD + "Error resuming the song." + Formatting.RESET);
      }
    } else {
      System.out.println(Formatting.RED_BOLD + "No song is paused." + Formatting.RESET);
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

  /** Starts playback of a selected track. Prompts user to select a track if none is specified. */
  public void play() {
    Task<Void> playTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            int selectedSong;
            if (audioPlayer == null || (!isSongPlaying() && !isPaused()) || isPaused()) {
              selectedSong = 0;
              if (selectedSong != -1) {
                play(selectedSong);
                setPaused(false);
              }
            } else {
              System.out.println(
                  Formatting.RED_BOLD + "Song is already playing." + Formatting.RESET);
            }
            // setVolume(getVolume());
            return null;
          }
        };
    new Thread(playTask).start();
  }

  /**
   * Plays a specific track from the playlist.
   *
   * @param songNumber index of the song in the playlist
   * @throws NullPointerException if the song file cannot be found or loaded
   */
  public void play(int songNumber) throws NullPointerException {
    if (songFileManager.getTracks()[songNumber] == currentSong) {
      System.out.println("Song already playing!");
      return;
    }
    try {
      if (songFileManager.getTracks().length < songNumber) {
        System.out.println(Formatting.RED_BOLD + "Invalid song number." + Formatting.RESET);
        return;
      }
      if (isAudioPlayerInitialized()) {
        Task<Void> pauseTask =
            new Task<Void>() {
              @Override
              protected Void call() throws Exception {
                audioPlayer.pause();
                audioPlayer = null;
                return null;
              }
            };
        new Thread(pauseTask).start();
      }
      String songPath = songFileManager.getTrackPath(songNumber);
      audioPlayer = minim.loadMP3File(songPath);
      audioPlayer.play();
      setVolume(getVolume());
      setPaused(false);
      setCurrentSong(songFileManager.getTracks()[songNumber]);
    } catch (NullPointerException e) {
      System.out.println("Song not found!");
    }
  }

  private void rewindAndPlay() {
    audioPlayer.rewind();
    audioPlayer.play();
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
  public boolean isSongPlaying() {
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
  public void next() {
    setPaused(false);
    if (currentTrackProperty.get() == null) {
      play(0);
      return;
    }
    int currentIndex =
        Arrays.asList(songFileManager.getTracks()).indexOf(currentTrackProperty.get());
    play((currentIndex + 1) % songFileManager.getTracks().length);
    songTimeThread.start();
  }

  /**
   * Plays the previous song in the playlist or rewinds the Song, if a song is currently playing
   * longer than 2 seconds. Wraps to the last song if at the beginning.
   *
   * @throws IllegalStateException if no song is currently selected
   */
  public void previous() {
    if (currentTrackProperty.get() == null) {
      play(0);
      return;
    }
    if (currentTimeProperty.get() >= 2) {
      setPaused(false);
      moveTo(0);
      return;
    }
    setPaused(false);
    int currentIndex =
        Arrays.asList(songFileManager.getTracks()).indexOf(currentTrackProperty.get());
    play(
        (currentIndex - 1 + songFileManager.getTracks().length)
            % songFileManager.getTracks().length);
    return;
  }

  public int getCurrentSongDuration() {
    if (audioPlayer != null) {
      return (int) (audioPlayer.length() / 1000);
    }
    return 0;
  }

  public void setLooping(boolean looping) {
    this.looping = looping;
  }

  public boolean isLooping() {
    return looping;
  }

  public synchronized void moveTo(int t) {
    Task<Void> moveTask =
        new Task<Void>() {
          @Override
          protected Void call() throws Exception {
            if (isAudioPlayerInitialized()) {
              if (t >= 0 && t <= getCurrentSongDuration()) {
                audioPlayer.cue(t * 1000);
                System.out.println("Successfully moved to: " + t + " seconds");
                audioPlayer.play();
              } else {
                System.out.println("Invalid time: " + t);
              }
            } else {
              System.out.println("Audio player is not initialized.");
            }
            return null;
          }
        };
    new Thread(moveTask).start();
  }

  public boolean isAudioPlayerInitialized() {
    return audioPlayer != null;
  }
}
