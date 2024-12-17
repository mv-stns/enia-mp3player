package com.mp3player.presentation;

import javafx.scene.layout.Pane;

public class Controller<P extends Pane> {
  protected P root;

  public P getRoot() {
    return root;
  }
}
