package com.mp3player.model;

import de.hsrm.mi.eibo.simpleplayer.*;
import de.hsrm.mi.prog.util.StaticScanner;
import de.vaitschulis.utils.Formatting;
import java.lang.Math;

/**
 * A class that implements an MP3 player with basic audio playback functionality.
 * This player supports operations like play, pause, resume, stop, volume control,
 * and playlist management.
 *
 * @author Marcus Vaitschulis
 * @version 1.0
 */
public class MP3Player {

  private SimpleMinim minim;
  private SimpleAudioPlayer audioPlayer;

  @SuppressWarnings("unused")
  private boolean muted;

  @SuppressWarnings("unused")
  private boolean looping;

  private Track currentSong;
  private boolean paused;
  private PlaylistManager songFileManager;
  private float volume;

  /**
   * Checks if the current track is paused.
   *
   * @return true if the player is in paused state, false otherwise
   */
  public boolean isPaused() {
    return paused;
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
    return currentSong;
  }

  /**
   * Sets the current track.
   *
   * @param currentSong Track object to set as current song
   */
  public void setCurrentSong(Track currentSong) {
    this.currentSong = currentSong;
  }

  /**
   * Constructs a new MP3Player instance.
   * Initializes the audio system, loads sound files, and sets default values for
   * volume and playback states.
   */
  public MP3Player() {
    minim = new SimpleMinim(true);
    loadSoundFiles();
    this.muted = false;
    this.looping = false;
    this.volume = normalizeAudioPercentage(100);
  }

  /**
   * Loads available sound files into the player's playlist.
   *
   * @throws NullPointerException if the playlist directory is not found or cannot be accessed
   */
  public void loadSoundFiles() throws NullPointerException {
    try {
      songFileManager = new PlaylistManager();
    } catch (NullPointerException e) {
      System.out.println(e.getStackTrace());
    }
  }

  /**
   * Gets the current volume level in decibels.
   *
   * @return float value representing the current volume gain in dB
   */
  public float getVolume() {
    return audioPlayer.getGain();
  }

  /**
   * Normalizes the volume percentage to a corresponding decibel value.
   * Converts a percentage (0-100) to decibels (-80 to 0).
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

  /**
   * Increases the volume by 3dB.
   * Maximum volume is 0dB.
   */
  public void increaseVolume() {
    float currentGainDB = audioPlayer.getGain();
    float newGainDB = Math.min(currentGainDB + 3, 0);
    audioPlayer.setGain(newGainDB);
    System.out.println(Formatting.BLUE_BOLD + "Volume increased to " + String.format("%.2f", newGainDB) + " dB" + Formatting.RESET);
  }

  /**
   * Decreases the volume by 3dB.
   * Minimum volume is -80dB.
   */
  public void decreaseVolume() {
    float currentGainDB = audioPlayer.getGain();
    float newGainDB = Math.max(currentGainDB - 3, -80);
    audioPlayer.setGain(newGainDB);
    System.out.println(Formatting.BLUE_BOLD + "Volume decreased to " + String.format("%.2f", newGainDB) + " dB" + Formatting.RESET);
  }

  /**
   * Mutes the audio output by setting volume to minimum (-80dB).
   */
  public void mute() {
    audioPlayer.setGain(-80);
    setMuted(true);
    System.out.println(Formatting.BLUE_BOLD + "Volume muted" + Formatting.RESET);
  }

  /**
   * Sets the volume to a specific percentage value.
   *
   * @param vol volume level as percentage (0-100)
   */
  public void volume(float vol) {
    this.volume = vol;
    try {
      if (audioPlayer == null) {
        System.out.println(Formatting.RED_BOLD + "No song is playing." + Formatting.RESET);
        return;
      }
      float gainDB = normalizeAudioPercentage(vol);
      audioPlayer.setGain(gainDB);
      System.out.println(Formatting.BLUE_BOLD + "Volume set to " + vol + "% (" + String.format("%.2f", gainDB) + " dB)" + Formatting.RESET);
      this.volume = gainDB;
    } catch (Exception e) {
      System.out.println(Formatting.RED_BOLD + "Invalid volume." + Formatting.RESET);
    }
  }

