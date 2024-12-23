package com.mp3player.utils;

import java.util.Arrays;
import javafx.scene.Node;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.BorderWidths;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

public class Constants {
  public static final boolean DEBUG = true;

  public static void debug(Object... message) {
    if (!DEBUG) return;

    for (Object msg : message) {
      if (msg instanceof StackTraceElement[]) {
        System.out.println(Arrays.toString((StackTraceElement[]) msg));
      } else {
        System.out.println(msg);
      }
    }
  }

  public static void debugNode(Node... nodes) {
    if (!DEBUG) return;
    
    for (Node node : nodes) {
      if (node instanceof Region) {
        ((Region) node).setBorder(new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
      }
    }
  }
}
