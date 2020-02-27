package com.chervatiuk.playwithme.checks;

import androidx.annotation.Nullable;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class RWPaths implements Check {

  private static final String[] PATHS = {
      "/etc",
      "/sbin",
      "/system",
      "/system/bin",
      "/system/sbin",
      "/system/xbin",
      "/vendor/bin",
  };

  @Override
  public boolean execute() {
    final String[] lines = mountReader();

    if (lines == null){
      // Could not read, assume false;
      return false;
    }

    for (final String line : lines) {

      // Split lines into parts
      final String[] args = line.split(" ");

      if (args.length < 4){
        // If we don't have enough options per line, skip this and log an error
        continue;
      }

      final String mountPoint = args[1];
      final String mountOptions = args[3];

      for(final String pathToCheck: PATHS) {
        if (mountPoint.equalsIgnoreCase(pathToCheck)) {
          // Split options out and compare against "rw" to avoid false positives
          for (final String option : mountOptions.split(",")){
            if (option.equalsIgnoreCase("rw")){
              return true;
            }
          }
        }
      }
    }

    return false;
  }

  @Nullable
  private String[] mountReader() {
    try {
      InputStream inputstream = Runtime.getRuntime().exec("mount").getInputStream();
      if (inputstream == null) return null;
      String propVal = new Scanner(inputstream).useDelimiter("\\A").next();
      return propVal.split("\n");
    } catch (IOException | NoSuchElementException e) {
      return null;
    }
  }

}