  /**
   * Pauses the currently playing track.
   * Does nothing if no track is playing or if already paused.
   */
  public void pause() {
    if (!isSongPlaying() && !isPaused()) {
      System.out.println(Formatting.RED_BOLD + "No song is playing." + Formatting.RESET);
    } else if (isPaused()) {
      System.out.println(Formatting.RED_BOLD + "Song is already paused." + Formatting.RESET);
    } else {
      try {
        audioPlayer.pause();
        setPaused(true);
        System.out.println(Formatting.BLUE_BOLD + "Song paused." + Formatting.RESET);
      } catch (Exception e) {
        System.out.println(Formatting.RED_BOLD + "Error pausing the song." + Formatting.RESET);
      }
    }
  }

  /**
   * Resumes playback of a paused track.
   * Does nothing if no track is paused or if already playing.
   */
  public void resume() {
    if (isSongPlaying()) {
      System.out.println(Formatting.RED_BOLD + "Song is already playing." + Formatting.RESET);
    } else if (isPaused()) {
      try {
        audioPlayer.play();
        setPaused(false);
        System.out.println(Formatting.BLUE_BOLD + "Song resumed." + Formatting.RESET);
      } catch (Exception e) {
        System.out.println(Formatting.RED_BOLD + "Error resuming the song." + Formatting.RESET);
      }
    } else {
      System.out.println(Formatting.RED_BOLD + "No song is paused." + Formatting.RESET);
    }
  }

  /**
   * Stops the current playback and resets the player.
   */
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
   * Starts playback of a selected track.
   * Prompts user to select a track if none is specified.
   */
  public void play() {
    int selectedSong;
    if (audioPlayer == null || (!isSongPlaying() && !isPaused()) || isPaused()) {
      selectedSong = selectSong();
      if (selectedSong != -1) {
        this.play(selectedSong);
      }
    } else if (isSongPlaying()) {
      selectedSong = selectSong();
      this.stop();
      if (selectedSong != -1) {
        this.play(selectedSong);
      }
    } else {
      System.out.println(Formatting.RED_BOLD + "Song is already playing." + Formatting.RESET);
    }
    volume(getVolume());
  }

  /**
   * Plays a specific track from the playlist.
   *
   * @param songNumber index of the song in the playlist
   * @throws NullPointerException if the song file cannot be found or loaded
   */
  public void play(int songNumber) throws NullPointerException {
    try {
      if (songFileManager.getTracks().length < songNumber) {
        System.out.println(Formatting.RED_BOLD + "Invalid song number." + Formatting.RESET);
        return;
      }
      if (audioPlayer != null) {
        audioPlayer.pause();
        audioPlayer = null;
      }
      String songPath = songFileManager.getTrackPath(songNumber);
      audioPlayer = minim.loadMP3File(songPath);
      audioPlayer.play();
      audioPlayer.setGain(volume);
      setPaused(false);
      this.setCurrentSong(songFileManager.getTracks()[songNumber]);
    } catch (NullPointerException e) {
      System.out.println("Song not found!");
    }
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
      System.out.println(Formatting.RED_BOLD + "Invalid input. Please enter a number." + Formatting.RESET);
      return -1;
    }
  }

  /**
   * Checks if a song is currently playing.
   *
   * @return true if a song is playing, false otherwise
   */
  public boolean isSongPlaying() {
    return audioPlayer.isPlaying();
  }

  /**
   * Sets the muted state of the player.
   *
   * @param b true to mute, false to unmute
   */
  private void setMuted(boolean b) {
    muted = b;
  }

  /**
   * Displays a list of all available songs in the playlist.
   */
  public void listSongs() {
    System.out.println(Formatting.BLUE_BACKGROUND_BRIGHT + "Available Songs:" + Formatting.RESET);
    System.out.println(songFileManager.toString());
  }

  /**
   * Enables loop mode for the current track.
   * Does nothing if no track is playing.
   */
  public void loop() {
    if (audioPlayer != null) {
      audioPlayer.loop();
      System.out.println(Formatting.BLUE_BOLD + "Song looped." + Formatting.RESET);
    } else {
      System.out.println(Formatting.RED_BOLD + "No song is playing." + Formatting.RESET);
    }
  }
}
