package com.chervatiuk.playwithme.checks;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public abstract class Binary implements Check {

  private static final String[] PATHS ={
      "/data/local/",
      "/data/local/bin/",
      "/data/local/xbin/",
      "/sbin/",
      "/su/bin/",
      "/system/bin/",
      "/system/bin/.ext/",
      "/system/bin/failsafe/",
      "/system/sd/xbin/",
      "/system/usr/we-need-root/",
      "/system/xbin/",
      "/cache/",
      "/data/",
      "/dev/"
  };

  @Override
  public boolean execute() {
    return checkForBinary(fileName());
  }

  protected abstract String fileName();

  private boolean checkForBinary(String filename) {
    for (String path : getPaths()) {
      final File file = new File(path, filename);
      if (file.exists()) {
        return true;
      }
    }
    return false;
  }

  private String[] getPaths(){
    final ArrayList<String> paths = new ArrayList<>(Arrays.asList(PATHS));
    final String sysPaths = System.getenv("PATH");
    if (sysPaths == null || sysPaths.isEmpty()){
      return PATHS;
    }
    for (String path : sysPaths.split(":")){
      if (!path.endsWith("/")){
        path = path + '/';
      }
      if (!paths.contains(path)){
        paths.add(path);
      }
    }
    return paths.toArray(new String[0]);
  }
}
