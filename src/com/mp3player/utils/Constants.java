package com.mp3player.utils;

import java.util.Arrays;

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
}
