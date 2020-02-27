package com.chervatiuk.playwithme.checks;

import java.io.IOException;

public class SuExecution implements Check {

  @Override
  public boolean execute() {
    try {
      Runtime.getRuntime().exec("su");
      return true;
    } catch (IOException e) {
      return false;
    }
  }
}
