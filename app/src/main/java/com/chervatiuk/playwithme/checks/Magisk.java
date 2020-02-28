package com.chervatiuk.playwithme.checks;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class Magisk implements Check {

  static {
    System.loadLibrary("magisk-lib");
  }

  private static final String[] MOUNT_PATHS = {
      "/sbin/.magisk/",
      "/sbin/.core/mirror",
      "/sbin/.core/img",
      "/sbin/.core/db-0/magisk.db"
  };


  public native boolean isMagiskPresentNative();

  @Override
  public boolean execute() {
    boolean isMagiskPresent = false;
    final int pid = android.os.Process.myPid();
    final String cwd = String.format("/proc/%d/mounts", pid);
    final File file = new File(cwd);
    try {
      final FileInputStream fis = new FileInputStream(file);
      final BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
      String str;
      int count = 0;

      StringBuilder builder = new StringBuilder();

      while ((str = reader.readLine()) != null) {
        builder.append(str).append("\n");
        for (String path : MOUNT_PATHS) {
          if (str.contains(path)) {
            count++;
          }
        }
      }
      reader.close();
      fis.close();
      if (count > 1) {
        isMagiskPresent = true;
      } else {
        isMagiskPresent = isMagiskPresentNative();
      }
    } catch (IOException ignored) {
    }
    return isMagiskPresent;
  }
}
